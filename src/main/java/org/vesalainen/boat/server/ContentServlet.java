/*
 * Copyright (C) 2016 tkv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vesalainen.boat.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.boat.server.pages.BaseContainer;
import org.vesalainen.boat.server.pages.GridContainer;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.boat.server.pages.PageType;
import org.vesalainen.code.TimeToLivePropertySetter;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileServlet;
import org.vesalainen.web.I18nResourceBundle;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class ContentServlet extends JQueryMobileServlet<JQueryMobileDocument,Model>
{
    public static final String Action = "/boatserver";
    private DataSource dataSource;

    public ContentServlet(DataSource dataSource)
    {
        this.dataSource = dataSource;
        this.i18nSupport = new I18nResourceBundle("text");
    }

    @Override
    protected JQueryMobileDocument createDocument()
    {
        JQueryMobileDocument doc = new JQueryMobileDocument(threadLocalModel);
        doc.setAjax(false);
        Element head = doc.getHead();
        head.addElement("script").setAttr("src", "/sse.js");
        head.addElement("link").setAttr("rel", "stylesheet").setAttr("href", "/rwd-height.css");
        head.addElement("link").setAttr("rel", "stylesheet").setAttr("href", "/boat-server.css");
        head.addElement("script").setAttr("src", "/page-control.js");
        head.addElement("script").setAttr("src", "/stats.js");
        doc.getRawBody().addRenderer(new PagesContent(threadLocalModel));
        return doc;
    }

    @Override
    protected void onService(HttpServletRequest req, HttpServletResponse resp, Parameters parameters, Context<Model> context, Model model) throws ServletException, IOException
    {
        String assign = parameters.getParameter("assign");
        if (assign != null)
        {
            parameters.remove("assign");
            String pattern = assign.replace('-', '.');
            String field = BeanHelper.prefix(pattern)+'='+BeanHelper.suffix(pattern);
            parameters.put(field, "");
            parameters.put("sendFragment", "");
            super.onService(req, resp, parameters, context, model);
        }
        else
        {
            String refresh = parameters.getParameter("refresh");
            if (refresh != null)
            {
                DataSource source = DataSource.getInstance();
                Set<String> freshProperties = source.getFreshProperties();
                resp.setContentType("application/json");
                String json = freshProperties.stream().collect(Collectors.joining("\",\"", "{\"refresh\":[\"", "\"]}"));
                resp.setContentLength(json.length());
                resp.getWriter().print(json);
                resp.flushBuffer();
            }
            else
            {
                super.onService(req, resp, parameters, context, model);
            }
        }
    }
    
    @Override
    protected void onSubmit(Model ctx, String field)
    {
    }
    
    @Override
    protected Model createData()
    {
        return new Model(threadLocalModel);
    }

    @Override
    protected <T> T createObject(Model data, String field, Class<T> cls, String hint)
    {
        if (hint == null || hint.isEmpty())
        {
            return super.createObject(data, field, cls, hint);
        }
        try
        {
            if (GridContainer.class.equals(cls))
            {
                Layout layout = Layout.valueOf(hint);
                Class<? extends BaseContainer> type = layout.getType();
                Constructor<? extends BaseContainer> constructor = type.getConstructor(ThreadLocal.class);
                return (T) constructor.newInstance(threadLocalModel);
            }
            if (MeterPage.class.equals(cls))
            {
                PageType pageType = PageType.valueOf(hint);
                Class<? extends MeterPage> type = pageType.getType();
                Constructor<? extends MeterPage> constructor = type.getConstructor(ThreadLocal.class);
                return (T) constructor.newInstance(threadLocalModel);
            }
            throw new IllegalArgumentException("no objectFactory for "+field+" "+cls+" "+hint);
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

}


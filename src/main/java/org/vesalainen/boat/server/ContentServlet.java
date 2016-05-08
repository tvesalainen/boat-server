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
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.boat.server.pages.GridContainer;
import org.vesalainen.boat.server.pages.LocationContainer;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.boat.server.pages.Page12;
import org.vesalainen.boat.server.pages.PageType;
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
        head.addElement("script").setAttr("src", "/page-control.js");
        doc.getRawBody().addRenderer(new PagesContent(threadLocalModel));
        return doc;
    }

    @Override
    protected void onService(HttpServletRequest req, HttpServletResponse resp, Parameters parameters, Context<Model> context, Model model) throws ServletException, IOException
    {
        String assign = parameters.getParameter("assign");
        if (assign == null)
        {
            super.onService(req, resp, parameters, context, model);
        }
        else
        {
            parameters.remove("assign");
            String pattern = assign.replace('-', '.');
            String field = BeanHelper.prefix(pattern)+'='+BeanHelper.suffix(pattern);
            parameters.put(field, "");
            parameters.put("sendFragment", "");
            super.onService(req, resp, parameters, context, model);
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
        if (GridContainer.class.equals(cls))
        {
            switch (hint)
            {
                case "location":
                    return (T) new LocationContainer(threadLocalModel);
                default:
                    throw new UnsupportedOperationException(hint+ "not supported");
            }
        }
        if (MeterPage.class.equals(cls))
        {
            PageType pageType = PageType.valueOf(hint);
            switch (pageType)
            {
                case Page12:
                    return (T) new Page12(threadLocalModel);
                default:
                    throw new UnsupportedOperationException(pageType+ "not supported");
            }
        }
        return null;
    }

}


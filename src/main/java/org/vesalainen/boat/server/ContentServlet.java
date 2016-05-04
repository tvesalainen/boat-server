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

import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.boat.server.pages.Page12;
import org.vesalainen.boat.server.pages.PageType;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileServlet;
import org.vesalainen.web.I18nResourceBundle;

/**
 *
 * @author tkv
 */
public class ContentServlet extends JQueryMobileServlet<JQueryMobileDocument,Model>
{
    public static final String Action = "/boatserver";

    public ContentServlet()
    {
        this.i18nSupport = new I18nResourceBundle("text");
    }

    @Override
    protected JQueryMobileDocument createDocument()
    {
        JQueryMobileDocument doc = new JQueryMobileDocument(threadLocalData);
        doc.setAjax(false);
        Element head = doc.getHead();
        head.addElement("script").setAttr("src", "/sse.js");
        head.addElement("script").setAttr("src", "/page-control.js");
        doc.getRawBody().addRenderer(new PagesContent(threadLocalData));
        return doc;
    }
    
    @Override
    protected void onSubmit(Model ctx, String field)
    {
    }
    
    @Override
    protected Model createData()
    {
        return new Model(threadLocalData);
    }

    @Override
    protected <T> T createObject(org.vesalainen.boat.server.Model data, String field, Class<T> cls, String hint)
    {
        if (hint == null || hint.isEmpty())
        {
            return super.createObject(data, field, cls, hint);
        }
        if (MeterPage.class.equals(cls))
        {
            PageType pageType = PageType.valueOf(hint);
            switch (pageType)
            {
                case Page12:
                    return (T) new Page12(threadLocalData);
            }
        }
        return null;
    }

}


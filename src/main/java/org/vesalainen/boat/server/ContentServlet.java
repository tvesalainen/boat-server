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

import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.Link;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.servlet.bean.DynamicQuery;

/**
 *
 * @author tkv
 */
public class ContentServlet extends BaseServlet
{
    public static final String Action = "/con";

    @Override
    protected Document createDocument()
    {
        JQueryMobileDocument doc = new JQueryMobileDocument(getLabel("BoatServer"));
        JQueryMobileDocument.Page meterPage = doc.getPage("meterPage");
        
        Element header = meterPage.getHeader();
        Element addButton = header.addElement("a")
                .addClasses("ui-btn", "ui-btn-right", "ui-icon-home", "ui-btn-icon-left")
                .addText(getLabel("addMeter"));
        DynamicQuery query = new DynamicQuery(threadLocalData, doc.getCharset(), allFields);
        Link href = new Link("href", AddMeterServlet.Action, query);
        addButton.setAttr(href);
        return doc;
    }
    
}

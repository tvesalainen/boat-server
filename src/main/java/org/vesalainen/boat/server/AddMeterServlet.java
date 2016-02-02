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
public class AddMeterServlet extends BaseServlet
{
    public static final String Action = "/add";

    @Override
    protected Document createDocument()
    {
        JQueryMobileDocument doc = new JQueryMobileDocument(getLabel("addPage"));
        JQueryMobileDocument.Page main = doc.getPage("addPage");
        Element form = main.addElement("form")
                .setAttr("method", "post");
        DynamicQuery query = new DynamicQuery(threadLocalData, doc.getCharset(), allFields);
        Link href = new Link("action", UnitsServlet.Action, query);
        form.setAttr(href);
        addInputs(form, "newMeter");
        addRestAsHiddenInputs(form);
        return doc;
    }
    
}

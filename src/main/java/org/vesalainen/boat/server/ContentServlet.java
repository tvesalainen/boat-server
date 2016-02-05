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
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument.JQueryMobilePage;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.web.servlet.bean.DynamicQuery;

/**
 *
 * @author tkv
 */
public class ContentServlet extends BaseServlet
{
    public static final String Action = "/con";

    @Override
    protected JQueryMobileDocument createDocument()
    {
        JQueryMobileDocument doc = new JQueryMobileDocument(threadLocalData);
        createAddPage(doc);
        return doc;
    }
    
    private void createAddPage(JQueryMobileDocument doc)
    {
        JQueryMobilePage page = createPage(doc, "addPage");
        JQueryMobileForm form = page.addForm(Action);
        form.addInputs("pageType", "addPage");
        form.addRestAsHiddenInputs();
    }
    private JQueryMobilePage createPage(JQueryMobileDocument doc, String id)
    {
        JQueryMobilePage page = doc.getPage("addPage");
        return page;
    }
}

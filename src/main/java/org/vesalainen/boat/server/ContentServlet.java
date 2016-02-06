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

import java.util.HashMap;
import java.util.Map;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.boat.server.pages.Page12;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.html.jquery.mobile.JQueryMobileServlet;

/**
 *
 * @author tkv
 */
public class ContentServlet extends JQueryMobileServlet<ContentDocument,Context>
{
    public static final String Action = "/con";

    @Override
    protected ContentDocument createDocument()
    {
        ContentDocument doc = new ContentDocument(threadLocalData);
        doc.getBody().addContent(new DynamicPages(doc, threadLocalData));
        createAddPage(doc);
        return doc;
    }
    
    private void createAddPage(JQueryMobileDocument doc)
    {
        JQueryMobilePage page = createPage(doc, "addPage");
        Element header = page.getHeader();
        header.addElement("a")
                .setAttr("href", new LastMeterPage(threadLocalData))
                .addClasses("ui-btn", "ui-icon-home", "ui-btn-icon-left")
                .addText(doc.getLabel("lastMeterPage"));
        header.addElement("h1")
                .addText(doc.getLabel("Add new meter page"));
        JQueryMobileForm form = page.addForm(Action);
        form.addInputs("pageType", "addPage");
        form.addRestAsHiddenInputs();
    }
    private JQueryMobilePage createPage(JQueryMobileDocument doc, String id)
    {
        JQueryMobilePage page = doc.getPage("addPage");
        return page;
    }

    @Override
    protected void onSubmit(Context ctx, String field)
    {
        if (field != null)
        {
            switch (field)
            {
                case "addPage":
                    addPage(ctx);
                    break;
            }
        }
    }
    
    private void addPage(Context ctx)
    {
        PageType pageType = ctx.getPageType();
        if (pageType != null)
        {
            String page = "page"+ctx.nextId();
            ctx.getPages().add(page);
            ctx.getTypeMap().put(page, pageType);
            MeterPage mp = document.getPage(pageType);
            ctx.getGridMap().addAll(page, mp.createInitList());
        }
    }
    
    @Override
    protected Context createData()
    {
        return new Context();
    }
    
}


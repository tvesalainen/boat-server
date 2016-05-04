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
package org.vesalainen.boat.server.pages;

import static org.vesalainen.boat.server.ContentServlet.Action;
import org.vesalainen.boat.server.Model;
import org.vesalainen.html.Element;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public class AddPage extends ThreadLocalBeanRenderer<Model>
{

    public AddPage(ThreadLocal<Context<Model>> threadLocalModel)
    {
        super(threadLocalModel);
    }

    @Override
    protected Renderer create()
    {
        JQueryMobilePage page = new JQueryMobilePage(null, "addPage", threadLocalModel);
        Element header = page.getHeader();
        header.addElement("h1")
                .addText(I18n.getLabel("Add new meter page"));
        
        Context<Model> ctx = threadLocalModel.get();
        Element ul = page.addElement("ul").setDataAttr("role", "listview");
        for (Enum e : PageType.values())
        {
            String n = e.toString();
            Renderer d = I18n.getLabel(n);
            Element li = ul.addElement("li");
            li.addElement("a").setAttr("href", "#").addText(d);
            li.addElement("a").setAttr("href", "#").setDataAttr("pattern", ctx.inputName("pages+"+n)).addClasses("add-page");
        }
        return page;
    }

}

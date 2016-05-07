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

import java.io.IOException;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.boat.server.Model;
import org.vesalainen.boat.server.PageScript;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Element;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.math.UnitCategory;
import org.vesalainen.math.UnitType;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public abstract class MeterPage extends ThreadLocalBeanRenderer<Model,JQueryMobilePage>
{
    protected GridContainer[] grid;
    protected String pageId;
 
    public MeterPage(ThreadLocal<Context<Model>> threadLocalData, int gridCount)
    {
        super(threadLocalData);
        this.grid = new GridContainer[gridCount];
        for (int ii=0;ii<gridCount;ii++)
        {
            grid[ii] = new GridContainer(threadLocalData);
        }
    }

    protected JQueryMobilePage createPage(ThreadLocal<Context<Model>> threadLocalModel)
    {
        return createPage("${pageId}", threadLocalModel);
    }
    protected JQueryMobilePage createPage(String pageId, ThreadLocal<Context<Model>> threadLocalModel)
    {
        JQueryMobilePage page = new JQueryMobilePage(null, pageId, threadLocalModel);
        page.getScriptContainer().addScript(new PageScript());
        Element popup = page.getMain().addElement("div").setDataAttr("role", "popup").setAttr("id", "${pageId}-popup");
        popup.add(createMeterChooser());
        return page;
    }
    private Renderer createMeterChooser()
    {
        ContainerContent container = new ContainerContent();
        Element div = container.addElement("div").setDataAttr("role", "collapsible");
        div.addElement("h4").addText(I18n.getLabel("location"));
        Element ul = div.addElement("ul").setDataAttr("role", "listview");
        Element form = ul.addElement("form").setAttr("id", "${pageId}-popup-form").setAttr("method", "post").setAttr("action", "location");
        form.addRenderer(getUnitChooser(UnitCategory.Coordinate));
        form.addElement("a").setAttr("href", "#").addText(I18n.getLabel("submit")).addClasses("post-grid");
        return container;
    }
    
    private Renderer getUnitChooser(UnitCategory category)
    {
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").setAttr("for", "${pageId}-popup-unit").addText(category.name());
        Element select = fieldSet.addElement("select").setAttr("name", "unit").setAttr("id", "${pageId}-popup-unit");
        for (UnitType e : UnitType.values())
        {
            if (category.equals(e.getCategory()))
            {
                String n = e.toString();
                Renderer d = I18n.getLabel(n);
                Element option = select.addElement("option").setAttr("value", n).addText(d);
            }
        }
        return fieldSet;
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        pageId = getWebPattern();
        super.append(out);
    }

    public String getPageId()
    {
        return pageId;
    }

    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }

    public GridContainer[] getGrid()
    {
        return grid;
    }

}

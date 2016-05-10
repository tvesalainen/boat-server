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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.vesalainen.boat.server.DataSource;
import org.vesalainen.boat.server.Layout;
import org.vesalainen.boat.server.Model;
import org.vesalainen.boat.server.PageScript;
import org.vesalainen.code.TimeToLivePropertySetter;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.math.UnitCategory;
import org.vesalainen.math.UnitType;
import org.vesalainen.parsers.nmea.NMEACategory;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.AbstractSSESource;
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
        Set<String> set = DataSource.NmeaProperties.stream().collect(Collectors.toSet());
        Map<NMEACategory, List<String>> map = DataSource.NmeaProperties.stream().collect(Collectors.groupingBy((s)->{return DataSource.NmeaProperties.getCategory(s);}));
        for (Layout layout : Layout.values())
        {
            String required = null;
            if (!layout.getRequired().isEmpty())
            {
                required = layout.getRequired().get(0);
            }
            Element div = container.addElement("div");
            if (required != null)
            {
                div.setAttr("style", "display: none;").setAttr(AbstractSSESource.EventSink, required+"-Unitless-Visible");;
            }
            if (set.containsAll(layout.getRequired()))
            {
                if (HasProperty.class.isAssignableFrom(layout.getType()))
                {
                    Element div1 = div.addElement("div").setDataAttr("role", "collapsible");
                    div1.addElement("h4").addText(I18n.getLabel(layout));
                    for (NMEACategory cat : map.keySet())
                    {
                        Element div2 = div1.addElement("div").setDataAttr("role", "collapsible");
                        div2.addElement("h4").addText(I18n.getLabel(cat));
                        Element ul = div2.addElement("ul").setDataAttr("role", "listview");
                        map.get(cat).stream().forEach((property) ->
                        {
                            Element li = ul.addElement("li").setAttr("style", "display: none;").setAttr(AbstractSSESource.EventSink, property+"-Unitless-Visible");;
                            UnitType unit = DataSource.NmeaProperties.getType(property);
                            li.addContent(getOption(layout, unit.getCategory(), property));
                        });
                    }
                }
                else
                {
                    UnitCategory unitCategory = null;
                    if (required != null)
                    {
                        UnitType unit = DataSource.NmeaProperties.getType(required);
                        unitCategory = unit.getCategory();
                    }
                    container.addContent(getOption(layout, unitCategory, layout.name()));
                }
            }
            
        }
        return container;
    }
    
    private Content getOption(Layout layout, UnitCategory unitCategory, String property)
    {
        Element div = new Element("div").setDataAttr("role", "collapsible");
        div.addElement("h4").addText(I18n.getLabel(property));
        Element ul = div.addElement("ul").setDataAttr("role", "listview");
        Element form = ul.addElement("form").setAttr("id", "${pageId}-popup-form").setAttr("method", "post").setAttr("action", layout);
        if (HasProperty.class.isAssignableFrom(layout.getType()))
        {
            form.addElement("input").setAttr("type", "text").setAttr("name", "property").setAttr("value", property).setAttr(new BooleanAttribute("hidden", true));
        }
        if (HasUnit.class.isAssignableFrom(layout.getType()))
        {
            form.addRenderer(getUnitChooser(unitCategory));
        }
        form.addElement("a").setAttr("href", "#").addText(I18n.getLabel("submit")).addClasses("post-grid");
        return div;
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

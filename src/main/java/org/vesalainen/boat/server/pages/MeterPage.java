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
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.vesalainen.boat.server.DataSource;
import org.vesalainen.boat.server.Layout;
import org.vesalainen.boat.server.Model;
import org.vesalainen.boat.server.PageScript;
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
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public abstract class MeterPage extends ThreadLocalBeanRenderer<Model,JQueryMobilePage>
{
    private static final long serialVersionUID = 1L;
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
        Set<String> set = DataSource.getInstance().getAllProperties();
        Map<NMEACategory, List<String>> map = set.stream().collect(Collectors.groupingBy((s)->{return DataSource.getInstance().nmeaProperties.getCategory(s);}));
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
                div.setAttr("style", "display: none;").setDataAttr("property", required);
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
                            Element li = ul.addElement("li").setAttr("style", "display: none;").setDataAttr("property", property);
                            UnitType unit = DataSource.getInstance().getUnit(property);
                            li.addContent(getOption(layout, unit.getCategory(), property));
                        });
                    }
                }
                else
                {
                    UnitCategory unitCategory = null;
                    if (required != null)
                    {
                        UnitType unit = DataSource.getInstance().getUnit(required);
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
        Element form = ul.addElement("form").setAttr("id", property+"_form").setAttr("method", "post").setAttr("action", layout);
        if (HasProperty.class.isAssignableFrom(layout.getType()))
        {
            form.addElement("input").setAttr("type", "text").setAttr("name", "property").setAttr("value", property).setAttr(new BooleanAttribute("hidden", true));
        }
        if (HasSeconds.class.isAssignableFrom(layout.getType()))
        {
            form.addElement("label").setAttr("for", property+"_seconds").addText(I18n.getLabel("seconds"));
            form.addElement("input").setAttr("type", "range").setAttr("name", "seconds").setAttr("min", "1").setAttr("max", "1000").setAttr("id", property+"_seconds");
        }
        if (HasUnit.class.isAssignableFrom(layout.getType()))
        {
            form.addRenderer(getUnitChooser(unitCategory, property));
        }
        form.addElement("a").setAttr("href", "#").addText(I18n.getLabel("submit")).addClasses("post-grid", "ui-btn");
        return div;
    }

    private Renderer getUnitChooser(UnitCategory category, String property)
    {
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").setAttr("for", property+"_unit").addText(category.name());
        Element select = fieldSet.addElement("select").setAttr("name", "unit").setAttr("id", property+"_unit");
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

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Arrays.deepHashCode(this.grid);
        hash = 67 * hash + Objects.hashCode(this.pageId);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final MeterPage other = (MeterPage) obj;
        if (!Objects.equals(this.pageId, other.pageId))
        {
            return false;
        }
        if (!Arrays.deepEquals(this.grid, other.grid))
        {
            return false;
        }
        return true;
    }

}

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
import java.util.EnumMap;
import java.util.Map;
import org.vesalainen.boat.server.ContentServlet;
import org.vesalainen.boat.server.GridContext;
import org.vesalainen.boat.server.Id;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ClassAttribute;
import org.vesalainen.html.Container;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.EnumDynContent;
import org.vesalainen.html.EnumDynContentSupport;
import org.vesalainen.html.Placeholder;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.math.UnitCategory;
import org.vesalainen.math.UnitType;
import org.vesalainen.util.EnumMapList;
import org.vesalainen.util.MapList;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.EnumInput;

/**
 *
 * @author tkv
 */
public class UnitPage extends JQueryMobilePage implements EnumDynContent<GridContext,Id>
{
    private final EnumDynContent<GridContext,Id> dynContent = new EnumDynContentSupport<>(Id.class);
    private final Map<UnitCategory,Container> categoryMapList = new EnumMap<>(UnitCategory.class);
    private Placeholder<Container> options = new Placeholder<>();

    public UnitPage(JQueryMobileDocument document)
    {
        this(document, new Placeholder());
    }
    public UnitPage(JQueryMobileDocument document, Placeholder wrap)
    {
        super(wrap, document);
        attach(Id.UnitPage, wrap);
        setDataAttr("dialog", true);
        JQueryMobileForm form = addForm("post", new DynString(ContentServlet.Action, "?", wrap(Id.Query)));
        form.setAttr("id", wrap(Id.Form));
        String field = "unit";
        EnumInput input = new EnumInput(document.getThreadLocalData(), document.getDataType(), field);
        document.getFieldMap().put(field, input);
        Element fieldSet = form.addElement("fieldset");
        fieldSet.addElement("label").addText(I18n.getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", wrap(Id.Input)).setAttr("data-native-menu", false);
        select.addContent(options);
        form.addInput("setUnit", new ClassAttribute("ui-icon-action"));
        form.addRestAsHiddenInputs();
        
        for (UnitType opt : UnitType.values())
        {
            UnitCategory category = opt.getCategory();
            Container container = categoryMapList.get(category);
            if (container == null)
            {
                container = new ContainerContent();
                categoryMapList.put(category, container);
            }
            String n = opt.toString();
            Content d = I18n.getLabel(n);
            Element option = container.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected",wrap(Id.SelectedUnit, opt)));
        }
    }

    @Override
    public void append(GridContext param, Appendable out) throws IOException
    {
        UnitType unit = param.meterData.getUnit();
        if (unit != null)
        {
            options.setValue(categoryMapList.get(unit.getCategory()));
            append(out);
        }
    }
    
    @Override
    public final Placeholder wrap(Id key)
    {
        return dynContent.wrap(key);
    }

    @Override
    public Placeholder<Object> wrap(Id key, Object comp)
    {
        return dynContent.wrap(key, comp);
    }

    @Override
    public void provision(GridContext param)
    {
        dynContent.provision(param);
    }

    @Override
    public final void attach(Id key, Placeholder wrap)
    {
        dynContent.attach(key, wrap);
    }
    
}

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

import java.io.IOException;
import org.vesalainen.html.ClassAttribute;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.EnumDynContent;
import org.vesalainen.html.EnumDynContentSupport;
import org.vesalainen.html.Placeholder;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.web.servlet.bean.EnumInput;

/**
 *
 * @author tkv
 */
public class MeterForm extends JQueryMobileForm implements EnumDynContent<GridContext,Id>
{
    private final EnumDynContent<GridContext,Id> dynContent = new EnumDynContentSupport<>(Id.class);

    public MeterForm(JQueryMobileDocument document)
    {
        super(document, null, "post", null);
        setAttr("action", new DynString(ContentServlet.Action, "?", wrap(Id.Query)));
        setAttr("id", wrap(Id.Form));
        hasHideScript = true;
        String field = "meter";
        EnumInput input = new EnumInput(document.getThreadLocalData(), document.getDataType(), field);
        document.getFieldMap().put(field, input);
        Element fieldSet = addElement("fieldset");
        fieldSet.addElement("label").addText(getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", wrap(Id.Input)).setAttr("data-native-menu", false);
        for (MeterType opt : MeterType.values())
        {
            String n = opt.toString();
            String d = document.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
        }
        addInput("addMeter", new SimpleAttribute("data-inline", true), new ClassAttribute("ui-icon-action"));
        addRestAsHiddenInputs();
    }

    @Override
    public void append(GridContext param, Appendable out) throws IOException
    {
        append(out);
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
    public void attach(Id key, Placeholder wrap)
    {
        dynContent.attach(key, wrap);
    }
    
}

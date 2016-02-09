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
import org.vesalainen.html.DynContent;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.http.Query;
import org.vesalainen.math.UnitType;
import org.vesalainen.util.Wrap;
import org.vesalainen.web.servlet.bean.EnumInput;

/**
 *
 * @author tkv
 */
public class UnitForm extends JQueryMobileForm
{
    private final Query query;
    private final Wrap<String> formId;
    private final Wrap<String> inputId;

    public UnitForm(JQueryMobileDocument document, Query query, Wrap<String> formId, Wrap<String> inputId)
    {
        super(document, null, "post", new DynString(ContentServlet.Action, "?", query));
        this.query = query;
        this.formId = formId;
        this.inputId = inputId;
        setAttr("id", formId);
        hasHideScript = true;
        String field = "unit";
        EnumInput input = new EnumInput(document.getThreadLocalData(), document.getDataType(), field);
        document.getFieldMap().put(field, input);
        Element fieldSet = addElement("fieldset");
        fieldSet.addElement("label").addText(getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", inputId).setAttr("data-native-menu", false);
        for (UnitType opt : UnitType.values())
        {
            String n = opt.toString();
            String d = document.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
        }
        addInput("setUnit", new SimpleAttribute("data-inline", true), new ClassAttribute("ui-icon-action"));
        addRestAsHiddenInputs();
    }

}

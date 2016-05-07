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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.vesalainen.boat.server.ContentServlet;
import org.vesalainen.boat.server.Id;
import org.vesalainen.boat.server.Layout;
import org.vesalainen.boat.server.MeterChoice;
import org.vesalainen.boat.server.Model;
import org.vesalainen.html.ClassAttribute;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Placeholder;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.EnumInput;

/**
 *
 * @author tkv
 */
public class MeterChooser extends BaseContainer
{

    public MeterChooser(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData);
    }

    @Override
    protected void addSVGContent(Element svg)
    {
    }
/*
    public MeterChooser(JQueryMobileDocument document)
    {
        super(document, null, "post", null);
        setAttr("action", new DynString(ContentServlet.Action, "?", wrap(Id.Query)));
        setAttr("id", wrap(Id.Form));
        String field = "meter";
        EnumInput input = new EnumInput(document.getThreadLocalData(), document.getDataType(), field);
        Element fieldSet = addElement("fieldset");
        fieldSet.addElement("label").addText(I18n.getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", wrap(Id.Input)).setAttr("data-native-menu", true);
        Map<Layout,List<MeterChoice>> map = Arrays.stream(MeterChoice.values()).collect(Collectors.groupingBy(MeterChoice::getLayout));
        for (Layout layout : map.keySet())
        {
            Element optgroup = select.addElement("optgroup")
                    .setAttr("label", I18n.getLabel(layout.name()));
            for (MeterChoice opt : map.get(layout))
            {
                String n = opt.toString();
                Renderer d = I18n.getLabel(n);
                optgroup.addElement("option").setAttr("value", n).addText(d);
            }
        }
        addInput("addMeter", new ClassAttribute("ui-icon-action"));
    }
*/

}

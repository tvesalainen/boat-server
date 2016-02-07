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
import java.util.List;
import java.util.Map;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.util.Lists;
import org.vesalainen.util.MapList;
import org.vesalainen.web.servlet.bean.MultipleSelectorInput;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 */
public class DynamicPages extends ThreadLocalContent<Context>
{
    private final ContentDocument document;
    private MeterForm form;

    public DynamicPages(ContentDocument document, ThreadLocal local)
    {
        super(local);
        this.document = document;
        this.form = new MeterForm(document);
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Context ctx = local.get();
        Map<String, PageType> typeMap = ctx.getTypeMap();
        MapList<String, String> gridMap = ctx.getGridMap();
        for (String pg : ctx.getPages())
        {
            PageType pt = typeMap.get(pg);
            MeterPage page = document.getMeterPage(pt);
            page.setPageId(pg);
            int idx = 0;
            for (String g : gridMap.get(pg))
            {
                if (g == null)
                {
                    page.setGrid(idx, form);
                }
                idx++;
            }
            page.append(out);
        }
    }
    private static class MeterForm extends JQueryMobileForm
    {

        public MeterForm(JQueryMobileDocument document)
        {
            super(document, null, "post", null);
            String field = "meter";
            List<Meter> options = Lists.create(Meter.values());
            MultipleSelectorInput<Context, String> input = new MultipleSelectorInput<>(document.getThreadLocalData(), document.getDataType(), field, options);
            document.getFieldMap().put(field, input);
            Element fieldSet = addElement("fieldset");
            fieldSet.addElement("label").addText(getLabel(field));
            Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
            select.setAttr(new BooleanAttribute("multiple", true));
            for (Meter opt : options)
            {
                String n = opt.toString();
                String d = document.getLabel(n);
                Element option = select.addElement("option")
                        .setAttr("value", n)
                        .setAttr("category", opt.getCategory())
                        .addText(d);
            }
        }
        
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ClassAttribute;
import org.vesalainen.html.DynContent;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.ParamContent;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.http.Query;
import org.vesalainen.util.Lists;
import org.vesalainen.util.MapList;
import org.vesalainen.util.Wrap;
import org.vesalainen.web.servlet.bean.MultipleSelectorInput;
import org.vesalainen.web.servlet.bean.SingleSelectorInput;
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
        Map<Integer, PageType> typeMap = ctx.getTypeMap();
        MapList<Integer, String> gridMap = ctx.getGridMap();
        for (int id : ctx.getPages())
        {
            PageType pt = typeMap.get(id);
            MeterPage page = document.getMeterPage(pt);
            page.setPageId(id);
            int idx = 0;
            for (String g : gridMap.get(id))
            {
                if (g == null)
                {
                    ParamContent<GridContext> grid = page.getGrid(idx);
                    grid.setContent(form);
                    grid.getParam().setPageId(id);
                }
                idx++;
            }
            page.append(out);
        }
    }
    private static class MeterForm extends JQueryMobileForm implements DynContent<GridContext>
    {
        private final Query query;
        private final Wrap<String> formId = new Wrap<>();
        private final Wrap<String> inputId = new Wrap<>();
        
        public MeterForm(JQueryMobileDocument document)
        {
            this(document, new Query());
        }
        public MeterForm(JQueryMobileDocument document, Query query)
        {
            super(document, null, "post", new DynString(ContentServlet.Action, query));
            this.query = query;
            setAttr("id", formId);
            hasHideScript = true;
            String field = "meter";
            List<Meter> options = Lists.create(Meter.values());
            SingleSelectorInput<Context, String> input = new SingleSelectorInput<>(document.getThreadLocalData(), document.getDataType(), field, options);
            document.getFieldMap().put(field, input);
            Element fieldSet = addElement("fieldset");
            fieldSet.addElement("label").addText(getLabel(field));
            Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", inputId).setAttr("data-native-menu", false);
            for (Meter opt : options)
            {
                String n = opt.toString();
                String d = document.getLabel(n);
                Element option = select.addElement("option")
                        .setAttr("value", n)
                        .addText(d);
            }
            addInput("addMeter", 
                    new SimpleAttribute("data-inline", true),
                    new ClassAttribute("ui-icon-action")
                    );
            addRestAsHiddenInputs();
        }
        
        @Override
        public void append(GridContext param, Appendable out) throws IOException
        {
            query.clear();
            int pageId = param.getPageId();
            int gridNo = param.getGridNo();
            query.add("pageId", String.valueOf(pageId));
            query.add("gridNo", String.valueOf(gridNo));
            formId.setValue("fp"+pageId+"g"+gridNo);
            inputId.setValue("ip"+pageId+"g"+gridNo);
            append(out);
        }
    }
}

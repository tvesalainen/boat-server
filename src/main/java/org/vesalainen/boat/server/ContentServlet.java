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

import java.util.List;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileServlet;
import org.vesalainen.http.Query;
import org.vesalainen.math.UnitType;
import org.vesalainen.util.TreeMapList;

/**
 *
 * @author tkv
 */
public class ContentServlet extends JQueryMobileServlet<ContentDocument,Context>
{
    public static final String Action = "/con";

    @Override
    protected ContentDocument createDocument()
    {
        ContentDocument doc = new ContentDocument(threadLocalData);
        doc.setAjax(false);
        Element head = doc.getHead();
        Element sse = head.addElement("script")
                .setAttr("src", "/sse.js");
        Element script = head.addElement("script");
        //script.addContent(DataSource.getInstance().createScript());
        script.addContent(new DynamicScripts(doc, threadLocalData));
        doc.getBody().addContent(new DynamicPages(doc, threadLocalData));
        return doc;
    }
    
    @Override
    protected void onSubmit(Context ctx, String field, Query query)
    {
        if (field != null)
        {
            switch (field)
            {
                case "addPage":
                    addPage(ctx);
                    break;
                case "addMeter":
                    addMeter(ctx, query);
                    break;
                case "setUnit":
                    setUnit(ctx, query);
                    break;
            }
        }
    }
    
    private void addPage(Context ctx)
    {
        PageType pageType = ctx.pageType;
        if (pageType != null)
        {
            int page = ctx.nextId();
            ctx.typeMap.put(page, pageType);
            MeterPage mp = document.getMeterPage(pageType);
            ctx.gridMap.addAll(page, mp.createInitList());
        }
    }
    
    private void addMeter(Context ctx, Query query)
    {
        MeterChoice meter = ctx.meter;
        int pageId = Integer.parseInt(query.get("pageId").get(0));
        int gridNo = Integer.parseInt(query.get("gridNo").get(0));
        TreeMapList<Integer, MeterData> gridMap = ctx.gridMap;
        List<MeterData> lst = gridMap.get(pageId);
        lst.set(gridNo, new MeterData(meter));
    }
    
    private void setUnit(Context ctx, Query query)
    {
        UnitType unit = ctx.unit;
        int pageId = Integer.parseInt(query.get("pageId").get(0));
        int gridNo = Integer.parseInt(query.get("gridNo").get(0));
        TreeMapList<Integer, MeterData> gridMap = ctx.gridMap;
        List<MeterData> lst = gridMap.get(pageId);
        MeterData md = lst.get(gridNo);
        md.setUnit(unit);
    }

    @Override
    protected Context createData()
    {
        return new Context();
    }

}


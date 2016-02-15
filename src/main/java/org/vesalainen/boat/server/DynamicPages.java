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

import org.vesalainen.boat.server.pages.MeterForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.vesalainen.boat.server.pages.AddMeterContent;
import org.vesalainen.boat.server.pages.AddPage;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.boat.server.pages.OneRowContainer;
import org.vesalainen.boat.server.pages.TackticalContainer;
import org.vesalainen.boat.server.pages.TwoRowContainer;
import org.vesalainen.boat.server.pages.UnitPage;
import org.vesalainen.html.DynContent;
import org.vesalainen.html.ParamContent;
import org.vesalainen.util.MapList;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 */
public class DynamicPages extends ThreadLocalContent<Context>
{
    private final ContentDocument document;
    private final AddMeterContent addMeterContent;
    private final Map<Layout,DynContent<GridContext,Id>> containerMap = new HashMap<>();
    private final AddPage addPage;
    private final UnitPage unitPage;

    public DynamicPages(ContentDocument document, ThreadLocal local)
    {
        super(local);
        this.document = document;
        this.addMeterContent = new AddMeterContent(document);
        this.addPage = new AddPage(document);
        this.unitPage = new UnitPage(document);
        containerMap.put(Layout.OneRow, new OneRowContainer(document));
        containerMap.put(Layout.TwoRow, new TwoRowContainer(document));
        containerMap.put(Layout.TackticalFullScreen, new TackticalContainer(document));
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Context ctx = local.get();
        Map<Integer, PageType> typeMap = ctx.typeMap;
        MapList<Integer, MeterData> gridMap = ctx.gridMap;
        for (Entry<Integer, List<MeterData>> e : gridMap.entrySet())
        {
            int pageId = e.getKey();
            PageType pt = typeMap.get(pageId);
            MeterPage page = document.getMeterPage(pt);
            page.setPageId(pageId);
            int gridNo = 0;
            List<ParamContent<GridContext,Id>> params = new ArrayList<>();
            for (MeterData meter : e.getValue())
            {
                ParamContent<GridContext,Id> grid = page.getGrid(gridNo);
                GridContext param = grid.getParam();
                param.pageId=pageId;
                param.meterData=meter;
                if (meter == null)
                {
                    grid.setContent(addMeterContent);
                }
                else
                {
                    DynContent<GridContext, Id> container = containerMap.get(meter.getType().getLayout());
                    if (container == null)
                    {
                        throw new IllegalArgumentException("container for "+meter.getType().getLayout()+" not found");
                    }
                    grid.setContent(container);
                    params.add(new ParamContent<>(param));
                }
                gridNo++;
            }
            page.append(out);
            for (ParamContent<GridContext,Id> param : params)
            {
                param.setContent(unitPage);
                param.append(out);
            }
        }
        addPage.append(out);
    }
}

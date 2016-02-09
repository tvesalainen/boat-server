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
import java.util.Map.Entry;
import org.vesalainen.boat.server.pages.MeterContainer;
import org.vesalainen.boat.server.pages.MeterPage;
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
    private final MeterForm form;
    private final MeterContainer meterContainer;

    public DynamicPages(ContentDocument document, ThreadLocal local)
    {
        super(local);
        this.document = document;
        this.form = new MeterForm(document);
        this.meterContainer = new MeterContainer(document);
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Context ctx = local.get();
        Map<Integer, PageType> typeMap = ctx.getTypeMap();
        MapList<Integer, MeterData> gridMap = ctx.getGridMap();
        for (Entry<Integer, List<MeterData>> e : gridMap.entrySet())
        {
            int id = e.getKey();
            PageType pt = typeMap.get(id);
            MeterPage page = document.getMeterPage(pt);
            page.setPageId(id);
            int idx = 0;
            for (MeterData meter : e.getValue())
            {
                ParamContent<GridContext> grid = page.getGrid(idx);
                GridContext param = grid.getParam();
                param.setPageId(id);
                param.setMeterData(meter);
                if (meter == null)
                {
                    grid.setContent(form);
                }
                else
                {
                    grid.setContent(meterContainer);
                }
                idx++;
            }
            page.append(out);
        }
    }
}

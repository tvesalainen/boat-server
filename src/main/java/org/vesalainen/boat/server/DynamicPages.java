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
import org.vesalainen.util.MapList;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 */
public class DynamicPages extends ThreadLocalContent<Context>
{
    private final ContentDocument document;

    public DynamicPages(ContentDocument document, ThreadLocal local)
    {
        super(local);
        this.document = document;
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
            List<String> ls = gridMap.get(pt);
            MeterPage page = document.getPage(pt);
            page.setPageId(pg);
            page.append(out);
        }
    }
    
}

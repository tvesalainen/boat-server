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
import org.vesalainen.html.DynString;
import org.vesalainen.js.AbstractScriptContainer;
import org.vesalainen.util.MapList;
import org.vesalainen.util.Wrap;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 */
public class DynamicScripts extends ThreadLocalContent<Context>
{
    private final ContentDocument document;
    private final Sc script;

    public DynamicScripts(ContentDocument document, ThreadLocal<Context> local)
    {
        super(local);
        this.document = document;
        this.script = new Sc();
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Context ctx = local.get();
        Map<Integer, PageType> typeMap = ctx.getTypeMap();
        MapList<Integer, String> gridMap = ctx.getGridMap();
        for (int pg : ctx.getPages())
        {
            PageType pt = typeMap.get(pg);
            List<String> ls = gridMap.get(pg);
            if (hasUninitializedGrids(ls))
            {
                script.pageId.setValue(pg);
                script.append(out);
            }
        }
    }

    private boolean hasUninitializedGrids(List<String> ls)
    {
        for (String s : ls)
        {
            if (s == null)
            {
                return true;
            }
        }
        return false;
    }
    
    private static class Sc extends AbstractScriptContainer
    {
        private Wrap<Integer> pageId = new Wrap();

        public Sc()
        {
            prefix = new DynString("$(document).on(\"pagecreate\",\"#page", pageId, "\",function(){");
            suffix = "});";
            addCode("$('.hidden').hide();");
        }
        
    }
}

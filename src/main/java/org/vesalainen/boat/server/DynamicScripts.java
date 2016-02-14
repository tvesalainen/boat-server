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
import org.vesalainen.html.DynString;
import org.vesalainen.js.AbstractScriptContainer;
import org.vesalainen.js.Function;
import org.vesalainen.util.MapList;
import org.vesalainen.util.TreeMapList;
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
        Map<Integer, PageType> typeMap = ctx.typeMap;
        TreeMapList<Integer, MeterData> gridMap = ctx.gridMap;
        List<Integer> pages = new ArrayList<>();
        pages.add(-1);
        pages.addAll(gridMap.keySet());
        pages.add(-1);
        int cnt = pages.size()-1;
        for (int ii=1;ii<cnt;ii++)
        {
            script.prevId.setValue(pages.get(ii-1));
            script.pageId.setValue(pages.get(ii));
            script.nextId.setValue(pages.get(ii+1));
            script.append(out);
        }
        if (!gridMap.isEmpty())
        {
            script.prevId.setValue(gridMap.lastKey());
            script.pageId.setValue(-1);
            script.nextId.setValue(gridMap.firstKey());
            script.append(out);
        }
    }

    private boolean hasUninitializedGrids(List<MeterData> ls)
    {
        for (MeterData m : ls)
        {
            if (m == null)
            {
                return true;
            }
        }
        return false;
    }
    
    private static class Sc extends AbstractScriptContainer
    {
        private Wrap<Integer> pageId = new Wrap();
        private Wrap<Integer> prevId = new Wrap();
        private Wrap<Integer> nextId = new Wrap();

        public Sc()
        {
            prefix = new DynString("$(document).on(\"pagecreate\",\"#page", pageId, "\",function(){");
            suffix = "});";
            addCode("$('.hidden').hide();");
            
            addCode("$(\"#page");
            addCode(pageId);
            addCode("\").on(\"swipeleft\",");
            Function pf = new Function(null);
            addCode(pf);
            pf.addCode("$( \":mobile-pagecontainer\" ).pagecontainer( \"change\", \"#page");
            pf.addCode(prevId);
            pf.addCode("\", { role: \"page\"});");
            addCode(");");
            
            addCode("$(\"#page");
            addCode(pageId);
            addCode("\").on(\"swiperight\",");
            Function nf = new Function(null);
            addCode(nf);
            nf.addCode("$( \":mobile-pagecontainer\" ).pagecontainer( \"change\", \"#page");
            nf.addCode(nextId);
            nf.addCode("\", { role: \"page\"});");
            addCode(");");
        }
        
    }
}

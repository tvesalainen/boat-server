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
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 */
public class LastMeterPage extends ThreadLocalContent<Context>
{

    public LastMeterPage(ThreadLocal<Context> local)
    {
        super(local);
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append('#');
        Context ctx = local.get();
        List<Integer> pages = ctx.getPages();
        if (!pages.isEmpty())
        {
            Integer id = pages.get(pages.size()-1);
            out.append("page");
            out.append(String.valueOf(id));
        }
    }
    
}

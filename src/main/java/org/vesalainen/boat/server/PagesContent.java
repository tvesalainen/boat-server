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
import org.vesalainen.html.RendererSupport;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 */
public class PagesContent extends ThreadLocalContent<Model>
{

    public PagesContent(ThreadLocal<org.vesalainen.web.servlet.bean.Context<Model>> local)
    {
        super(local);
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Context<Model> ctx = threadLocalModel.get();
        RendererSupport.render(ctx.getModel(), out);
    }
    
}

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
package org.vesalainen.boat.server.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.boat.server.MeterData;
import org.vesalainen.boat.server.Model;
import org.vesalainen.html.Content;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public abstract class MeterPage extends ThreadLocalBeanRenderer<Model>
{
    protected BaseContainer[] grid;
    protected int pageId;
 
    public MeterPage(ThreadLocal<Context<Model>> threadLocalData, int gridCount)
    {
        super(threadLocalData);
        this.grid = new BaseContainer[gridCount];
        for (int ii=0;ii<gridCount;ii++)
        {
            grid[ii] = new MeterChooser(threadLocalData);
        }
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        String pattern = getPattern();
        String suffix = BeanHelper.suffix(pattern);
        pageId = Integer.parseInt(suffix);
        super.append(out);
    }

    public BaseContainer[] getGrid()
    {
        return grid;
    }

    public int getPageId()
    {
        return pageId;
    }

    public void setPageId(int pageId)
    {
        this.pageId = pageId;
    }

}

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
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.boat.server.Model;
import org.vesalainen.boat.server.PageScript;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public abstract class MeterPage extends ThreadLocalBeanRenderer<Model,JQueryMobilePage>
{
    protected BaseContainer[] grid;
    protected String pageId;
 
    public MeterPage(ThreadLocal<Context<Model>> threadLocalData, int gridCount)
    {
        super(threadLocalData);
        this.grid = new BaseContainer[gridCount];
        for (int ii=0;ii<gridCount;ii++)
        {
            grid[ii] = new MeterChooser(threadLocalData);
        }
    }

    protected JQueryMobilePage createPage(String pageId, ThreadLocal<Context<Model>> threadLocalModel)
    {
        JQueryMobilePage page = new JQueryMobilePage(null, pageId, threadLocalModel);
        page.getScriptContainer().addScript(new PageScript());
        return page;
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        pageId = getWebPattern();
        super.append(out);
    }

    public BaseContainer[] getGrid()
    {
        return grid;
    }

    public String getPageId()
    {
        return pageId;
    }

    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }

}

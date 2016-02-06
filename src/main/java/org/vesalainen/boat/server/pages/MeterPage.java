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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vesalainen.boat.server.PageType;
import org.vesalainen.html.Content;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.util.Wrap;

/**
 *
 * @author tkv
 */
public class MeterPage extends JQueryMobilePage
{
    protected Wrap<Content>[] grid;
    private Wrap<String> pageId = new Wrap();
 
    public MeterPage(JQueryMobileDocument document, int gridCount)
    {
        super(wrap(), document);
        pageId = (Wrap<String>) getId();
        this.grid = new Wrap[gridCount];
        for (int ii=0;ii<gridCount;ii++)
        {
            grid[ii] = new Wrap<>();
        }
    }

    public List<String> createInitList()
    {
        List<String> list = new ArrayList<>();
        for (int ii=0;ii<grid.length;ii++)
        {
            list.add(null);
        }
        return list;
    }
    public int getGridCount()
    {
        return grid.length;
    }
    
    public void setPageId(String pageid)
    {
        pageId.setValue(pageid);
    }
    
    public void setGrid(int index, Content content)
    {
        grid[index].setValue(content);
    }
    
    private static Wrap<String> wrap()
    {
        return new Wrap();
    }
}

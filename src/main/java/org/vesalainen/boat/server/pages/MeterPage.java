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
import java.util.List;
import org.vesalainen.boat.server.GridContext;
import org.vesalainen.boat.server.Id;
import org.vesalainen.boat.server.MeterData;
import org.vesalainen.html.DynString;
import org.vesalainen.html.ParamContent;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.util.Wrap;

/**
 *
 * @author tkv
 */
public class MeterPage extends JQueryMobilePage
{
    protected ParamContent<GridContext,Id>[] grid;
    private Wrap<Integer> pageId;
 
    public MeterPage(JQueryMobileDocument document, int gridCount)
    {
        this(document, gridCount, new Wrap<Integer>());
    }
    
    private MeterPage(JQueryMobileDocument document, int gridCount, Wrap<Integer> pageId)
    {
        super(new DynString("page", pageId), document);
        this.pageId = pageId;
        this.grid = new ParamContent[gridCount];
        for (int ii=0;ii<gridCount;ii++)
        {
            grid[ii] = new ParamContent<>(new GridContext(-1, ii));
        }
    }

    public List<MeterData> createInitList()
    {
        List<MeterData> list = new ArrayList<>();
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
    
    public void setPageId(int pageid)
    {
        pageId.setValue(pageid);
    }
    
    public ParamContent<GridContext,Id> getGrid(int index)
    {
        return grid[index];
    }
    
}

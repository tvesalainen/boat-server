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

/**
 *
 * @author tkv
 */
public class GridContext
{
    private int pageId;
    private int gridNo;

    public GridContext(int pageId, int gridNo)
    {
        this.pageId = pageId;
        this.gridNo = gridNo;
    }

    public int getPageId()
    {
        return pageId;
    }

    public void setPageId(int pageId)
    {
        this.pageId = pageId;
    }

    public int getGridNo()
    {
        return gridNo;
    }

    public void setGridNo(int gridNo)
    {
        this.gridNo = gridNo;
    }
    
}

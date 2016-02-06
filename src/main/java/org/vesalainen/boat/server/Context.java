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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;
import org.vesalainen.web.InputType;

/**
 *
 * @author tkv
 */
public class Context
{
    private int nextId;
    private List<String> pages = new ArrayList<>();
    private MapList<String,String> gridMap = new HashMapList<>();
    private Map<String,PageType> typeMap = new HashMap<>();
    private String addPage;
    private PageType pageType;

    public int nextId()
    {
        return nextId++;
    }
    
    public PageType getPageType()
    {
        return pageType;
    }

    public void setPageType(PageType pageType)
    {
        this.pageType = pageType;
    }

    
    @InputType("submit")
    public String getAddPage()
    {
        return addPage;
    }

    public void setAddPage(String addPage)
    {
        this.addPage = addPage;
    }

    public int getNextId()
    {
        return nextId;
    }

    public void setNextId(int nextId)
    {
        this.nextId = nextId;
    }

    @InputType(itemType=String.class)
    public List<String> getPages()
    {
        return pages;
    }

    public void setPages(List<String> pages)
    {
        this.pages = pages;
    }

    @InputType(itemType=String.class, itemType2=String.class)
    public MapList<String, String> getGridMap()
    {
        return gridMap;
    }

    public void setGridMap(MapList<String, String> gridMap)
    {
        this.gridMap = gridMap;
    }

    @InputType(itemType=String.class, itemType2=PageType.class)
    public Map<String, PageType> getTypeMap()
    {
        return typeMap;
    }

    public void setTypeMap(Map<String, PageType> typeMap)
    {
        this.typeMap = typeMap;
    }

}

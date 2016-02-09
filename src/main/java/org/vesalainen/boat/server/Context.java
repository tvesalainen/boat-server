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

import java.util.HashMap;
import java.util.Map;
import org.vesalainen.math.UnitType;
import org.vesalainen.util.TreeMapList;
import org.vesalainen.web.InputType;

/**
 *
 * @author tkv
 */
public class Context
{
    private int nextId;
    private TreeMapList<Integer,MeterData> gridMap = new TreeMapList<>();
    private Map<Integer,PageType> typeMap = new HashMap<>();
    private String addPage;
    private String addMeter;
    private String setUnit;
    private PageType pageType;
    private MeterType meter;
    private UnitType unit;

    public String getSetUnit()
    {
        return setUnit;
    }

    public void setSetUnit(String setUnit)
    {
        this.setUnit = setUnit;
    }

    public UnitType getUnit()
    {
        return unit;
    }

    public void setUnit(UnitType unit)
    {
        this.unit = unit;
    }

    public MeterType getMeter()
    {
        return meter;
    }

    public void setMeter(MeterType meter)
    {
        this.meter = meter;
    }

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
    public String getAddMeter()
    {
        return addMeter;
    }

    public void setAddMeter(String addMeter)
    {
        this.addMeter = addMeter;
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

    @InputType(itemType=Integer.class, itemType2=MeterData.class)
    public TreeMapList<Integer, MeterData> getGridMap()
    {
        return gridMap;
    }

    public void setGridMap(TreeMapList<Integer, MeterData> gridMap)
    {
        this.gridMap = gridMap;
    }

    @InputType(itemType=Integer.class, itemType2=PageType.class)
    public Map<Integer, PageType> getTypeMap()
    {
        return typeMap;
    }

    public void setTypeMap(Map<Integer, PageType> typeMap)
    {
        this.typeMap = typeMap;
    }

}

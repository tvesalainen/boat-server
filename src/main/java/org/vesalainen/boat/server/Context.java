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
    public int nextId;
    @InputType(itemType=Integer.class, itemType2=MeterData.class)
    public TreeMapList<Integer,MeterData> gridMap = new TreeMapList<>();
    @InputType(itemType=Integer.class, itemType2=PageType.class)
    public Map<Integer,PageType> typeMap = new HashMap<>();
    @InputType("submit")
    public String addPage;
    @InputType("submit")
    public String addMeter;
    @InputType("submit")
    public String setUnit;
    public PageType pageType;
    public MeterType meter;
    public UnitType unit;

    public int nextId()
    {
        return nextId++;
    }
    
}

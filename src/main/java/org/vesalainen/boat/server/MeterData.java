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

import org.vesalainen.json.JSONBean;
import org.vesalainen.math.UnitType;

/**
 *
 * @author tkv
 */
public class MeterData implements JSONBean
{
    private MeterType type;
    private UnitType unit;

    public MeterData()
    {
    }

    public MeterData(MeterType type)
    {
        this.type = type;
        this.unit = type.getUnit();
    }

    public MeterData(MeterType type, UnitType unit)
    {
        this.type = type;
        this.unit = unit;
    }

    public MeterType getType()
    {
        return type;
    }

    public void setType(MeterType type)
    {
        this.type = type;
    }

    public UnitType getUnit()
    {
        return unit;
    }

    public void setUnit(UnitType unit)
    {
        this.unit = unit;
    }

    @Override
    public String toString()
    {
        return type + ":" + unit;
    }
    
}

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
import java.util.List;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.json.JSONBean;
import org.vesalainen.math.UnitType;
import org.vesalainen.parsers.nmea.NMEAProperties;
import org.vesalainen.util.Lists;
import org.vesalainen.web.InputType;

/**
 *
 * @author tkv
 */
public class MeterData implements JSONBean
{
    public MeterChoice type;
    public UnitType unit;
    @InputType(itemType=String.class)
    public List<String> properties;

    public MeterData()
    {
        properties = new ArrayList<>();
    }

    public MeterData(MeterChoice type)
    {
        this.type = type;
        NMEAProperties instance = NMEAProperties.getInstance();
        if (type.getProperties() == null)
        {
            String property = BeanHelper.field(type.name());
            this.unit = instance.getType(property);  // default type
            this.properties = Lists.create(property);
        }
        else
        {
            properties = Lists.create(type.getProperties());
            for (String p : this.properties)
            {
                String prop = BeanHelper.field(p);
                if (!instance.isProperty(prop))
                {
                    throw new IllegalArgumentException(prop+" not NMEAProperty");
                }
                if (unit == null)
                {
                    unit = NMEAProperties.getInstance().getType(prop);
                }
                else
                {
                    if (!unit.equals(NMEAProperties.getInstance().getType(prop)))
                    {
                        throw new UnsupportedOperationException("multi unit not supported yet!");
                    }
                }
            }
        }
    }

    public List<String> getProperties()
    {
        return properties;
    }

    public UnitType getUnit()
    {
        return unit;
    }

    public void setUnit(UnitType unit)
    {
        this.unit = unit;
    }

    public MeterChoice getType()
    {
        return type;
    }

    public void setType(MeterChoice type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return type + "-" + unit;
    }
    
}

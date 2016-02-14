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

import static org.vesalainen.boat.server.Layout.*;

/**
 *
 * @author tkv
 */
public enum MeterType
{
    Location(TwoRow, "Latitude", "Longitude"),
    DepthBelowTransducer(OneRow),
    TrueBearing(OneRow),
    MagneticBearing(OneRow),
    TrueHeading(OneRow),
    WaterTemperature(OneRow),
    RelativeWindAngle(OneRow),
    TrueWindAngle(OneRow),
    WindSpeed(OneRow),
    Latitude(OneRow),
    Longitude(OneRow)
    ;
    
    private final Layout layout;
    private final String[] properties;

    private MeterType(Layout layout)
    {
        this.layout = layout;
        this.properties = null;
    }

    private MeterType(Layout layout, String... properties)
    {
        this.layout = layout;
        this.properties = properties;
    }

    public String[] getProperties()
    {
        return properties;
    }

    public Layout getLayout()
    {
        return layout;
    }
    
}

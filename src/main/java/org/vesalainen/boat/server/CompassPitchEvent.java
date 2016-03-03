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

import java.util.Locale;
import org.json.JSONObject;
import org.vesalainen.math.UnitType;

/**
 *
 * @author tkv
 */
public class CompassPitchEvent extends Event
{
    private static final double ViewAngle = 45;
    
    public CompassPitchEvent(DataSource source, String event, String property, UnitType currentUnit, UnitType propertyUnit)
    {
        super(source, event, property, currentUnit, propertyUnit);
    }
    
    @Override
    protected void populate(JSONObject jo, String property, double value)
    {
        jo.keySet().clear();
        jo.put("transform", String.format(Locale.US, "scale(1,%.3f)", Math.cos(Math.toRadians(90-ViewAngle-value))));
    }
    
}

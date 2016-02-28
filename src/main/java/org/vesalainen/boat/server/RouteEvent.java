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
public class RouteEvent extends Event
{
    private static final double A = Math.pow(40, 0.1);
    
    private double bearingToDestination;
    private double crossTrackError;
    
    public RouteEvent(DataSource source, String event, UnitType currentUnit, UnitType propertyUnit)
    {
        super(source, event, new String[] {"bearingToDestination", "crossTrackError"}, currentUnit, propertyUnit);
    }

    @Override
    protected void populate(JSONObject jo, String property, double value)
    {
        switch (property)
        {
            case "bearingToDestination":
                bearingToDestination = value;
                break;
            case "crossTrackError":
                crossTrackError = value;
                break;
            default:
                throw new IllegalArgumentException(property+" unexpected");
        }
        jo.keySet().clear();
        double scale = 1.0/Math.pow(A, Math.abs(crossTrackError));
        double translate = Math.signum(crossTrackError)*(30-scale*30);
        jo.put("transform", String.format(Locale.US, " rotate(%.0f)  translate(%.3f,0) scale(%.3f,1)", bearingToDestination, translate, scale));
    }

}

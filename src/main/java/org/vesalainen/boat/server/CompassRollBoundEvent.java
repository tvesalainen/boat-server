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

import org.json.JSONObject;
import org.vesalainen.math.sliding.AbstractTimeoutSlidingBound;
import org.vesalainen.math.sliding.TimeoutSlidingMax;
import org.vesalainen.math.sliding.TimeoutSlidingMin;

/**
 *
 * @author tkv
 */
public class CompassRollBoundEvent extends RotateEvent
{
    private static final long Timeout = 15*60*1000;
    private static final int Size = 16*60;
    private AbstractTimeoutSlidingBound bound;
    
    public CompassRollBoundEvent(DataSource source, String eventString, String property, String ext)
    {
        super(source, eventString, property, null, "rotate(%.1f)");
        switch (ext)
        {
            case "Port":
                bound = new TimeoutSlidingMax(Size, Timeout);
                break;
            case "Starboard":
                bound = new TimeoutSlidingMin(Size, Timeout);
                break;
            default:
                throw new IllegalArgumentException(ext+" unknown");
        }
    }

    @Override
    protected void populate(JSONObject jo, String property, double value)
    {
        bound.accept(-value);
        double b = bound.getBound();
        if (b < 0)
        {
            b += 360;
        }
        super.populate(jo, property, b);
    }

}

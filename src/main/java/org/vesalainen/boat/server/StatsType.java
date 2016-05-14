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

import org.vesalainen.math.sliding.StatsSupplier;
import org.vesalainen.math.sliding.TimeoutStats;

/**
 *
 * @author tkv
 */
public enum StatsType
{
    Ave(TimeoutStats::fast),
    Min(TimeoutStats::getMin),
    Max(TimeoutStats::getMax),
    Last(TimeoutStats::last),
    Prev(TimeoutStats::previous),
    LastTime(TimeoutStats::lastTime),
    PrevTime(TimeoutStats::previousTime),
    FirstTime(TimeoutStats::firstTime)
    ;
    
    private final StatsSupplier func;

    private StatsType(StatsSupplier func)
    {
        this.func = func;
    }

    public StatsSupplier getFunc()
    {
        return func;
    }
    
}

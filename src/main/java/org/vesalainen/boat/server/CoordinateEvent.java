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

import org.vesalainen.math.UnitType;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public class CoordinateEvent extends Event
{
    private char[] chars;
    
    public CoordinateEvent(DataSource source, String event, String property, UnitType currentUnit, UnitType propertyUnit, char... chars)
    {
        super(source, event, new String[] {property}, currentUnit, propertyUnit);
        this.chars = chars;
    }

    @Override
    public void fire(String property, double value)
    {
        source.fireEvent(eventString, format(value));
    }

    @Override
    protected String format(double lat)
    {
        switch (currentUnit)
        {
            case Deg:
                return deg(lat);
            case DegMin:
                return degmin(lat);
            case DegMinSec:
                return degminsec(lat);
            default:
                throw new UnsupportedOperationException(currentUnit+" no supported");
        }
    }
    private String degmin(double value)
    {
        char ns = value > 0 ? chars[0] : chars[1];
        double min = Math.abs(value);
        int deg = (int) min;
        min = min-deg;
        return String.format(I18n.getLocale(),
                "%c %d\u00b0 %.3f'", 
                ns,
                deg,
                min*60
        );
    }

    private String deg(double value)
    {
        char ns = value > 0 ? chars[0] : chars[1];
        double min = Math.abs(value);
        return String.format(I18n.getLocale(),
                "%c %.6f\u00b0", 
                ns,
                min
        );
    }

    private String degminsec(double value)
    {
        char ns = value > 0 ? chars[0] : chars[1];
        double m = Math.abs(value);
        int deg = (int) m;
        m = m-deg;
        int min = (int) (m*60);
        double sec = m-(double)min/60.0;
        return String.format(I18n.getLocale(),
                "%c %d\u00b0 %d' %.1f\"", 
                ns,
                deg,
                min,
                sec*3600
        );
    }
}

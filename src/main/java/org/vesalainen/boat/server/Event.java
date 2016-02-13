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
import static org.vesalainen.boat.server.DataSource.NmeaProperties;
import org.vesalainen.math.UnitType;
import org.vesalainen.parsers.nmea.NMEAService;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public class Event
{
    protected final DataSource source;
    protected final String event;
    protected final String property;
    protected final UnitType currentUnit;
    protected UnitType propertyUnit;

    public Event(DataSource source, String event, String property, UnitType currentUnit, UnitType propertyUnit)
    {
        this.source = source;
        this.event = event;
        this.property = property;
        this.currentUnit = currentUnit;
        this.propertyUnit = propertyUnit;
    }

    public void fire(float value)
    {
        double dval = propertyUnit.convertTo(value, currentUnit);
        source.fireEvent(event, format(dval));
    }

    protected String format(double value)
    {
        String format;
        if (value < 10.0)
        {
            format = "%.2f";
        }
        else
        {
            format = "%.1f";
        }
        return String.format(I18n.getLocale(), format, value);
    }
    
    public String getProperty()
    {
        return property;
    }

    public UnitType getUnit()
    {
        return currentUnit;
    }

    public void register(NMEAService service)
    {
        if (NmeaProperties.isProperty(property))
        {
            service.addNMEAObserver(source, property);
        }
        else
        {
            throw new IllegalArgumentException(property+" is not NMEAObserver property");
        }
    }

}

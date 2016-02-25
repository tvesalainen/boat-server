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

import java.util.EnumMap;
import java.util.Map;
import org.json.JSONObject;
import static org.vesalainen.boat.server.DataSource.NmeaProperties;
import org.vesalainen.json.JsonHelper;
import org.vesalainen.math.UnitType;
import org.vesalainen.parsers.nmea.NMEAService;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public class Event
{
    private static final long RefreshLimit = 5000;
    
    protected static final Map<UnitType,String> formatMap = new EnumMap<>(UnitType.class);
    static
    {
        formatMap.put(UnitType.BEAUFORT, "%.0f %s");
        formatMap.put(UnitType.DEGREE, "%.0f %s");
        formatMap.put(UnitType.FOOT, "%.0f %s");
        formatMap.put(UnitType.GFORCEEARTH, "%.2f %s");
        formatMap.put(UnitType.HPA, "%.0f %s");
        formatMap.put(UnitType.INCH, "%.0f %s");
        formatMap.put(UnitType.PASCAL, "%.0f %s");
        formatMap.put(UnitType.RADIAN, "%.2f %s");
        formatMap.put(UnitType.YARD, "%.0f %s");
    }

    protected final DataSource source;
    protected final String event;
    protected final String property;
    protected final UnitType currentUnit;
    protected UnitType propertyUnit;
    protected JSONObject json = new JSONObject();
    protected JSONObject prev = new JSONObject();
    protected long lastFire;

    public Event(DataSource source, String event, String property, UnitType currentUnit, UnitType propertyUnit)
    {
        this.source = source;
        this.event = event;
        this.property = property;
        this.currentUnit = currentUnit;
        this.propertyUnit = propertyUnit;
    }

    public void fire(double value)
    {
        populate(json, convert(value));
        long now = System.currentTimeMillis();
        if (!json.similar(prev) || now-lastFire > RefreshLimit)
        {
            source.fireEvent(event, json);
            prev = JsonHelper.copy(json, prev);
            lastFire = now;
        }
    }

    protected double convert(double value)
    {
        if (propertyUnit != null)
        {
            return propertyUnit.convertTo(value, currentUnit);
        }
        else
        {
            return value;
        }
    }
    
    protected void populate(JSONObject jo, double value)
    {
        json.keySet().clear();
        json.put("text", format(value));
    }
    protected String format(double value)
    {
        String format = formatMap.get(currentUnit);
        if (format == null)
        {
            if (value < 10.0)
            {
                format = "%.2f %s";
            }
            else
            {
                format = "%.1f %s";
            }
        }
        return String.format(I18n.getLocale(), format, value, currentUnit.getUnit());
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

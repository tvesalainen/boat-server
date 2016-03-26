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

import java.util.function.DoubleUnaryOperator;
import org.json.JSONObject;
import org.vesalainen.bean.BeanHelper;
import static org.vesalainen.boat.server.DataSource.NmeaProperties;
import org.vesalainen.boat.server.pages.EventAction;
import org.vesalainen.json.JsonHelper;
import org.vesalainen.math.UnitType;

/**
 *
 * @author tkv
 */
public class Event
{
    private static final long RefreshLimit = 5000;
    
    protected final DataSource source;
    protected final String eventString;
    protected final String property;
    protected final UnitType currentUnit;
    protected final UnitType propertyUnit;
    protected final String action;
    protected final BiFormat format;
    protected final DoubleUnaryOperator func;
    protected JSONObject json = new JSONObject();
    protected JSONObject prev = new JSONObject();
    protected long lastFire;

    public Event(DataSource source, String event, String[] properties, UnitType currentUnit, UnitType propertyUnit)
    {
        throw new UnsupportedOperationException("String[] properties");
    }
    
    public Event(DataSource source, String eventString, String property, UnitType currentUnit, UnitType propertyUnit)
    {
        throw new UnsupportedOperationException("String[] properties");
    }
    
    public Event(DataSource source, String eventString, String property, UnitType currentUnit, UnitType propertyUnit, EventAction action)
    {
        this.source = source;
        this.eventString = eventString;
        this.property = property;
        this.currentUnit = currentUnit;
        this.propertyUnit = propertyUnit;
        this.action = action.getAction();
        this.format = action.getFormat();
        this.func = action.getFunc();
    }

    public static Event create(DataSource source, String eventString)
    {
        String[] evs = eventString.split("-");
        UnitType currentUnit = null;
        UnitType propertyUnit = null;
        MeterChoice meterChoice;
        String property;
        EventAction action = EventAction.DEFAULT;
        String ext = null;
        switch (evs.length)
        {
            case 4:
                ext = evs[3];
            case 3:
                action = EventAction.valueOf(evs[2]);
            case 2:
                currentUnit = UnitType.valueOf(evs[1]);
            case 1:
                meterChoice = MeterChoice.valueOf(evs[0]);
                property = BeanHelper.field(evs[0]);
                propertyUnit = NmeaProperties.getType(property);
                break;
            default:
                throw new IllegalArgumentException(eventString+" illegal");
        }
        return new Event(source, eventString, property, currentUnit, propertyUnit, action);
    }
    public void fire(String property, double value)
    {
        json.keySet().clear();
        value = convert(value);
        value = (double) func.applyAsDouble(value);
        json.put(action, format.format(value, currentUnit));
        long now = System.currentTimeMillis();
        if (!json.similar(prev) || now-lastFire > RefreshLimit)
        {
            source.fireEvent(eventString, json);
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
    
    protected void populate(JSONObject jo, String property, double value)
    {
        throw new UnsupportedOperationException("not supported");
    }
    
    protected String format(double value)
    {
        throw new UnsupportedOperationException("not supported");
    }
    
    public String getProperty()
    {
        return property;
    }

    public UnitType getUnit()
    {
        return currentUnit;
    }

}

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.vesalainen.code.PropertySetter;
import org.vesalainen.math.UnitType;
import org.vesalainen.parsers.nmea.NMEAProperties;
import org.vesalainen.parsers.nmea.NMEAService;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class DataSource extends AbstractSSESource implements PropertySetter
{
    public static final String Action = "/sse";
    public static final NMEAProperties nmeaProperties = NMEAProperties.getInstance();
    private static DataSource source;
    private final NMEAService service;
    private final Map<String,Event> eventMap = new HashMap<>();
    private final Map<String,Event> propertyMap = new HashMap<>();

    public DataSource() throws IOException
    {
        super(Action);
        service = new NMEAService("224.0.0.3", 10110);
        service.start();
    }

    public static DataSource getInstance() throws IOException
    {
        if (source == null)
        {
            source = new DataSource();
        }
        return source;
    }
    
    @Override
    protected void addEvent(String eventString)
    {
        Event event = new Event(eventString);
        eventMap.put(eventString, event);
        String property = event.getProperty();
        propertyMap.put(property, event);
        if (nmeaProperties.isProperty(property))
        {
            event.setPropertyUnit(nmeaProperties.getType(property));
            service.addNMEAObserver(this, property);
        }
    }

    @Override
    protected void removeEvent(String event)
    {
        Event ev = eventMap.get(event);
        if (ev != null)
        {
            eventMap.remove(event);
            String property = ev.getProperty();
            propertyMap.remove(property);
            if (nmeaProperties.isProperty(property))
            {
                service.removeNMEAObserver(this, property);
            }
        }
    }

    @Override
    public String[] getPrefixes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, boolean arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, byte arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, char arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, short arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, int arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, long arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, float arg)
    {
        Event ev = propertyMap.get(property);
        if (ev != null)
        {
            ev.fire(arg);
        }
    }

    @Override
    public void set(String property, double arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String property, Object arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private class Event
    {
        private final String event;
        private final String property;
        private final UnitType eventUnit;
        private UnitType propertyUnit;

        public Event(String ev)
        {
            this.event = ev;
            String[] evs = ev.split("-");
            property = Character.toLowerCase(evs[0].charAt(0))+evs[0].substring(1);
            eventUnit = UnitType.valueOf(evs[1]);
        }

        public void fire(float value)
        {
            fireEvent(event, String.valueOf(value));
        }
        
        public String getProperty()
        {
            return property;
        }

        public UnitType getUnit()
        {
            return eventUnit;
        }

        public UnitType getPropertyUnit()
        {
            return propertyUnit;
        }

        public void setPropertyUnit(UnitType propertyUnit)
        {
            this.propertyUnit = propertyUnit;
        }
        
    }
}

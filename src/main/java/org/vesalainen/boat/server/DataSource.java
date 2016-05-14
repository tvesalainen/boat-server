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
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.vesalainen.code.PropertySetter;
import org.vesalainen.code.TimeToLivePropertySetter;
import org.vesalainen.math.sliding.TimeoutStatsService;
import org.vesalainen.parsers.nmea.NMEAProperties;
import org.vesalainen.parsers.nmea.NMEAService;
import org.vesalainen.util.FloatMap;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class DataSource extends AbstractSSESource implements PropertySetter
{
    public static final String Action = "/sse";
    public static final NMEAProperties NmeaProperties = NMEAProperties.getInstance();
    private static DataSource source;
    private final NMEAService service;
    private final Map<String,Event> eventMap = new HashMap<>();
    private final MapList<String,Event> propertyMapList = new HashMapList<>();
    private final FloatMap<String> valueMap = new FloatMap();
    private final TimeoutStatsService statsService;
    private final TimeToLivePropertySetter freshProperties = new TimeToLivePropertySetter(1, TimeUnit.HOURS);

    public DataSource()
    {
        super(Action);
        try
        {
            service = new NMEAService("224.0.0.3", 10110);
            NmeaProperties.stream().forEach((s)->service.addNMEAObserver(freshProperties, s));
            service.start();
            // we use EPOCH times to get shorter numbers in SVG
            Clock clock = Clock.offset(Clock.systemUTC(), Duration.between(Instant.now(), Instant.EPOCH));
            statsService = new TimeoutStatsService(clock, service.getDispatcher(), "boat-server");
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    public static DataSource getInstance()
    {
        if (source == null)
        {
            source = new DataSource();
        }
        return source;
    }
    
    public TimeToLivePropertySetter getFreshProperties()
    {
        return freshProperties;
    }

    public NMEAService getService()
    {
        return service;
    }

    public TimeoutStatsService getStatsService()
    {
        return statsService;
    }
    
    @Override
    protected void addEvent(String eventString)
    {
        System.err.println(eventString);
        Event event = Event.create(this, eventString);
        eventMap.put(eventString, event);
        String property = event.getProperty();
        propertyMapList.add(property, event);
        event.register();
    }

    @Override
    protected void removeEvent(String eventString)
    {
        Event event = eventMap.get(eventString);
        if (event != null)
        {
            eventMap.remove(eventString);
            String property = event.getProperty();
            propertyMapList.remove(property);
            event.unregister();
        }
    }

    public FloatMap<String> getValueMap()
    {
        return valueMap;
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
        set(property, (float)arg);
    }

    @Override
    public void set(String property, char arg)
    {
        set(property, (float)arg);
    }

    @Override
    public void set(String property, short arg)
    {
        set(property, (float)arg);
    }

    @Override
    public void set(String property, int arg)
    {
        set(property, (float)arg);
    }

    @Override
    public void set(String property, long arg)
    {
        set(property, (float)arg);
    }

    @Override
    public void set(String property, float arg)
    {
        valueMap.put(property, arg);
        try
        {
            fireAll(property, arg);
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    private void fireAll(String property, double arg)
    {
        propertyMapList.get(property).stream().forEach((ev) ->
        {
            ev.fire(property, arg);
        });
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

}

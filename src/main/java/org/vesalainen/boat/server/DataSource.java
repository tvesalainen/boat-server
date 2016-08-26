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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static java.util.logging.Level.SEVERE;
import org.vesalainen.code.PropertySetter;
import org.vesalainen.code.TimeToLivePropertySetter;
import org.vesalainen.dev.AbstractMeter;
import org.vesalainen.dev.DevMeter;
import org.vesalainen.math.UnitType;
import org.vesalainen.math.sliding.TimeoutStatsService;
import org.vesalainen.parsers.nmea.NMEAProperties;
import org.vesalainen.parsers.nmea.NMEAService;
import org.vesalainen.util.DoubleMap;
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
    private static DataSource source;
    public final NMEAProperties nmeaProperties = NMEAProperties.getInstance();
    private final Set<String> allProperties = new HashSet<>();
    private AbstractMeter meterService;
    private final NMEAService service;
    private final Map<String,Event> eventMap = new HashMap<>();
    private final MapList<String,Event> propertyMapList = new HashMapList<>();
    private final DoubleMap<String> valueMap = new DoubleMap();
    private final TimeoutStatsService statsService;
    private final TimeToLivePropertySetter freshProperties = new TimeToLivePropertySetter(1, TimeUnit.HOURS);
    
    private DataSource()
    {
        super(Action);
        try
        {
            config("starting NMEAService");
            service = new NMEAService(Config.getNmeaMulticastAddress(), Config.getNmeaUDPPort());
            nmeaProperties.stream().forEach((s)->service.addNMEAObserver(freshProperties, s));
            allProperties.addAll(nmeaProperties.getAllProperties());
            service.start();
            // we use EPOCH times to get shorter numbers in SVG
            Clock clock = Clock.offset(Clock.systemUTC(), Duration.between(Instant.now(), Instant.EPOCH));
            config("starting TimeoutStatsService");
            statsService = new TimeoutStatsService(clock, service.getDispatcher(), "boat-server");
            config("starting DevMeter");
            meterService = DevMeter.getInstance(Config.getDevConfigFile());
            allProperties.addAll(meterService.getNames());
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    public Set<String> getAllProperties()
    {
        return allProperties;
    }

    public UnitType getUnit(String property)
    {
        if (nmeaProperties.isProperty(property))
        {
            return nmeaProperties.getType(property);
        }
        else
        {
            return meterService.getUnit(property);
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
    
    public Set<String> getFreshProperties()
    {
        Set<String> set = new HashSet<>();
        freshProperties.forEach((p)->set.add(p));
        set.addAll(meterService.getNames());
        return set;
    }

    public NMEAService getService()
    {
        return service;
    }

    public TimeoutStatsService getStatsService()
    {
        return statsService;
    }

    public AbstractMeter getMeterService()
    {
        return meterService;
    }
    
    @Override
    protected void addEvent(String eventString)
    {
        fine("add event %s", eventString);
        Event event = Event.create(this, eventString);
        eventMap.put(eventString, event);
        String property = event.getProperty();
        propertyMapList.add(property, event);
        event.register();
    }

    @Override
    protected void removeEvent(String eventString)
    {
        fine("remove event %s", eventString);
        Event event = eventMap.get(eventString);
        if (event != null)
        {
            eventMap.remove(eventString);
            String property = event.getProperty();
            propertyMapList.remove(property);
            event.unregister();
        }
    }

    public DoubleMap<String> getValueMap()
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
            log(SEVERE, ex, "set(%s, %f) -> %s", property, arg, ex.getMessage());
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
        valueMap.put(property, arg);
        try
        {
            fireAll(property, arg);
        }
        catch (Exception ex)
        {
            log(SEVERE, ex, "set(%s, %f) -> %s", property, arg, ex.getMessage());
        }
    }

    @Override
    public void set(String property, Object arg)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

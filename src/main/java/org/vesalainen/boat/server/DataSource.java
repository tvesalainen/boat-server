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
import org.vesalainen.code.PropertySetter;
import org.vesalainen.code.SimplePropertySetterDispatcher;
import org.vesalainen.code.TimeToLivePropertySetter;
import org.vesalainen.dev.AbstractMeter;
import org.vesalainen.dev.DevMeter;
import org.vesalainen.math.UnitType;
import org.vesalainen.math.sliding.TimeoutStatsService;
import org.vesalainen.math.sliding.TimeoutStatsService.StatsObserver;
import org.vesalainen.parsers.nmea.NMEAProperties;
import org.vesalainen.parsers.nmea.NMEAService;
import org.vesalainen.util.DoubleMap;
import org.vesalainen.web.servlet.sse.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class DataSource extends AbstractSSESource
{
    public static final String Action = "/sse";
    private static DataSource source;
    public final NMEAProperties nmeaProperties = NMEAProperties.getInstance();
    private final Set<String> allProperties = new HashSet<>();
    private AbstractMeter meterService;
    private final NMEAService service;
    private final Map<String,Event> eventMap = new HashMap<>();
    private final Dispatcher dispatcher = new Dispatcher();
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
            config("starting DevMeter");
            meterService = DevMeter.getInstance(Config.getDevConfigFile());
            config("starting TimeoutStatsService");
            // we use EPOCH times to get shorter numbers in SVG
            Clock clock = Clock.offset(Clock.systemUTC(), Duration.between(Instant.now(), Instant.EPOCH));
            statsService = new TimeoutStatsService(clock, dispatcher, "boat-server");
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
        UnitType unit;
        if (nmeaProperties.isProperty(property))
        {
            unit = nmeaProperties.getType(property);
            fine("nmea property %s has type %s", property, unit);
        }
        else
        {
            unit = meterService.getUnit(property);
            fine("meter property %s has type %s", property, unit);
        }
        return unit;
    }
    
    public double getMin(String property)
    {
        double min;
        if (nmeaProperties.isProperty(property))
        {
            min = nmeaProperties.getMin(property);
            fine("nmea property %s has min %f", property, min);
        }
        else
        {
            min = meterService.getMin(property);
            fine("meter property %s has min %f", property, min);
        }
        return min;
    }
    
    public double getMax(String property)
    {
        double max;
        if (nmeaProperties.isProperty(property))
        {
            max = nmeaProperties.getMax(property);
            fine("nmea property %s has max %f", property, max);
        }
        else
        {
            max = meterService.getMax(property);
            fine("meter property %s has max %f", property, max);
        }
        return max;
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
        //dispatcher.addObserver(property, event);
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
            //dispatcher.removeObserver(property, event);
            event.unregister();
        }
    }

    public DoubleMap<String> getValueMap()
    {
        return valueMap;
    }

    public void register(String property, PropertySetter ps)
    {
        dispatcher.addObserver(property, ps);
    }
    public void unregister(String property, PropertySetter ps)
    {
        dispatcher.removeObserver(property, ps);
    }
    public void registerStatsService(String property, StatsObserver observer, boolean isAngle)
    {
        fine("registerStatsService(%s %s)", property, observer);
        statsService.addObserver(property, observer, isAngle);
    }
    public void unregisterStatsService(String property, StatsObserver observer)
    {
        fine("unregisterStatsService(%s %s)", property, observer);
        statsService.removeObserver(property, observer);
    }
    private class Dispatcher extends SimplePropertySetterDispatcher
    {

        @Override
        public void addObserver(String property, PropertySetter ps)
        {
            if (nmeaProperties.isProperty(property))
            {
                fine("register %s %s to nmea service", property, ps);
                service.addNMEAObserver(ps, property);
            }
            else
            {
                fine("register %s %s to meter service", property, ps);
                meterService.register(ps, property, Config.getDevMeterPeriod(), TimeUnit.MILLISECONDS);
            }
            super.addObserver(property, ps);
        }

        @Override
        public void removeObserver(String property, PropertySetter ps)
        {
            super.removeObserver(property, ps);
            if (nmeaProperties.isProperty(property))
            {
                fine("unregister %s from nmea service", property);
                service.removeNMEAObserver(ps, property);
            }
            else
            {
                fine("unregister %s from meter service", property);
                meterService.unregister(ps, property);
            }
        }

        @Override
        public void set(String property, double arg)
        {
            valueMap.put(property, arg);
            super.set(property, arg);
        }

        @Override
        public void set(String property, float arg)
        {
            valueMap.put(property, arg);
            super.set(property, arg);
        }
        
    }
}

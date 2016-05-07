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
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
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

    public DataSource() throws IOException
    {
        super(Action);
        service = new NMEAService("224.0.0.3", 10110);
        NmeaProperties.stream().forEach((s)->service.addNMEAObserver(freshProperties, s));
        service.start();
        statsService = new TimeoutStatsService(service.getDispatcher(), "boat-server");
    }

    public static DataSource getInstance() throws IOException
    {
        if (source == null)
        {
            source = new DataSource();
        }
        return source;
    }
    
    public Stream<String> getFreshProperties()
    {
        return freshProperties.stream();
    }
    /*
    protected Event createEvent(String eventString)
    {
        String[] evs = eventString.split("-");
        UnitType currentUnit = null;
        UnitType propertyUnit = null;
        MeterChoice meterChoice;
        String property;
        EventAction transform = null;
        String ext = null;
        switch (evs.length)
        {
            case 4:
                ext = evs[3];
            case 3:
                transform = Action.valueOf(evs[2]);
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
        if (transform != null)
        {
            switch (transform)
            {
                case COMPASS:
                    switch (meterChoice)
                    {
                        case TrueHeading:
                            return new CompassRotateEvent(source, eventString, property, transform, "rotate(%.1f)");
                        case Pitch:
                            return new CompassPitchEvent(source, eventString, property, currentUnit, propertyUnit);
                        case Roll:
                            if (ext == null)
                            {
                                return new CompassRollEvent(source, eventString, property, transform);
                            }
                            else
                            {
                                return new CompassRollBoundEvent(source, eventString, property, ext);
                            }
                    }
                case ROUTE:
                    return new RouteEvent(source, eventString, currentUnit, propertyUnit);
                case ROTATE:
                    if (
                            MeterChoice.TrueHeading.equals(meterChoice) ||
                            MeterChoice.TrackMadeGood.equals(meterChoice)
                            )
                    {
                        return new RotateEvent(source, eventString, property, transform);
                    }
                    else
                    {
                        return new BoatRelativeEvent(source, eventString, property, transform);
                    }
                default:
                    throw new UnsupportedOperationException(transform+" not supported");
            }
        }
        else
        {
            switch (meterChoice)
            {
                case Latitude:
                    return new CoordinateEvent(source, eventString, property, currentUnit, propertyUnit, 'N', 'S');
                case Longitude:
                    return new CoordinateEvent(source, eventString, property, currentUnit, propertyUnit, 'E', 'W');
                default:
                    return new Event(source, eventString, new String[] {property}, currentUnit, propertyUnit);
            }
        }
    }
    */

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
        for (Event ev : propertyMapList.get(property))
        {
            ev.fire(property, arg);
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

}

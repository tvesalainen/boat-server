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

import java.util.concurrent.TimeUnit;
import static java.util.logging.Level.SEVERE;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.code.DoubleFire;
import org.vesalainen.math.UnitType;
import org.vesalainen.math.sliding.StatsSupplier;
import org.vesalainen.math.sliding.TimeoutStats;
import org.vesalainen.math.sliding.TimeoutStatsService.StatsObserver;
import org.vesalainen.util.CharSequences;
import org.vesalainen.util.logging.JavaLogging;

/**
 *
 * @author tkv
 */
public class Event extends JavaLogging implements DoubleFire
{
    private static final long RefreshLimit = 5000;
    
    protected final DataSource source;
    protected final String eventString;
    protected final String property;
    protected final UnitType currentUnit;
    protected final UnitType propertyUnit;
    protected final String action;
    protected final EventFormat format;
    protected final EventFunction func;
    protected final EventConversion conv;
    protected final boolean isObject;
    protected StringBuilder json = new StringBuilder();
    protected StringBuilder prev = new StringBuilder();
    protected long lastFire;
    protected EventContext ec;

    public Event(DataSource source, String eventString, String property, UnitType currentUnit, UnitType propertyUnit, EventAction action)
    {
        super(Event.class);
        this.source = source;
        this.eventString = eventString;
        this.property = property;
        this.currentUnit = currentUnit;
        this.propertyUnit = propertyUnit;
        this.action = action.getAction();
        this.format = action.getFormat();
        this.func = action.getFunc();
        this.conv = action.getConv();
        this.isObject = action.isObject();
        ec = new EventContext();
        ec.setUnit(currentUnit);
    }

    public static Event create(DataSource source, String eventString)
    {
        JavaLogging.getLogger(Event.class).fine("create event %s", eventString);
        String[] evs = eventString.split("-");
        UnitType currentUnit = null;
        UnitType propertyUnit = null;
        String property;
        EventAction action = EventAction.Default;
        int seconds = 0;
        StatsType statsType = null;
        try
        {
            switch (evs.length)
            {
                case 5:
                    statsType = StatsType.valueOf(evs[4]);
                case 4:
                    seconds = Integer.parseInt(evs[3]);
                case 3:
                    action = EventAction.valueOf(evs[2]);
                case 2:
                    currentUnit = UnitType.valueOf(evs[1]);
                case 1:
                    property = BeanHelper.property(evs[0]);
                    propertyUnit = DataSource.getInstance().nmeaProperties.getType(property);
                    break;
                default:
                    throw new IllegalArgumentException(eventString+" illegal");
            }
        }
        catch (Exception ex)
        {
            JavaLogging.getLogger(Event.class).log(SEVERE, ex, "event %s -> %s", eventString, ex);
            throw ex;
        }
        if (statsType == null)
        {
            return new Event(source, eventString, property, currentUnit, propertyUnit, action);
        }
        else
        {
            return new StatsEvent(source, eventString, property, currentUnit, propertyUnit, action, seconds, statsType.getFunc());
        }
    }
    
    public void register()
    {
        source.register(property, this);
    }
    
    public void unregister()
    {
        source.unregister(property, this);
    }
    
    @Override
    public void fire(String property, double value)
    {
        value = conv.apply(propertyUnit, currentUnit, value);
        value = (double) func.apply(value, source.getValueMap());
        ec.setValue(value);
        fire(ec);
    }
    
    public void fire(EventContext ec)
    {
        json.setLength(0);
        json.append("{\"");
        json.append(action);
        json.append("\":");
        if (!isObject)
        {
            json.append("\"");
        }
        format.format(json, ec);
        if (!isObject)
        {
            json.append("\"");
        }
        json.append("}");
        long now = System.currentTimeMillis();
        if (CharSequences.compare(json, prev) != 0 || now-lastFire > RefreshLimit)
        {
            System.err.println(eventString+" "+json+" "+prev);
            source.fireEvent(eventString, json);
            prev.setLength(0);
            prev.append(json);
            lastFire = now;
        }
    }
    
    public String getProperty()
    {
        return property;
    }

    public UnitType getUnit()
    {
        return currentUnit;
    }

    public static class StatsEvent extends Event implements StatsObserver
    {
        private int seconds;
        private StatsSupplier op;
        
        public StatsEvent(DataSource source, String eventString, String property, UnitType currentUnit, UnitType propertyUnit, EventAction action, int seconds, StatsSupplier op)
        {
            super(source, eventString, property, currentUnit, propertyUnit, action);
            this.seconds = seconds;
            this.op = op;
        }

        @Override
        public void register()
        {
            source.registerStatsService(property+':'+seconds, this, UnitType.Degree.equals(propertyUnit));
        }

        @Override
        public void unregister()
        {
            source.unregisterStatsService(property+':'+seconds, this);
        }

        @Override
        public void changed(TimeoutStats stats)
        {
            ec.setStats(stats);
            if (op != null)
            {
                fire(property, op.get(stats));
            }
            else
            {
                throw new UnsupportedOperationException("not supported");
            }
        }
        
        
    }

}

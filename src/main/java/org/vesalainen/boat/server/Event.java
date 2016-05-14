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

import org.vesalainen.bean.BeanHelper;
import static org.vesalainen.boat.server.DataSource.NmeaProperties;
import org.vesalainen.math.UnitType;
import org.vesalainen.math.sliding.StatsSupplier;
import org.vesalainen.math.sliding.TimeoutStats;
import org.vesalainen.math.sliding.TimeoutStatsService.StatsObserver;
import org.vesalainen.time.RelativeMillis;
import org.vesalainen.util.CharSequences;

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
    protected final EventFormat format;
    protected final EventFunction func;
    protected final EventConversion conv;
    protected StringBuilder json = new StringBuilder();
    protected StringBuilder prev = new StringBuilder();
    protected long lastFire;
    protected EventContext ec;

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
        this.conv = action.getConv();
        ec = new EventContext();
        ec.setUnit(currentUnit);
    }

    public static Event create(DataSource source, String eventString)
    {
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
                    propertyUnit = NmeaProperties.getType(property);
                    break;
                default:
                    throw new IllegalArgumentException(eventString+" illegal");
            }
        }
        catch (Exception ex)
        {
            System.err.println(eventString);
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
        source.getService().addNMEAObserver(source, property);
    }
    
    public void unregister()
    {
        source.getService().removeNMEAObserver(source, property);
    }
    
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
        json.append("\":\"");
        format.format(json, ec);
        json.append("\"}");
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
            source.getStatsService().addObserver(property+':'+seconds, this, UnitType.Degree.equals(propertyUnit));
        }

        @Override
        public void unregister()
        {
            source.getStatsService().removeObserver(property+':'+seconds, this);
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

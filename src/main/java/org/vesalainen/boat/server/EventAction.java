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
import static org.vesalainen.boat.server.Constants.*;
import org.vesalainen.math.UnitType;
import org.vesalainen.math.sliding.TimeoutStats;
import org.vesalainen.navi.CoordinateFormat;
import org.vesalainen.util.FloatMap;
import org.vesalainen.util.ThreadLocalFormatter;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public enum EventAction
{

    Default("text", "%.1f%s", EventAction::same),
    Rotate("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.1f)", c.getValue()), EventAction::same),
    InvRotate("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.1f)", c.getValue()), (double v, FloatMap m)->{return 360-v;}),
    BoatRelativeRotate("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.1f)", c.getValue()), (double v, FloatMap m)->{return (m.getFloat("trueHeading")+v) % 360;}),
    Latitude("text", (Appendable o, EventContext c)->CoordinateFormat.formatLatitude(o, I18n.getLocale(), c.getValue(), c.getUnit()), EventAction::same),
    Longitude("text", (Appendable o, EventContext c)->CoordinateFormat.formatLatitude(o, I18n.getLocale(), c.getValue(), c.getUnit()), EventAction::same),
    CompassPitch("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "scale(1,%.3f)", c.getValue()), (double v, FloatMap m)->{ return Math.cos(Math.toRadians(90-ViewAngle-v));}),
    Route1("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.0f)", c.getValue()), EventAction::same),
    Route2("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "translate(%.3f,0)", c.getValue()), (double v, FloatMap m)->{return 1.0/Math.pow(A, Math.abs(v));}),
    Route3("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "translate(%.3f,0)", c.getValue()), (double v, FloatMap m)->{return Math.signum(v)*(30-1.0/Math.pow(A, Math.abs(v))*30);}),
    ViewBox("viewBox", (Appendable o, EventContext c)->{
        TimeoutStats s = c.getStats();
        ThreadLocalFormatter.format(o, Locale.US, "%d %.1f %d %.1f", 
                s.firstTime(),
                -s.getMin(),
                s.lastTime()-s.firstTime(),
                s.getMax()-s.getMin()
                );
    }, EventAction::same),
    Visible("refresh", "", EventAction::same, EventAction::same)
;

    private final String action;
    private final EventFormat format;
    private final EventFunction func;
    private final EventConversion conv;

    private EventAction(String action, String format, EventFunction func)
    {
        this(
            action, 
            (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, I18n.getLocale(), format, c.getValue(), c.getUnit().getUnit()),
            func);
    }

    private EventAction(String action, EventFormat format, EventFunction func)
    {
        this(action, format, func, EventAction::conv);
    }
    
    private EventAction(String action, String format, EventFunction func, EventConversion conv)
    {
        this(
            action, 
            (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, I18n.getLocale(), format, c.getValue(), c.getUnit().getUnit()),
            func, 
            conv);
    }
    
    private EventAction(String action, EventFormat format, EventFunction func, EventConversion conv)
    {
        this.action = action;
        this.format = format;
        this.func = func;
        this.conv = conv;
    }

    public String getAction()
    {
        return action;
    }

    public EventFormat getFormat()
    {
        return format;
    }

    public EventFunction getFunc()
    {
        return func;
    }

    public EventConversion getConv()
    {
        return conv;
    }

    private static double same(double value, FloatMap map)
    {
        return value;
    }
    private static double same(UnitType from, UnitType to, double value)
    {
        return value;
    }
    private static double conv(UnitType from, UnitType to, double value)
    {
        if (from != null)
        {
            return from.convertTo(value, to);
        }
        else
        {
            return value;
        }
    }
}

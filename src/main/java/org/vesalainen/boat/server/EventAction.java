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
import java.util.Locale;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfDouble;
import java.util.PrimitiveIterator.OfLong;
import static org.vesalainen.boat.server.Constants.*;
import org.vesalainen.math.UnitType;
import org.vesalainen.math.sliding.TimeoutStats;
import org.vesalainen.navi.CoordinateFormat;
import org.vesalainen.svg.SVGPath;
import org.vesalainen.util.DoubleMap;
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
    InvRotate("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.1f)", c.getValue()), (double v, DoubleMap m)->{return 360-v;}),
    BoatRelativeRotate("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.1f)", c.getValue()), (double v, DoubleMap m)->{return (m.getDouble("trueHeading")+v) % 360;}),
    Latitude("text", (Appendable o, EventContext c)->CoordinateFormat.formatLatitude(o, I18n.getLocale(), c.getValue(), c.getUnit()), EventAction::same),
    Longitude("text", (Appendable o, EventContext c)->CoordinateFormat.formatLatitude(o, I18n.getLocale(), c.getValue(), c.getUnit()), EventAction::same),
    CompassPitch("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "scale(1,%.3f)", c.getValue()), (double v, DoubleMap m)->{ return Math.cos(Math.toRadians(90-ViewAngle-v));}),
    Route1("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "rotate(%.0f)", c.getValue()), EventAction::same),
    Route2("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "translate(%.3f,0)", c.getValue()), (double v, DoubleMap m)->{return 1.0/Math.pow(A, Math.abs(v));}),
    Route3("transform", (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, Locale.US, "translate(%.3f,0)", c.getValue()), (double v, DoubleMap m)->{return Math.signum(v)*(30-1.0/Math.pow(A, Math.abs(v))*30);}),
    Graph("append", (Appendable o, EventContext c)->{
        TimeoutStats s = c.getStats();
        if (s.count() > 2)
        {
            SVGPath path = new SVGPath();
            long maxDuration = s.maxDuration()/1000;
            path.setAttr("ttl", maxDuration);
            double strokeWidth = (c.getMax()-c.getMin())/200;
            path.setAttr("stroke-width", strokeWidth);
            path.addElement("animateTransform")
                    .setAttr("attributeName", "transform")
                    .setAttr("type", "translate")
                    .setAttr("from", 0)
                    .setAttr("to", -maxDuration)
                    .setAttr("fill", "freeze");
            long now = System.currentTimeMillis();
            if (c.isFirstFire())
            {
                OfLong ti = s.timeStream().iterator();
                OfDouble vi = s.stream().iterator();
                while (ti.hasNext() && vi.hasNext())
                {
                    long time = ti.nextLong();
                    double value = vi.nextDouble();
                    if (path.isEmpty())
                    {
                        path.moveTo(-(now-time)/1000, value);
                    }
                    else
                    {
                        path.lineTo(-(now-time)/1000, value);
                    }
                }
            }
            else
            {
                path.moveTo(-(now-s.previousTime())/1000, s.previous());
                path.lineTo(0, s.last());
            }
            try
            {
                o.append('"');
                StringBuilder sb = new StringBuilder();
                path.append(sb);
                o.append(sb.toString().replace('"', '\''));
                o.append('"');
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }, EventAction::same, EventAction::same, true),
    Visible("refresh", "", EventAction::same)
;

    private final String action;
    private final EventFormat format;
    private final EventFunction func;
    private final EventConversion conv;
    private final boolean isObject; // is the event data string or object

    private EventAction(String action, String format, EventFunction func)
    {
        this(
            action, 
            (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, I18n.getLocale(), format, c.getValue(), c.getUnit().getUnit()),
            func);
    }

    private EventAction(String action, EventFormat format, EventFunction func)
    {
        this(action, format, func, EventAction::conv, false);
    }
    
    private EventAction(String action, String format, EventFunction func, EventConversion conv, boolean isObject)
    {
        this(
            action, 
            (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, I18n.getLocale(), format, c.getValue(), c.getUnit().getUnit()),
            func, 
            conv,
            isObject);
    }
    
    private EventAction(String action, EventFormat format, EventFunction func, EventConversion conv, boolean isObject)
    {
        this.action = action;
        this.format = format;
        this.func = func;
        this.conv = conv;
        this.isObject = isObject;
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

    public boolean isObject()
    {
        return isObject;
    }

    private static double same(double value, DoubleMap map)
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

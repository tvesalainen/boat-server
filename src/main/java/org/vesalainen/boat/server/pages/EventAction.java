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
package org.vesalainen.boat.server.pages;

import java.util.Locale;
import static org.vesalainen.boat.server.Constants.*;
import org.vesalainen.boat.server.EventContext;
import org.vesalainen.boat.server.EventFormat;
import org.vesalainen.boat.server.EventFunction;
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

;

    private final String action;
    private final EventFormat format;
    private final EventFunction func;

    private EventAction(String action, EventFormat format, EventFunction func)
    {
        this.action = action;
        this.format = format;
        this.func = func;
    }

    private EventAction(String action, String format, EventFunction func)
    {
        this.action = action;
        this.format = (Appendable o, EventContext c)->ThreadLocalFormatter.format(o, I18n.getLocale(), format, c.getValue(), c.getUnit().getUnit());
        this.func = func;
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

    private static double same(double value, FloatMap map)
    {
        return value;
    }
}

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

import java.util.function.DoubleUnaryOperator;
import org.vesalainen.boat.server.BiFormat;
import org.vesalainen.math.UnitType;
import org.vesalainen.navi.CoordinateFormat;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public enum EventAction
{

    DEFAULT("text", "%.1f%s", EventAction::same),
    ROTATE("transform", "rotate(%.1f)", EventAction::same),
    LATITUDE("text", (double v, UnitType u)->CoordinateFormat.formatLatitude(v, I18n.getLocale(), u), EventAction::same),
    LONGITUDE("text", (double v, UnitType u)->CoordinateFormat.formatLongitude(v, I18n.getLocale(), u), EventAction::same)
;

    private final String action;
    private final BiFormat format;
    private final DoubleUnaryOperator func;

    private EventAction(String action, BiFormat format, DoubleUnaryOperator func)
    {
        this.action = action;
        this.format = format;
        this.func = func;
    }

    private EventAction(String action, String format, DoubleUnaryOperator func)
    {
        this.action = action;
        this.format = (double v, UnitType u)->String.format(I18n.getLocale(), format, v, u.getUnit());
        this.func = func;
    }

    public String getAction()
    {
        return action;
    }

    public BiFormat getFormat()
    {
        return format;
    }

    public DoubleUnaryOperator getFunc()
    {
        return func;
    }

    private static double same(double value)
    {
        return value;
    }
}

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

import org.json.JSONObject;
import org.vesalainen.boat.server.pages.Transform;

/**
 *
 * @author tkv
 */
public class CompassRotateEvent extends RotateEvent
{
    public CompassRotateEvent(DataSource source, String eventString, String property, Transform transform)
    {
        this(source, eventString, property, transform, "rotate(%.1f)");
    }

    public CompassRotateEvent(DataSource source, String eventString, String property, Transform transform, String format)
    {
        super(source, eventString, property, transform, format);
    }

    @Override
    protected void populate(JSONObject jo, String property, double value)
    {
        super.populate(jo, property, 360-value);
    }

}

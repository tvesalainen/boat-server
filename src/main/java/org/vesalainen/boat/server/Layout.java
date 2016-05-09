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

import org.vesalainen.boat.server.pages.BaseContainer;
import org.vesalainen.boat.server.pages.CompassContainer;
import org.vesalainen.boat.server.pages.LocationContainer;
import org.vesalainen.boat.server.pages.OneRowContainer;
import org.vesalainen.boat.server.pages.StatsContainer;
import org.vesalainen.boat.server.pages.TackticalContainer;

/**
 *
 * @author tkv
 */
public enum Layout
{
    Compass(CompassContainer.class, "trueHeading"),
    Tacktical(TackticalContainer.class, "trueHeading"),
    Stats(StatsContainer.class),
    OneRow(OneRowContainer.class),
    Location(LocationContainer.class, "latitude", "longitude")
    ;
    private Class<? extends BaseContainer> type;
    private String[] properties;
    private Layout(Class<? extends BaseContainer> type, String... properties)
    {
        this.type = type;
        this.properties = properties;
    }

    public Class<? extends BaseContainer> getType()
    {
        return type;
    }

    public String[] getProperties()
    {
        return properties;
    }
    
}

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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.vesalainen.boat.server.pages.BaseContainer;
import org.vesalainen.boat.server.pages.CompassContainer;
import org.vesalainen.boat.server.pages.LocationContainer;
import org.vesalainen.boat.server.pages.OneRowContainer;
import org.vesalainen.boat.server.pages.StatsContainer;
import org.vesalainen.boat.server.pages.TackticalContainer;
import org.vesalainen.util.Lists;

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
    private List<String> required;
    private Layout(Class<? extends BaseContainer> type, String... required)
    {
        this.type = type;
        if (required.length > 0)
        {
            this.required = Collections.unmodifiableList(Lists.create(required));
        }
        else
        {
            this.required = Collections.EMPTY_LIST;
        }
    }

    public Class<? extends BaseContainer> getType()
    {
        return type;
    }
    /**
     * Returns required properties. First property tells the unit and unit-category.
     * @return 
     */
    public List<String> getRequired()
    {
        return required;
    }

}

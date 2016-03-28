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

import org.vesalainen.boat.server.Id;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class LocationContainer extends TitleContainer
{

    public LocationContainer(JQueryMobileDocument document)
    {
        super(document);
    }

    @Override
    protected void addRows(Element svg)
    {
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "0")
                .setAttr("textLength", "90")
                .setAttr("lengthAdjust", "spacingAndGlyphs")
                .setAttr(AbstractSSESource.EventSink, new DynString("Latitude-", wrap(Id.SelectedUnit), "-Latitude"))
                .setAttr("style", "font-size: 2em");
        
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "26")
                .setAttr("textLength", "90")
                .setAttr("lengthAdjust", "spacingAndGlyphs")
                .setAttr(AbstractSSESource.EventSink, new DynString("Longitude-", wrap(Id.SelectedUnit), "-Longitude"))
                .setAttr("style", "font-size: 2em");
    }
    
}

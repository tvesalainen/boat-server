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

import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class CompassContainer extends BaseContainer
{

    public CompassContainer(JQueryMobileDocument document)
    {
        super(document);
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        svg.addElement(new Arc(50, 0, 0, 120, 240, 1, 3, 0.3, "black"));
        svg.addElement(new Arc(50, 0, 0, 120, 240, 5, 5, 0.6, "black"));
        svg.addElement(new Arc(50, 0, 0, 120, 240, 10, 7, 0.9, "black"));

        svg.addElement("path")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr("stroke", "green")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 50 l 0 -10")
                .setAttr(AbstractSSESource.EventSink, "Roll-DEGREE-COMPASS-Port");
        
        svg.addElement("path")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr("stroke", "red")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 50 l 0 -10")
                .setAttr(AbstractSSESource.EventSink, "Roll-DEGREE-COMPASS-Starboard");
        
        Element view = svg.addElement("g")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "Roll-DEGREE-COMPASS");
        view.addElement("path")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 0 l 0 50");
        
        Element plane = view.addElement("g")
                .setAttr("transform", "scale(1,0.5)")
                .setAttr(AbstractSSESource.EventSink, "Pitch-DEGREE-COMPASS");
        Element compass = plane.addElement("g")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "TrueHeading-DEGREE-COMPASS");

        compass.addElement(new CompassRing(0.6, 43));
        
        view.addElement("path")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 0 l 0 -50");
        
    }
    
}
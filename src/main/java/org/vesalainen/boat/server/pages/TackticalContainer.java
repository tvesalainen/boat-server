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
public class TackticalContainer extends BaseContainer
{

    public TackticalContainer(JQueryMobileDocument document)
    {
        super(document);
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        Element defs = svg.addElement("defs");
        defs.addElement("marker")
                .setAttr("id", "triangle")
                .setAttr("viewBox", "0 0 10 10")
                .setAttr("refX", "0")
                .setAttr("refY", "5")
                .setAttr("markerUnits", "strokeWidth")
                .setAttr("markerWidth", "4")
                .setAttr("markerHeight", "3")
                .setAttr("orient", "auto")
                .setAttr("d", "M 0 0 L 10 5 L 0 10 z");

        svg.addElement(new CompassRing(0.5, 45));
        
        Element route1 = svg.addElement("g")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "BearingToDestination-DEGREE-Route1");
        Element route2 = route1.addElement("g")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "CrossTrackError-NM-Route2");
        Element route3 = route2.addElement("g")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "CrossTrackError-NM-Route3");
        route3.addElement("path")
                .setAttr("id", "centerLine")
                .setAttr("stroke", "green")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 50 l 0 -100");
        route3.addElement("path")
                .setAttr("id", "leftLine")
                .setAttr("stroke", "green")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M -30 50 l 0 -100");
        route3.addElement("path")
                .setAttr("id", "rightLine")
                .setAttr("stroke", "green")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 30 50 l 0 -100");
        
        svg.addElement("path")
                .setAttr("id", "boat")
                .setAttr("style", "display: none;")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "TrueHeading-DEGREE-Rotate")
                .setAttr("d", 
                        "M -20 40 l 40 0 "+
                        "C 23 10 20 -15 0 -40"+
                        "M -20 40 "+
                        "C -23 10 -20 -15 0 -40"
                );

        Element relativeWindAngle = svg.addElement("g")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "RelativeWindAngle-DEGREE-BoatRelativeRotate");
        relativeWindAngle.addElement("path")
                .setAttr("id", "windIndicatorTip")
                .setAttr("stroke", "red")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "red")
                .setAttr("d", "M -3 -15 L 0 -25 L 3 -15 Z");
        relativeWindAngle.addElement("path")
                .setAttr("id", "windIndicatorShaft")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 15 l 0 -30");
        relativeWindAngle.addElement("path")
                .setAttr("id", "windIndicatorTail")
                .setAttr("stroke", "red")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "red")
                .setAttr("d", "M -3 25 L -3 20 L 0 15 L 3 20 L 3 25 Z");

        Element trackMadeGood = svg.addElement("g")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "TrackMadeGood-DEGREE-Rotate-300-Ave");
        trackMadeGood.addElement("path")
                .setAttr("id", "trackMadeGood")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "2")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 0 l 0 -40")
                .setAttr("marker-end", "url(#triangle)");   // TODO
        
    }
    
}

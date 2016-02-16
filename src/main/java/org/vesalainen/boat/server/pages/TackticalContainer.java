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
        svg.addElement("path")
                .setAttr("id", "boat")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "TrueHeading-DEGREE-ROTATE")
                .setAttr("d", 
                        "M -20 40 l 40 0 "+
                        "C 23 10 20 -15 0 -40"+
                        "M -20 40 "+
                        "C -23 10 -20 -15 0 -40"
                );

        Element relativeWindAngle = svg.addElement("g")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "RelativeWindAngle-DEGREE-ROTATE");
        relativeWindAngle.addElement("path")
                .setAttr("id", "windIndicatorTip")
                .setAttr("stroke", "red")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "red")
                .setAttr("d", "M -3 -20 L 0 -30 L 3 -20 Z");
        relativeWindAngle.addElement("path")
                .setAttr("id", "windIndicatorShaft")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 20 l 0 -40");
        relativeWindAngle.addElement("path")
                .setAttr("id", "windIndicatorTail")
                .setAttr("stroke", "red")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "red")
                .setAttr("d", "M -3 30 L -3 25 L 0 20 L 3 25 L 3 30 Z");

        Element trackMadeGood = svg.addElement("g")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "TrackMadeGood-DEGREE-ROTATE");
        trackMadeGood.addElement("path")
                .setAttr("id", "windIndicatorShaft")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", "2")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 00 l 0 -100");
    }
    
}

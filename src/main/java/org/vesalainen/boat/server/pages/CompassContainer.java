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

import org.vesalainen.boat.server.Model;
import org.vesalainen.html.Element;
import org.vesalainen.web.servlet.AbstractSSESource;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class CompassContainer extends BaseContainer implements HasSeconds
{

    public CompassContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData);
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        //svg.addElement(new Arc(50, 0, 0, 120, 240, 1, 3, 0.3, "black"));
        //svg.addElement(new Arc(50, 0, 0, 120, 240, 5, 5, 0.6, "black"));
        //svg.addElement(new Arc(50, 0, 0, 120, 240, 10, 7, 0.9, "black"));
        Element g1 = svg.addElement("g");
        g1.setAttr("clip-rule", "nonzero");
        Element clipPath = g1.addElement("clipPath");
        clipPath.setAttr("id", "leanScale");
        double rad = Math.toRadians(60);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double x = 100*sin;
        double y = 100*cos;
        Element path = clipPath.addElement("path");
        path.setAttr("d", "M0,0L-"+x+","+y+"L"+x+","+y+"z");
        Element g2 = g1.addElement("g");
        g2.setAttr("clip-path", "url(#leanScale)");
        g2.addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale-1")
                .setAttr("transform", "scale(50)")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", 0.3/50);

        g2.addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale-5")
                .setAttr("transform", "scale(50)")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", 0.6/50);

        g2.addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale-10")
                .setAttr("transform", "scale(50)")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", 1.0/50);

        svg.addElement("path")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr("stroke", "green")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 50 l 0 -10")
                .setAttr(AbstractSSESource.EventSink, "Roll-Degree-InvRotate-${seconds}-Min");
        
        svg.addElement("path")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr("stroke", "red")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 50 l 0 -10")
                .setAttr(AbstractSSESource.EventSink, "Roll-Degree-InvRotate-${seconds}-Max");
        
        Element view = svg.addElement("g")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "Roll-Degree-InvRotate-${seconds}-Last");
        
        view.addElement("path")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 0 l 0 50");
        
        Element plane = view.addElement("g")
                .setAttr("transform", "scale(1,0.5)")
                .setAttr(AbstractSSESource.EventSink, "Pitch-Degree-CompassPitch");
        Element compass = plane.addElement("g")
                .setAttr("style", "display: none;")
                .setAttr("transform", "rotate(0)")
                .setAttr(AbstractSSESource.EventSink, "TrueHeading-Degree-InvRotate");

        compass.addContent(new CompassRing(0.6, 43));
        
        view.addElement("path")
                .setAttr("stroke", "blue")
                .setAttr("stroke-width", "1")
                .setAttr("fill", "none")
                .setAttr("d", "M 0 0 l 0 -50");
        
    }

}

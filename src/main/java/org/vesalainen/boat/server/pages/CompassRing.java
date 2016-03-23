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

/**
 *
 * @author tkv
 */
public class CompassRing extends Element
{

    public CompassRing(double em, double radius)
    {
        this(em, radius, 0, 0);
    }

    public CompassRing(double em, double r, double cx, double cy)
    {
        super("g");
        //addElement(new Arc(r, cx, cy, 1, 5, 0.3, "black"));
        //addElement(new Arc(r, cx, cy, 5, 7, 0.6, "black"));
        //addElement(new Arc(r, cx, cy, 10, 10, 1, "black"));

        addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale-1")
                .setAttr("transform", "scale("+r+")")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", 0.3/r);

        addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale-5")
                .setAttr("transform", "scale("+r+")")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", 0.6/r);

        addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale-10")
                .setAttr("transform", "scale("+r+")")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", 1/r);

        //addElement(new Scale(em, r, cx, cy));

        addElement("use")
                .setAttr("xlink:href", "/defs.svg#compass-scale")
                .setAttr("transform", "scale("+r+")")
                .setAttr("font-size", em/50 + "em");
    }

}

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
import org.vesalainen.util.Lists;

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
        addElement(new Arc(r, cx, cy, 1, 5, 0.3, "black"));
        addElement(new Arc(r, cx, cy, 5, 7, 0.6, "black"));
        addElement(new Arc(r, cx, cy, 10, 10, 1, "black"));
        addElement(new Scale(em, r, cx, cy));
    }

    private static class Scale extends Element
    {

        private Scale(double em, double r, double cx, double cy)
        {
            super("g");
            for (int a=0;a<360;a+=30)
            {
                Element text = addElement("text")
                        .setAttr("font-size", em+"em")
                        .setAttr("text-anchor", "middle")
                        .setAttr("transform", "rotate("+a+")");
                String l = String.valueOf(a);
                text.addText(l);
                String angles = null;
                String a1 = Lists.print(" ", 0);
                String a2 = Lists.print(" ", 357, 3);
                String a3 = Lists.print(" ", 355, 0, 5);
                String x = null;
                String x1 = Lists.print(" ", cx + Math.sin(Math.toRadians(0))*r);
                String x2 = Lists.print(" ", cx + Math.sin(Math.toRadians(357))*r, cx + Math.sin(Math.toRadians(3))*r);
                String x3 = Lists.print(" ", cx + Math.sin(Math.toRadians(355))*r, cx + Math.sin(Math.toRadians(0))*r, cx + Math.sin(Math.toRadians(5))*r);
                String y = null;
                String y1 = Lists.print(" ", cy - Math.cos(Math.toRadians(0))*r);
                String y2 = Lists.print(" ", cy - Math.cos(Math.toRadians(357))*r, cy - Math.cos(Math.toRadians(3))*r);
                String y3 = Lists.print(" ", cy - Math.cos(Math.toRadians(355))*r, cy - Math.cos(Math.toRadians(0))*r, cy - Math.cos(Math.toRadians(5))*r);
                switch (l.length())
                {
                    case 1:
                        y = y1;
                        x = x1;
                        angles = a1;
                        break;
                    case 2:
                        y = y2;
                        x = x2;
                        angles = a2;
                        break;
                    case 3:
                        y = y3;
                        x = x3;
                        angles = a3;
                        break;
                }
                text.setAttr("x", x);
                text.setAttr("y", y);
                text.setAttr("rotate", angles);
            }
        }

    }
}

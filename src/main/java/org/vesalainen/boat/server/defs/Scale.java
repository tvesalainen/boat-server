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
package org.vesalainen.boat.server.defs;

import org.vesalainen.html.Element;

/**
 *
 * @author tkv
 */
public class Scale extends Element
{

    public Scale(int start, int end, int step, boolean vertical)
    {
        super("g");
        Element text = addElement("text").setAttr("text-anchor", "middle");
        
        for (int a = start; a <= end; a+=step)
        {
            Element tspan = text.addElement("tspan");
            String l = String.valueOf(-a);
            tspan.addText(l);
            if (vertical)
            {
                tspan.setAttr("y", a);
            }
            else
            {
                tspan.setAttr("x", a);
            }
        }
    }
    
}

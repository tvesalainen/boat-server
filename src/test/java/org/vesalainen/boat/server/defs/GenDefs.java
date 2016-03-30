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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.vesalainen.html.Element;
import org.vesalainen.svg.SVGDocument;

/**
 *
 * @author tkv
 */
public class GenDefs
{
    
    public GenDefs()
    {
    }

    @Test
    public void generate()
    {
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/defs.svg"))
        {
            SVGDocument svg = new SVGDocument();
            Element defs = svg.addElement("defs");
            double r = 1;
            double cx = 0;
            double cy = 0;
            Element cs1 = new Arc(r, cx, cy, 1, 0.1);
            cs1.setAttr("id", "compass-scale-1");
            defs.addElement(cs1);
            Element cs5 = new Arc(r, cx, cy, 5, 0.15);
            cs5.setAttr("id", "compass-scale-5");
            defs.addElement(cs5);
            Element cs10 = new Arc(r, cx, cy, 10, 0.2);
            cs10.setAttr("id", "compass-scale-10");
            defs.addElement(cs10);
            Element cs = new CircleScale();
            cs.setAttr("id", "compass-scale");
            svg.addElement(cs);
            // stats
            int minx = -100;
            int miny = -1000;
            int maxx = 100;
            int maxy = 0;
            Scale vScale = new Scale(minx, maxx, 1, true);
            vScale.setAttr("id", "vertical-scale");
            svg.addElement(vScale);
            Grid vGrid1 = new Grid(miny, maxy, minx, maxx, 1, false);
            vGrid1.setAttr("id", "vertical-grid-1");
            svg.addElement(vGrid1);
            Grid vGrid5 = new Grid(miny, maxy, minx, maxx, 5, false);
            vGrid5.setAttr("id", "vertical-grid-5");
            svg.addElement(vGrid5);
            Grid vGrid10 = new Grid(miny, maxy, minx, maxx, 10, false);
            vGrid10.setAttr("id", "vertical-grid-10");
            svg.addElement(vGrid10);
            Grid hGrid1 = new Grid(minx, maxx, miny, maxy, 1, true);
            hGrid1.setAttr("id", "horizontal-grid-1");
            svg.addElement(hGrid1);
            Grid hGrid5 = new Grid(minx, maxx, miny, maxy, 5, true);
            hGrid5.setAttr("id", "horizontal-grid-5");
            svg.addElement(hGrid5);
            Grid hGrid10 = new Grid(minx, maxx, miny, maxy, 10, true);
            hGrid10.setAttr("id", "horizontal-grid-10");
            svg.addElement(hGrid10);
            svg.write(fos);
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(GenDefs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

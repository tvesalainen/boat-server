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

import org.vesalainen.boat.server.defs.Arc;
import org.vesalainen.boat.server.defs.Scale;
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
            Element cs1 = new Arc(r, cx, cy, 1, 0.1, 0.3, "black");
            cs1.setAttr("id", "compass-scale-1");
            defs.addElement(cs1);
            Element cs5 = new Arc(r, cx, cy, 5, 0.15, 0.6, "black");
            cs5.setAttr("id", "compass-scale-5");
            defs.addElement(cs5);
            Element cs10 = new Arc(r, cx, cy, 10, 0.2, 1, "black");
            cs10.setAttr("id", "compass-scale-10");
            defs.addElement(cs10);
            Element cs = new Scale();
            cs.setAttr("id", "compass-scale");
            svg.addElement(cs);
            svg.write(fos);
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(GenDefs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

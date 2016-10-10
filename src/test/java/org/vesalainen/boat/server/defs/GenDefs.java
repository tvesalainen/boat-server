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

import org.vesalainen.svg.CircleScale;
import org.vesalainen.svg.Arc;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.vesalainen.html.Element;
import org.vesalainen.html.PrettyPrinter;
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
        try (FileWriter fw = new FileWriter("src/main/resources/org/vesalainen/web/jar/defs.svg"))
        {
            SVGDocument svg = new SVGDocument();
            Element defs = svg.addElement("defs");
            double r = 1;
            double cx = 0;
            double cy = 0;
            Element cs1 = new Arc(r, cx, cy, 1, 0.1);
            cs1.setAttr("id", "compass-scale-1");
            defs.addContent(cs1);
            Element cs5 = new Arc(r, cx, cy, 5, 0.15);
            cs5.setAttr("id", "compass-scale-5");
            defs.addContent(cs5);
            Element cs10 = new Arc(r, cx, cy, 10, 0.2);
            cs10.setAttr("id", "compass-scale-10");
            defs.addContent(cs10);
            Element cs = new CircleScale();
            cs.setAttr("id", "compass-scale");
            defs.addContent(cs);
            PrettyPrinter pp = new PrettyPrinter(fw);
            svg.append(pp);
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(GenDefs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

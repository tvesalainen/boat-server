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

import org.vesalainen.boat.server.Id;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public abstract class TitleContainer extends BaseContainer
{

    public TitleContainer(JQueryMobileDocument document)
    {
        super(document);
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "-35")
                .setAttr("textLength", "90")
                .setAttr("lengthAdjust", "spacingAndGlyphs")
                .addText(wrap(Id.MeterName))
                .setAttr("style", "font-size: 1em");
        addRows(svg);
        
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "45")
                .setAttr("textLength", "90")
                .setAttr("lengthAdjust", "spacingAndGlyphs")
                .addText(wrap(Id.MeterUnit))
                .setAttr("style", "font-size: 1em");
       
    }

    protected abstract void addRows(Element svg);
    
}

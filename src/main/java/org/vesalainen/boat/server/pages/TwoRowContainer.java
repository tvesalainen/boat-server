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
import org.vesalainen.boat.server.Model;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.servlet.AbstractSSESource;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class TwoRowContainer extends TitleContainer
{

    public TwoRowContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData);
    }

    @Override
    protected void addRows(Element svg)
    {
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "0")
                .setAttr("textLength", "90")
                .setAttr("lengthAdjust", "spacingAndGlyphs")
//                .setAttr(AbstractSSESource.EventSink, wrap(Id.Prop1))
                .setAttr("style", "font-size: 2em");
        
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "26")
                .setAttr("textLength", "90")
                .setAttr("lengthAdjust", "spacingAndGlyphs")
  //              .setAttr(AbstractSSESource.EventSink, wrap(Id.Prop2))
                .setAttr("style", "font-size: 2em");
    }

    @Override
    protected void addFormContent(Element form)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

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
import org.vesalainen.web.servlet.sse.AbstractSSESource;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class StatsContainer extends BaseContainer implements HasProperty, HasSeconds, HasUnit
{

    public StatsContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData, "0 0 0 0");
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        svg.removeAttr("viewBox");
        svg.setAttr(AbstractSSESource.EventSink, "${property}-${unit}-ViewBox-${seconds}-Last");
        Element g = svg.addElement("g").setAttr(AbstractSSESource.EventSink, "${property}-${unit}-Scale-${seconds}-Last");
        g.addElement("use")
            .setAttr("xlink:href", "/defs.svg#vertical-scale");
        g.addElement("use")
            .setAttr("xlink:href", "/defs.svg#vertical-grid-1")
            .setAttr("stroke", "black")
            .setAttr("stroke-width", 0.3/50);
        g.addElement("use")
            .setAttr("xlink:href", "/defs.svg#vertical-grid-5")
            .setAttr("stroke", "black")
            .setAttr("stroke-width", 0.3/50);
        g.addElement("use")
            .setAttr("xlink:href", "/defs.svg#vertical-grid-10")
            .setAttr("stroke", "black")
            .setAttr("stroke-width", 0.3/50);


    }

}

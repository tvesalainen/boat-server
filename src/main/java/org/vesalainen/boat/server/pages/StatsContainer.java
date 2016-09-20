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
import org.vesalainen.svg.SVGCoordinates;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.sse.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class StatsContainer extends BaseContainer implements HasProperty, HasSeconds, HasUnit, HasLimits
{
    public StatsContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData, "0 0 0 0");
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        svg.setAttr("viewBox", "-${seconds} -${max} ${seconds} ${range}");
        svg.setAttr("preserveAspectRatio", "none");
        SVGCoordinates coord = new SVGCoordinates((double)-getSeconds(), -getMax(), (double)getSeconds(), getRange());
        svg.addContent(coord);
        double strokeWidth = getRange()/200;
        Element g = svg.addElement("g")
                .setAttr(AbstractSSESource.EventSink, "${property}-${unit}-Graph-${seconds}-Last")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", strokeWidth)
                .setDataAttr("seconds", getSeconds())
                .setDataAttr("moving", true)
                .setAttr("transform", "translate(0)");
;
    }

}

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
import org.vesalainen.web.servlet.bean.Context;

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
        double strokeWidth = 0.1;
        double range = getRange();
        Element g = svg.addElement("g").setAttr("transform", "translate(-${seconds})");
        g.addElement("use")
            .setAttr("xlink:href", "/defs.svg#vertical-scale");
        if (range < 20)
        {
            g.addElement("use")
                .setAttr("xlink:href", "/defs.svg#vertical-grid-1")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", strokeWidth);
        }
        if (range < 100)
        {
            g.addElement("use")
                .setAttr("xlink:href", "/defs.svg#vertical-grid-5")
                .setAttr("stroke", "black")
                .setAttr("stroke-width", strokeWidth);
        }
        g.addElement("use")
            .setAttr("xlink:href", "/defs.svg#vertical-grid-10")
            .setAttr("stroke", "black")
            .setAttr("stroke-width", strokeWidth);


    }

}

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
public abstract class TitleContainer extends BaseContainer
{
    protected String title;
    
    public TitleContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData);
    }

    @Override
    protected void addSVGContent(Element svg)
    {
        svg.addElement("text")
                .setAttr("x", "-45")
                .setAttr("y", "-35")
                .addText("${title}")
                .setAttr("style", "font-size: 0.5em");
        addRows(svg);
        
    }

    protected abstract void addRows(Element svg);

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
    
    
}

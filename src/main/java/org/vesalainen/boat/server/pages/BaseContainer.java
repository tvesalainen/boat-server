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

import org.vesalainen.boat.server.DataSource;
import org.vesalainen.boat.server.Model;
import org.vesalainen.html.Element;
import org.vesalainen.math.UnitType;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public abstract class BaseContainer extends GridContainer
{
    protected Element meterPanel;
    protected String viewBox;
    protected String property;
    protected UnitType unit;
    protected int seconds;

    public BaseContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        this(threadLocalData, "-50,-50,100,100");
    }
    public BaseContainer(ThreadLocal<Context<Model>> threadLocalData, String viewBox)
    {
        super(threadLocalData);
        this.viewBox = viewBox;
    }

    @Override
    protected Element create()
    {
        Element div = super.create();
        
        meterPanel = meterDiv.addElement("div");
        Element svg = meterPanel.addElement("svg")
                .setAttr("viewBox", viewBox);

        addSVGContent(svg);
        
        return div;
    }
    
    protected abstract void addSVGContent(Element svg);

    public String getProperty()
    {
        return property;
    }

    public void setProperty(String property)
    {
        this.property = property;
    }

    public UnitType getUnit()
    {
        return unit;
    }

    public void setUnit(UnitType unit)
    {
        this.unit = unit;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }
  
    public double getMin()
    {
        return DataSource.getInstance().getMin(property);
    }
    
    public double getMax()
    {
        return DataSource.getInstance().getMax(property);
    }
    
}

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

import java.io.Serializable;
import java.util.Objects;
import org.vesalainen.boat.server.Model;
import org.vesalainen.html.Element;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public class GridContainer extends ThreadLocalBeanRenderer<Model,Element> implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Element meterDiv;

    public GridContainer(ThreadLocal<Context<Model>> threadLocalData)
    {
        super(threadLocalData);
    }

    @Override
    protected Element create()
    {
        Element div = new Element("div").setAttr("id", "${gridId}");
        meterDiv = div.addElement("div");
        
        Element header = meterDiv.addElement("span");
        header.addElement("a")
                .setAttr("href", "#${pageId}-popup")
                .setDataAttr("rel", "popup")
                .setDataAttr("grid-id", "${gridId}")
                .addClasses("set-grid", "ui-btn", "ui-icon-gear", "ui-btn-icon-left", "ui-btn-icon-notext")
                .addText(I18n.getLabel("change"));
        
        return div;
    }

    public String getGridId()
    {
        return getWebPattern();
    }

    public String getPageId()
    {
        String gridId = getWebPattern();
        int idx = gridId.indexOf("-grid");
        return gridId.substring(0, idx);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.meterDiv);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final GridContainer other = (GridContainer) obj;
        if (!Objects.equals(this.meterDiv, other.meterDiv))
        {
            return false;
        }
        return true;
    }
}

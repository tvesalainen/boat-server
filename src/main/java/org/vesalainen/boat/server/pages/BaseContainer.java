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
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalBeanRenderer;

/**
 *
 * @author tkv
 */
public abstract class BaseContainer extends ThreadLocalBeanRenderer<Model,Element>
{
    private Element meterPanel;
    private Element meterDiv;
    private String viewBox;

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
        Element div = new Element("div");
        meterDiv = div.addElement("div");
        
        Element header = meterDiv.addElement("span");
        header.addElement("a")
                .setAttr("href", "#${formId}")
                .addClasses("ui-btn", "ui-icon-gear", "ui-btn-icon-left", "ui-btn-icon-notext")
                .addText(I18n.getLabel("change"));
        
        meterPanel = meterDiv.addElement("div");
        Element svg = meterPanel.addElement("svg")
                .setAttr("viewBox", viewBox);

        addSVGContent(svg);
        
        Element formDiv = div.addElement("div").setDataAttr("role", "popup").setAttr("id", "${formId}").addClasses("ui-content");
        Element form = formDiv.addElement("form").setAttr("method", "post");
        addFormContent(form);
        form.addElement("input").setAttr("type", "submit");
        return div;
    }
    
    public String getFormId()
    {
        return getWebPattern()+"-form";
    }

    protected abstract void addSVGContent(Element svg);

    protected abstract void addFormContent(Element form);
    
}

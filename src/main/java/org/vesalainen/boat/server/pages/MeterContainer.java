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

import java.io.IOException;
import org.vesalainen.boat.server.GridContext;
import org.vesalainen.boat.server.Id;
import org.vesalainen.html.DataAttribute;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.EnumDynContent;
import org.vesalainen.html.EnumDynContentSupport;
import org.vesalainen.html.Placeholder;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class MeterContainer extends Element implements EnumDynContent<GridContext,Id>
{
    private final EnumDynContent<GridContext,Id> dynContent = new EnumDynContentSupport<>(Id.class);
    private final JQueryMobileDocument document;
    private final Element meterPanel;
    private final Element meterDiv;

    public MeterContainer(JQueryMobileDocument document)
    {
        super("div");
        this.document = document;
        meterDiv = addElement("div");
        meterDiv.addElement("a")
                .setAttr("href", new DynString("#", wrap(Id.UnitPage)))
                .addClasses("ui-btn")
                .addText(document.getLabel("changeUnit"));
        meterPanel = meterDiv.addElement("div")
                .setAttr("id", wrap(Id.Meter))
                .addClasses(AbstractSSESource.EventClass)
                .addText("Mittari tässä!");
        meterPanel.setAttr(new DataAttribute("sse-event", wrap(Id.Event)));
    }

    @Override
    public void append(GridContext param, Appendable out) throws IOException
    {
        append(out);
    }

    @Override
    public final Placeholder wrap(Id key)
    {
        return dynContent.wrap(key);
    }

    @Override
    public Placeholder<Object> wrap(Id key, Object comp)
    {
        return dynContent.wrap(key, comp);
    }

    @Override
    public void provision(GridContext param)
    {
        dynContent.provision(param);
    }
    
    @Override
    public void attach(Id key, Placeholder wrap)
    {
        dynContent.attach(key, wrap);
    }
    
}

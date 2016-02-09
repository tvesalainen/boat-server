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
import org.vesalainen.boat.server.MeterData;
import org.vesalainen.boat.server.UnitForm;
import org.vesalainen.html.DataAttribute;
import org.vesalainen.html.DynContent;
import org.vesalainen.html.DynString;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.http.Query;
import org.vesalainen.util.Wrap;
import org.vesalainen.web.servlet.AbstractSSESource;

/**
 *
 * @author tkv
 */
public class MeterContainer extends Element implements DynContent<GridContext>
{
    private final JQueryMobileDocument document;
    private final Query query = new Query();
    private final Wrap<String> eventId = new Wrap<>();
    private final Wrap<String> meterId = new Wrap<>();
    private final Wrap<String> panelId = new Wrap<>();
    private final Wrap<String> formId = new Wrap<>();
    private final Wrap<String> inputId = new Wrap<>();
    private final Element panel;
    private final Element meterPanel;
    private final UnitForm unitForm;
    private final Element meterDiv;

    public MeterContainer(JQueryMobileDocument document)
    {
        super("div");
        this.document = document;
        this.unitForm = new UnitForm(document, query, formId, inputId);
        panel = addElement("div")
                .setAttr("data-role", "panel")
                .setAttr("id", panelId);
        panel.addContent(unitForm);
        meterDiv = addElement("div");
        meterDiv.addElement("a")
                .setAttr("href", new DynString("#", panelId))
                .addClasses("ui-btn", "ui-btn-inline")
                .addText("muuta");
        meterPanel = addElement("div")
                .setAttr("id", meterId)
                .addClasses(AbstractSSESource.EventClass)
                .addText("Mittari tässä!");
        meterPanel.setAttr(new DataAttribute("sse-event", eventId));
    }

    @Override
    public void append(GridContext param, Appendable out) throws IOException
    {
        query.clear();
        int pageId = param.getPageId();
        int gridNo = param.getGridNo();
        MeterData meterData = param.getMeterData();
        query.add("pageId", String.valueOf(pageId));
        query.add("gridNo", String.valueOf(gridNo));
        formId.setValue("fp" + pageId + "g" + gridNo);
        inputId.setValue("ip" + pageId + "g" + gridNo);
        panelId.setValue("p" + pageId + "g" + gridNo);
        meterId.setValue("m" + pageId + "g" + gridNo);
        eventId.setValue(meterData.getType()+":"+meterData.getUnit());
        append(out);
    }
    
}

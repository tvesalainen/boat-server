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
import org.vesalainen.boat.server.Id;
import org.vesalainen.html.Element;
import org.vesalainen.html.Placeholder;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public class AddMeterContent extends Element
{
    private MeterChooser form;
    
    public AddMeterContent(JQueryMobileDocument document)
    {
        super("div");
        setDataAttr("role", "collapsible");
        addElement("h1")
                .addText(I18n.getLabel("setMeterType"));
//        form = new MeterChooser(document);
  //      addContent(form);
    }

}

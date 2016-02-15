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

import static org.vesalainen.boat.server.ContentServlet.Action;
import org.vesalainen.html.ClassAttribute;
import org.vesalainen.html.Element;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public class AddPage extends MeterPage
{

    public AddPage(JQueryMobileDocument document)
    {
        super(document, 0);
        setPageId(-1);
        Element header = getHeader();
        header.addElement("h1")
                .addText(I18n.getLabel("Add new meter page"));
        JQueryMobileForm form = addForm(Action);
        form.addInput("pageType");
        form.addInput("addPage",
                new SimpleAttribute("data-iconpos", "notext"),
                new ClassAttribute("ui-icon-action")
        );
        form.addRestAsHiddenInputs();
    }
    
}

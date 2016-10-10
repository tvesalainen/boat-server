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
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class Page11 extends MeterPage
{

    public Page11(ThreadLocal<Context<Model>> threadLocalModel)
    {
        super(threadLocalModel, 2);
    }

    @Override
    protected JQueryMobilePage create()
    {
        JQueryMobilePage page = createPage(threadLocalModel);
        Element main = page.getMain();
        Element div1 = main.addElement("div")
                .addClasses("height-50");
        div1.add(grid[0]);
        Element div2 = main.addElement("div")
                .addClasses("height-50");
        div2.add(grid[1]);
        return page;
    }
    
}

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
package org.vesalainen.boat.server;

import java.util.HashMap;
import java.util.Map;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.boat.server.pages.Page12;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;

/**
 *
 * @author tkv
 */
public class ContentDocument extends JQueryMobileDocument
{
    private final Map<PageType,MeterPage> pageMap = new HashMap<>();
    

    public ContentDocument(ThreadLocal threadLocalData)
    {
        super(threadLocalData);
    }
    
    protected MeterPage getMeterPage(PageType pageType)
    {
        MeterPage mp = pageMap.get(pageType);
        if (mp == null)
        {
            switch (pageType)
            {
                case Page12:
                    mp = new Page12(this);
                    break;
            }
            pageMap.put(pageType, mp);
        }
        return mp;
    }

}

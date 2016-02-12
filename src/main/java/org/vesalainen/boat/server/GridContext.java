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

import org.vesalainen.html.DynParam;
import org.vesalainen.html.Placeholder;
import org.vesalainen.http.Query;
import org.vesalainen.web.I18n;

/**
 *
 * @author tkv
 */
public class GridContext implements DynParam<Id>
{
    public int pageId;
    public int gridNo;
    public MeterData meterData;

    public GridContext(int pageId, int gridNo)
    {
        this.pageId = pageId;
        this.gridNo = gridNo;
    }
    
    public void populate(Query query)
    {
        query.clear();
        query.add("pageId", String.valueOf(pageId));
        query.add("gridNo", String.valueOf(gridNo));
    }
    
    public String pageId()
    {
        return String.valueOf(pageId);
    }
    
    public String gridId()
    {
        return pageId+"-"+gridNo;
    }

    @Override
    public void provision(Id key, Placeholder<Object> wrap)
    {
        switch (key)
        {
            case Page:
                wrap.setValue("page"+pageId);
                break;
            case Form:
                wrap.setValue("form"+pageId+"-"+gridNo);
                break;
            case Input:
                wrap.setValue("input"+pageId+"-"+gridNo);
                break;
            case Popup:
                wrap.setValue("popup"+pageId+"-"+gridNo);
                break;
            case Meter:
                wrap.setValue("meter"+pageId+"-"+gridNo);
                break;
            case Event:
                wrap.setValue(meterData.getType()+"-"+meterData.getUnit());
                break;
            case Query:
                Query query = new Query();
                query.add("pageId", String.valueOf(pageId));
                query.add("gridNo", String.valueOf(gridNo));
                wrap.setValue(query);
                break;
            case UnitPage:
                wrap.setValue("unit"+pageId+"-"+gridNo);
                break;
            case SelectedUnit:
                wrap.setValue(meterData.getUnit());
                break;
            case MeterName:
                wrap.setValue(I18n.getLabel(meterData.getType()));
                break;
            case MeterUnit:
                wrap.setValue(I18n.getLabel(meterData.getUnit()));
                break;
            default:
                throw new UnsupportedOperationException(key+" not supported");
        }
    }
}

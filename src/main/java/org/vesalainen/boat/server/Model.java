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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.vesalainen.boat.server.pages.AddPage;
import org.vesalainen.boat.server.pages.MeterPage;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class Model implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<MeterPage> pages = new ArrayList<>();
    private AddPage addPage;

    public Model(ThreadLocal<Context<Model>> threadLocalData)
    {
        addPage = new AddPage(threadLocalData);
    }

    public AddPage getAddPage()
    {
        return addPage;
    }

    public void setAddPage(AddPage addPage)
    {
        this.addPage = addPage;
    }

    public List<MeterPage> getPages()
    {
        return pages;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.pages);
        hash = 71 * hash + Objects.hashCode(this.addPage);
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
        final Model other = (Model) obj;
        if (!Objects.equals(this.pages, other.pages))
        {
            return false;
        }
        if (!Objects.equals(this.addPage, other.addPage))
        {
            return false;
        }
        return true;
    }
    
}

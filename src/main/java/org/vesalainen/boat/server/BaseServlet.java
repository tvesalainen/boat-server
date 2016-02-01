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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.vesalainen.html.jquery.mobile.JQueryMobileBeanServlet;

/**
 *
 * @author tkv
 */
public abstract class BaseServlet extends JQueryMobileBeanServlet<Context>
{
    protected ThreadLocal<HttpSession> localSession = new ThreadLocal<>();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        localSession.set(req.getSession(true));
        super.service(req, resp);
        localSession.set(null);
    }
    
    @Override
    protected Context createData()
    {
        HttpSession session = localSession.get();
        if (session != null)
        {
            Context ctx = (Context) session.getAttribute(Context.class.getName());
            if (ctx == null)
            {
                ctx = new Context();
                session.setAttribute(Context.class.getName(), ctx);
            }
            return ctx;
        }
        else
        {
            return new Context();
        }
    }

}

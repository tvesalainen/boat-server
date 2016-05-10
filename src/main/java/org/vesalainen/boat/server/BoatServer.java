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
import org.vesalainen.web.server.EmbeddedServer;

/**
 *
 * @author tkv
 */
public class BoatServer extends EmbeddedServer
{
    
    public BoatServer(int port) throws IOException
    {
        super(port);
        DataSource source = DataSource.getInstance();
        source.start();
        ContentServlet contentServlet = new ContentServlet(source);
        addServlet(contentServlet, ContentServlet.Action+"/*");
        DataServlet dataServlet = new DataServlet(source);
        addServlet(dataServlet, DataSource.Action+"/*");
    }

    public static void main(String... args)
    {
        try
        {
            BoatServer server = new BoatServer(8080);
            server.startAndWait();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

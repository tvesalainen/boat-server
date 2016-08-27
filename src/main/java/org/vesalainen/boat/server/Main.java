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

import java.util.Arrays;
import java.util.logging.Level;
import org.vesalainen.util.JAXBCommandLine;

/**
 *
 * @author tkv
 */
public class Main extends JAXBCommandLine
{
    
    public Main()
    {
        super("org.vesalainen.web.boat.server.jaxb", true);
    }
    
    public static void main(String... args)
    {
        Main cmdLine = new Main();
        cmdLine.command(args);
        cmdLine.attachStatic(Config.class);
        try
        {
            BoatServer server = new BoatServer(Config.getHttpPort());
            server.startAndWait();
        }
        catch (Exception ex)
        {
            cmdLine.log(Level.SEVERE, ex, "args: %s", Arrays.toString(args));
        }
    }
}

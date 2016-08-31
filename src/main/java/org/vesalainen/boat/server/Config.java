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

import java.io.File;
import org.vesalainen.util.AbstractProvisioner.Setting;

/**
 *
 * @author tkv
 */
public class Config
{
    private static int httpPort;
    private static File sessionStoreDirectory;
    private static int nmeaUDPPort;
    private static String nmeaMulticastAddress;
    private static File devConfigFile;
    private static long devMeterPeriod;
    private static long sseAsyncTimeout;

    public static long getSseAsyncTimeout()
    {
        return sseAsyncTimeout;
    }

    @Setting("sseAsyncTimeout")
    public static void setSseAsyncTimeout(long sseAsyncTimeout)
    {
        Config.sseAsyncTimeout = sseAsyncTimeout;
    }

    public static File getSessionStoreDirectory()
    {
        return sessionStoreDirectory;
    }

    @Setting("sessionStoreDirectory")
    public static void setSessionStoreDirectory(File sessionStoreDirectory)
    {
        Config.sessionStoreDirectory = sessionStoreDirectory;
    }

    public static long getDevMeterPeriod()
    {
        return devMeterPeriod;
    }

    @Setting("devMeterPeriod")
    public static void setDevMeterPeriod(long devMeterPeriod)
    {
        Config.devMeterPeriod = devMeterPeriod;
    }

    public static File getDevConfigFile()
    {
        return devConfigFile;
    }

    @Setting("devConfigFile")
    public static void setDevConfigFile(File devConfigFile)
    {
        Config.devConfigFile = devConfigFile;
    }

    public static String getNmeaMulticastAddress()
    {
        return nmeaMulticastAddress;
    }

    @Setting("nmeaMulticastAddress")
    public static void setNmeaMulticastAddress(String nmeaMulticastAddress)
    {
        Config.nmeaMulticastAddress = nmeaMulticastAddress;
    }

    public static int getNmeaUDPPort()
    {
        return nmeaUDPPort;
    }

    @Setting("nmeaUDPPort")
    public static void setNmeaUDPPort(int nmeaUDPPort)
    {
        Config.nmeaUDPPort = nmeaUDPPort;
    }

    public static int getHttpPort()
    {
        return httpPort;
    }

    @Setting("httpPort")
    public static void setHttpPort(int httpPort)
    {
        Config.httpPort = httpPort;
    }
    
}

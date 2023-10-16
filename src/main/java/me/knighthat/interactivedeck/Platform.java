/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck;

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

public class Platform {

    public static PlatformType TYPE;

    private static void printSystemConfig() {
        String jre = vmName() + " " + vmVersion();
        String system = osArch() + " " + osName() + " " + osVersion();

        Log.info( "Java runtime version: " + jre );
        Log.info( "Running on: " + system );
    }

    public static void init() {
        if (osName().startsWith( "Linux" ))
            TYPE = PlatformType.LINUX;
        else if (osName().startsWith( "Mac" ))
            TYPE = PlatformType.MACOS;
        else if (osName().startsWith( "Windows" ))
            TYPE = PlatformType.WINDOWS;
        else
            TYPE = PlatformType.OTHER;

        printSystemConfig();
    }

    public static @NotNull String homeDir() {return System.getProperty( "user.home" ) + "/";}

    public static @NotNull String osName() {return System.getProperty( "os.name" );}

    public static @NotNull String osVersion() {return System.getProperty( "os.version" );}

    public static @NotNull String osArch() {return System.getProperty( "os.arch" );}

    public static @NotNull String vmName() {return System.getProperty( "java.vm.name" );}

    public static @NotNull String vmVersion() {return System.getProperty( "java.vm.version" );}


    enum PlatformType {
        WINDOWS, LINUX, MACOS, OTHER
    }
}

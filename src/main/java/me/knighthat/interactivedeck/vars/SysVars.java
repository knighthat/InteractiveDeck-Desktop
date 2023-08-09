/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.vars;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SysVars {

    public static final @NotNull String PLATFORM;
    public static final @NotNull File WORK_DIR;
    public static final @NotNull String JRE;

    static {
        // Platform
        String osName = System.getProperty("os.name");
        String osVer = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        PLATFORM = String.format("%s %s %s", osArch, osName, osVer);

        // Working directory
        String defaultHome = System.getProperty("user.home").concat("/");
        String path = defaultHome.concat(".config/InteractiveDeck");

        if (osName.startsWith("Windows")) {
            path = defaultHome.concat("InteractiveDeck");
        } else if (osName.startsWith("Mac")) {
            path = defaultHome.concat(".InteractiveDeck");
        }
        WORK_DIR = new File(path);

        // JRE information
        String vmName = System.getProperty("java.vm.name");
        String vmVer = System.getProperty("java.vm.version");

        JRE = String.format("%s %s", vmName, vmVer);
    }
}

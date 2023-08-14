/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck;

import me.knighthat.interactivedeck.console.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class WorkingDirectory {

    private static @NotNull File WORK_DIR;

    public static void init() {
        String osName = System.getProperty("os.name");
        String defaultHome = System.getProperty("user.home").concat("/");
        String path = defaultHome.concat(".config/InteractiveDeck");

        if (osName.startsWith("Windows")) {
            path = defaultHome.concat("InteractiveDeck");
        } else if (osName.startsWith("Mac")) {
            path = defaultHome.concat(".InteractiveDeck");
        }
        WORK_DIR = new File(path);

        if (WORK_DIR.exists()) return;

        Log.info("Working folder is not exist! Making one..");
        if (!WORK_DIR.mkdirs())
            Log.err("Couldn't create working directory at " + WORK_DIR.getAbsolutePath());
    }

    public static @NotNull File get() {
        return WORK_DIR;
    }

    public static @NotNull String absPath() {
        return WORK_DIR.getAbsolutePath();
    }

    public static @Nullable File[] listDir() {
        return WORK_DIR.listFiles();
    }
}

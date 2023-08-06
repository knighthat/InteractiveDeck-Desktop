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

import me.knighthat.interactivedeck.file.yaml.YamlImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class Settings {

    public static String ADDRESS = "0.0.0.0";

    public static int PORT = 9129;

    public static byte[] BUFFER = new byte[1024];

    // UI
    public static @NotNull Color SELECTED_COLOR = Color.YELLOW;

    public static void loadSettings( @Nullable YamlImpl yaml ) {
        if (yaml == null) return;

        ADDRESS = (String) yaml.get("address");
        PORT = (int) yaml.get("port");
        int bSize = (int) yaml.get("buffer_size");
        BUFFER = new byte[bSize];
    }

    public static @NotNull String address() {
        return ADDRESS + ":" + PORT;
    }
}

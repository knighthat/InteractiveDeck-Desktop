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

import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.file.SettingsFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;

public class Settings {

    public static @Nullable SettingsFile FILE;

    public static String ADDRESS = "0.0.0.0";

    public static int PORT = 9129;

    public static byte[] BUFFER = new byte[1024];

    public static int COLUMNS = 4;

    public static int ROWS = 2;

    public static int BUTTON_GAP = 4;

    // UI
    public static @NotNull Color SELECTED_COLOR = Color.YELLOW;

    public static void loadSettings() {
        if (FILE == null) return;

        ADDRESS = FILE.string("address", "0.0.0.0");
        PORT = FILE.integer("port", 9129);
        int bSize = FILE.integer("buffer_size", 1024);
        BUFFER = new byte[bSize];
        COLUMNS = FILE.integer("column_count", 4);
        ROWS = FILE.integer("row_count", 2);
        BUTTON_GAP = FILE.integer("button_gap", 4);
    }

    public static void dump() {
        if (FILE == null) return;

        Log.info("Dumping settings to file");

        FILE.set("address", ADDRESS);
        FILE.set("port", PORT);
        FILE.set("buffer_size", BUFFER.length);
        FILE.set("column_count", COLUMNS);
        FILE.set("row_count", ROWS);
        FILE.set("button_gap", BUTTON_GAP);

        try {
            FILE.save();
        } catch (IOException e) {
            Log.err("Failed to save settings file!");
        }
    }

    public static @NotNull String address() {
        return ADDRESS + ":" + PORT;
    }
}

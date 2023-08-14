/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.menus.component.action;

import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.file.Settings;
import me.knighthat.interactivedeck.menus.MainMenu;
import me.knighthat.interactivedeck.menus.component.ibutton.IButton;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ActionHandler {

    public static void process(@NotNull ActionType type, @NotNull UUID uuid) {
        if (!type.equals(ActionType.PRESS)) return;

        IButton button = MainMenu.iButtons().get(uuid);
        File script = button.script();
        if (script == null) return;

        String path = script.getAbsolutePath();
        ProcessBuilder builder = new ProcessBuilder("bash", path);
        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();
            int exitCode = process.waitFor();

            Log.info("Output from script execution:");

            InputStream inStream = process.getInputStream();
            int bytesRead;
            while ((bytesRead = inStream.read(Settings.BUFFER)) != -1) {
                String decoded = new String(Settings.BUFFER, 0, bytesRead);
                Log.info(decoded);
            }

            Log.info("End of script output.");

            if (exitCode == 0) {
                Log.info("Script executed successfully!");
            } else {
                Log.warn("Script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            //TODO More error handler needed
            Log.err("Error occurs while executing " + path);
            e.printStackTrace();
        }
    }
}

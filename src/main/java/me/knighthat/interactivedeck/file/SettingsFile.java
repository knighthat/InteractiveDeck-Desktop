/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.file;

import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.utils.Resource;
import me.knighthat.interactivedeck.vars.Settings;
import me.knighthat.interactivedeck.vars.SysVars;
import me.knighthat.interactivedeck.yaml.YamlFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingsFile extends YamlFile {

    SettingsFile( @NotNull File file ) throws FileNotFoundException {
        super(file);
    }

    public static void init() {
        String name = "settings.yml";
        File file = new File(SysVars.WORK_DIR, name);

        try {
            if (file.createNewFile())
                Resource.save(name, file);

            Settings.FILE = new SettingsFile(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            Log.err("Couldn't generate settings.yml. Perhaps permission error?");
        } finally {
            Settings.loadSettings();
        }
    }
}

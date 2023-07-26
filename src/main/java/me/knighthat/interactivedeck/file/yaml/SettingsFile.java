/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.file.yaml;

import me.knighthat.interactivedeck.utils.Resource;
import me.knighthat.interactivedeck.vars.SysVars;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class SettingsFile extends YamlImpl {

    public static final @NotNull String NAME = "settings.yml";

    SettingsFile( @NotNull File file ) {
        super(file);
    }

    public static SettingsFile init() {
        File file = new File(SysVars.WORK_DIR, NAME);
        if (!file.exists()) {
            LOGGER.info("No settings file found! Generating one...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Couldn't generate settings.yml. Perhaps permission error?");
                e.printStackTrace();
                return null;
            }
            Resource.save(NAME, file);
        }
        return new SettingsFile(file);
    }
}

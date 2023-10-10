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

package me.knighthat.interactivedeck.task;

import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OpenFile extends ExecutableFile {

    public OpenFile( @NotNull String filePath ) throws FileNotFoundException {
        super( filePath );
    }

    @Override
    protected void executeInternal() {
        if (!Desktop.isDesktopSupported()) {
            Log.warn( "Desktop is not supported" );
            return;
        }
        try {
            Log.info( "============== " + file.getName() + "'s output ==============" );
            Desktop.getDesktop().open( file );
            Log.info( "----------- End of file's output ------------" );
        } catch (IOException e) {
            Log.wexc( "Failed to open " + filePath(), e, false );
        }
    }

    @Override
    public @NotNull TaskAction taskAction() {
        return TaskAction.OPEN_FILE;
    }
}
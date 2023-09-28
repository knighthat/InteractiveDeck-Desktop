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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class BashExecutor extends ExecutableTask {

    private final @NotNull ProcessBuilder process;

    public BashExecutor( @NotNull String filePath ) throws FileNotFoundException {
        super( filePath );
        this.process = new ProcessBuilder( "bash", filePath );
    }

    private void log( @NotNull Log.LogLevel level, @NotNull InputStream stream ) throws IOException {
        int eightBytes = 8 * 1024;  // 8 KiB
        byte[] buffer = new byte[eightBytes];
        int bytesRead;

        while (( bytesRead = stream.read( buffer ) ) != -1) {
            String decoded = new String( buffer, 0, bytesRead );
            Log.log( level, decoded );
        }
    }

    @Override
    protected void executeInternal() {
        int exit = 1;
        try {
            Process process = this.process.start();
            exit = process.waitFor();

            Log.info( "============== Script's output ==============" );
            this.log( Log.LogLevel.INFO, process.getInputStream() );
            this.log( Log.LogLevel.WARNING, process.getErrorStream() );
            Log.info( "----------- End of script output ------------" );


        } catch (IOException | InterruptedException e) {
            //TODO More error handler needed
            Log.exc( "Error occurs while executing " + filePath(), e, true );
        }

        String s = exit == 0
                ? "Script executed successfully!"
                : "Script execution failed with exit code: " + exit;
        Log.log( exit == 0 ? Log.LogLevel.INFO : Log.LogLevel.WARNING, s );
    }

    @Override
    public @NotNull TaskAction taskAction() {
        return TaskAction.BASH_EXEC;
    }
}
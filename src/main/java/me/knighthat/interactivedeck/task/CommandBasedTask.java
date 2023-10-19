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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.lib.json.JsonArrayConverter;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class CommandBasedTask extends ExecutableFile {

    protected final @NotNull String[] args;

    public CommandBasedTask( @NotNull String filePath, @NotNull String[] args ) throws FileNotFoundException {
        super( filePath );
        this.args = args;
    }

    protected abstract @NotNull String[] command();

    void log( @NotNull Log.LogLevel level, @NotNull InputStream stream ) throws IOException {
        int eightKB = 8 * 1024;  // 8 KiB
        byte[] buffer = new byte[eightKB];
        int bytesRead;

        while (( bytesRead = stream.read( buffer ) ) != -1) {
            String decoded = new String( buffer, 0, bytesRead );
            Log.log( level, decoded, false );
        }
    }

    public @NotNull String[] args() {return this.args;}

    @Override
    protected void executeInternal() {
        /*
         * Default exit code to 1.
         * If program executed successfully,
         * it will be changed to 0, otherwise,
         * other code will be assigned.
         */
        int exit = 1;

        try {
            Log.info( "============== File's output ==============" );

            ProcessBuilder builder = new ProcessBuilder( command() );
            Process process = builder.start();

            log( Log.LogLevel.INFO, process.getInputStream() );
            log( Log.LogLevel.WARNING, process.getErrorStream() );
            exit = process.waitFor();

        } catch (IOException e) {
            Log.exc( "Error occurs while executing " + filePath(), e, false );
        } catch (InterruptedException e) {
            Log.warn( "Program was interrupted!" );
        } finally {
            Log.info( "----------- End of file output ------------" );

            String s = exit == 0
                    ? "Script executed successfully!"
                    : "Script execution failed with exit code: " + exit;
            Log.LogLevel level = exit == 0 ? Log.LogLevel.INFO : Log.LogLevel.WARNING;
            Log.log( level, s, false );
        }
    }

    @Override
    public @NotNull JsonElement serialize() {
        JsonObject json = super.serialize().getAsJsonObject();

        JsonArray args = JsonArrayConverter.fromStringArray( this.args );
        if (!args.isEmpty())
            json.add( "args", args );

        return json;
    }
}

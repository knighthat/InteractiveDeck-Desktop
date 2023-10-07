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

package me.knighthat.interactivedeck.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Json {

    private static final @NotNull Gson GSON =
            new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static void dump( @NotNull SaveAsJson instance ) {
        File file = new File( WorkingDirectory.path(), instance.fullName() );

        try (FileWriter writer = new FileWriter( file )) {
            if (!file.exists() && !file.createNewFile()) {
                Log.warn( "Failed to create " + instance.fullName() );
                return;
            }
            GSON.toJson( instance.serialize(), writer );

            Log.info( "Saved " + instance.displayName() + " under name " + instance.fullName() );
        } catch (IOException e) {
            Log.exc( "Failed to save " + instance.displayName(), e, false );
        }
    }

    public static byte @NotNull [] gzipCompress( @NotNull JsonElement json ) throws IOException {
        String string = toString( json );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GZIPOutputStream gzip = new GZIPOutputStream( baos );
        gzip.write( string.getBytes() );
        // GZip needs to be closed before reading its bytes.
        // Because the content isn't written until it's closed
        gzip.close();

        byte[] compressedBytes = baos.toByteArray();
        baos.close();

        return compressedBytes;
    }

    public static @NotNull String toString( @NotNull JsonElement json ) {
        return GSON.toJson( json );
    }
}
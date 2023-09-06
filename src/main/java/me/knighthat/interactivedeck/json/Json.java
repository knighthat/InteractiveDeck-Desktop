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
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Json {

    public static boolean dump( @NotNull String fileName, @NotNull JsonSerializable serializable ) {
        File file = new File( WorkingDirectory.FILE, fileName );
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            if (!file.exists() && !file.createNewFile()) {
                Log.warn( "Couldn't create " + fileName + ". Please make sure permission is sufficient!" );
                return false;
            }
            JsonObject json = serializable.serialize().getAsJsonObject();
            FileWriter writer = new FileWriter( file );
            gson.toJson( json, writer );
            writer.close();

            Log.info( "Saved under " + fileName );
            return true;
        } catch (IOException e) {
            Log.err( "Failed to save " + fileName );
            Log.err( "Caused by: " + e.getMessage() );

            return false;
        }
    }
}
/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.utils;

import me.knighthat.interactivedeck.console.Log;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Resource {

    private static final @NotNull ClassLoader LOADER = Resource.class.getClassLoader();

    public static void save( @NotNull String name, @NotNull File to ) throws IOException {
        try (InputStream inStream = LOADER.getResourceAsStream(name) ;
             FileWriter writer = new FileWriter(to)) {
            int i;
            while (( i = inStream.read() ) != -1)
                writer.write((char) i);

            writer.flush();
        } catch (FileNotFoundException e) {
            Log.err(name + " could not be found!");
            e.printStackTrace();
        } catch (IOException e) {
            throw new IOException(e);
        } catch (NullPointerException e) {
            Log.warn("file's name is null!");
        }
    }
}

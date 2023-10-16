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

package me.knighthat.interactivedeck.utils;

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Resources {

    /**
     * Then loops through each entry inside jar file and find get all
     * files that start with {@param dir} and end with {@param fileName}.
     * Break at first matched item if {@param returnFirstMatch}.
     *
     * @param jar              file to perform query
     * @param dir              root folder after jar file
     * @param match            file's name, must include file's extension if applicable
     * @param returnFirstMatch cancels and returns at first match
     *
     * @return array of matching inputs
     */
    private static @NotNull JarEntry[] queryEntries( @NotNull JarFile jar, @NotNull String dir, @NotNull String match, boolean returnFirstMatch ) {
        Log.deb( "Looking for file ends with %s in directory %s".formatted( match, dir ) );

        List<JarEntry> entryList = new ArrayList<>();
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (name.startsWith( dir ) && name.endsWith( match )) {
                entryList.add( entry );
                if (returnFirstMatch)
                    break;
            }
        }

        Log.deb( "Found " + entryList.size() + " matched files." );

        return entryList.toArray( JarEntry[]::new );
    }

    /**
     * Creates and reads {@link JarFile} instance represent this app then use
     * {@link #queryEntries(JarFile, String, String, boolean)} to get first matching file.<br>
     *
     * @param dir      root folder after jar file
     * @param fileName must include file's extension if applicable
     *
     * @return {@link Optional} represents {@link JarEntry} and could be null
     */
    public static @NotNull Optional<JarEntry> jarEntry( @NotNull String dir, @NotNull String fileName ) {
        JarEntry result = null;

        try (JarFile jar = new JarFile( getJARLocation() )) {
            JarEntry[] queried = queryEntries( jar, dir, fileName, true );
            if (queried.length > 0)
                result = queried[0];
        } catch (IOException e) {
            Log.exc( "Error occurs while accessing jar file", e, true );
        }

        return Optional.ofNullable( result );
    }

    /**
     * Creates and reads {@link JarFile} instance represent this app then use
     * {@link #queryEntries(JarFile, String, String, boolean)} to get all matching files.<br>
     *
     * @param dir      root folder after jar file
     * @param fileName must include file's extension if applicable
     *
     * @return array of matching entries, empty if none found
     */
    public static @NotNull JarEntry[] jarEntries( @NotNull String dir, @NotNull String fileName ) {
        try (JarFile jar = new JarFile( getJARLocation() )) {
            return queryEntries( jar, dir, fileName, false );
        } catch (IOException e) {
            Log.exc( "Error occurs while accessing jar file", e, true );
            return new JarEntry[0];
        }
    }

    public static @NotNull String getJARLocation() {
        URL url = Resources.class.getResource( "" );
        if (url == null) {
            // This one should never be triggered
            Log.err( "Cannot find Jar file" );
            Log.reportBug();
            return "";
        }

        String uri = URLDecoder.decode( url.getPath(), StandardCharsets.UTF_8 );       // Raw path "file:/path/to/InteractiveDeck.jar!/"
        int start = 5;                                                                 // "file:" takes 5 characters
        int end = uri.indexOf( "!" );

        return uri.substring( start, end );                                            // Turns raw path to "/path/to/InteractiveDeck.jar"
    }
}

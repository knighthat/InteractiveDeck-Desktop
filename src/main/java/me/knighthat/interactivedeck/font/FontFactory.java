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

package me.knighthat.interactivedeck.font;

import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FontFactory {

    private static final @NotNull GraphicsEnvironment GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();

    public static @NotNull String[] availableFamilyNames() {
        return GRAPHICS_ENVIRONMENT.getAvailableFontFamilyNames();
    }

    public static void init() {
        Log.info( "Loading fonts..." );

        String jarLoc = getJARLocation();
        for (Font font : getInternalFonts( jarLoc ))
            GRAPHICS_ENVIRONMENT.registerFont( font );

        Log.info( "Fonts loaded!" );
    }

    private static @NotNull String getJARLocation() {
        URL url = FontFactory.class.getResource( "" );
        String uri = url == null ? "" : url.toExternalForm();
        int start = uri.lastIndexOf( ":" ) + 1;
        int end = uri.indexOf( "!" );

        return uri.substring( start, end );
    }

    private static @NotNull Font[] getInternalFonts( @NotNull String path ) {
        List<Font> fonts = new ArrayList<>( 12 );

        try (JarFile jarFile = new JarFile( path )) {

            getFontFiles( jarFile ).forEach( entry -> {
                Font font = convertJarEntryToFont( jarFile, entry );
                if (font != null)
                    fonts.add( font );
            } );
        } catch (IOException e) {
            Log.exc( "Could not read \"fonts\" folder inside .jar file", e, true );
        }

        return fonts.toArray( Font[]::new );
    }

    private static Collection<JarEntry> getFontFiles( @NotNull JarFile jar ) {
        Collection<JarEntry> files = new HashSet<>();

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith( "fonts/" ) && name.endsWith( ".ttf" ))
                files.add( entry );
        }

        return files;
    }

    private static @Nullable Font convertJarEntryToFont( @NotNull JarFile jar, @NotNull JarEntry entry ) {
        try {
            InputStream stream = jar.getInputStream( entry );
            int fontType = entry.getName().endsWith( ".ttf" ) ? Font.TRUETYPE_FONT : 0;
            return Font.createFont( fontType, stream );
        } catch (IOException | FontFormatException e) {
            Log.exc( "Error occurs while loading font " + entry.getRealName(), e, true );
            return null;
        }
    }
}

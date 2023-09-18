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

import me.knighthat.interactivedeck.InteractiveDeck;
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FontFactory {

    private static final @NotNull GraphicsEnvironment GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();

    public static @NotNull String[] availableFamilyNames() {
        return GRAPHICS_ENVIRONMENT.getAvailableFontFamilyNames();
    }

    public static void init() {
        Log.info( "Loading fonts..." );

        for (File fontFile : getFontFiles())
            try {
                Font font = Font.createFont( Font.TRUETYPE_FONT, fontFile );
                GRAPHICS_ENVIRONMENT.registerFont( font );
            } catch (IOException | FontFormatException e) {
                Log.exc( "Could not register font " + fontFile.getName(), e, false );
            }

        Log.deb( "Fonts loaded!" );
    }

    private static @NotNull File[] getFontFiles() {
        List<File> fontFiles = new ArrayList<>( 12 );

        URL fontPath = InteractiveDeck.class.getResource( "/fonts" );
        try {
            File fontFolder = new File( fontPath.toURI() );
            for (File file : fontFolder.listFiles())
                if (file.isFile() && file.getName().endsWith( ".ttf" ))
                    fontFiles.add( file );
        } catch (NullPointerException | URISyntaxException e) {
            Log.exc( "Error while loading internal fonts", e, false );
        }

        return fontFiles.toArray( File[]::new );
    }
}

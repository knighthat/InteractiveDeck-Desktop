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

package me.knighthat.interactivedeck.component.icon;

import me.knighthat.interactivedeck.exception.InvalidFileTypeException;
import me.knighthat.interactivedeck.svg.SVGParser;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class ExternalIcon {

    public static boolean verifyPath( @NotNull String path ) {
        if (path.isBlank()) {
            Log.err( "Provided image path is empty!" );
            return false;
        }

        File file = new File( path );

        if (!file.exists()) {
            Log.err( path + " does not exist!" );
            return false;
        }
        if (!file.canRead()) {
            Log.err( path + " is accessible!" );
            return false;
        }
        String pLower = path.toLowerCase();
        if (!pLower.endsWith( ".svg" )) {
            Log.err( "Unsupported image format!" );
            return false;
        } else {
            return true;
        }
    }

    /**
     * Load image from given path.<br> Accepted image formats:<br> - PNG<br> - JPEG<br> - SVG<br>
     *
     * @param path Path to image (*.png, *.jpg, *jpeg, *.svg).
     *
     * @return instance of {@link BufferedImage} from given path
     *
     * @throws FileNotFoundException    if file does not exist.
     * @throws InvalidFileTypeException when file does not fall into list of supported files.
     * @throws IOException              file is not readable or error occurs while reading file.
     */
    public static @NotNull BufferedImage fromPath( @NotNull String path, @NotNull Dimension dimension ) throws IOException {
        if (!verifyPath( path )) {
            throw new IllegalArgumentException();
        }
        File file = new File( path );

        FileInputStream inStream = new FileInputStream( file );
        SVGDocument document = SVGParser.fromInputStream( inStream );

        SVGSVGElement root = document.getRootElement();
        root.setAttribute( "width", String.valueOf( dimension.width ) );
        root.setAttribute( "height", String.valueOf( dimension.height ) );

        return SVGParser.toBufferedImage( document );
    }

    /**
     * Read bytes from given image's path.
     *
     * @param path location of the image.
     *
     * @return array of byte of the image. Array is empty when path is empty, file not exist, or file is inaccessible
     */
    public static byte[] getImageBytes( @NotNull String path ) {
        byte[] imageBytes = new byte[0];

        if (path.isBlank()) {
            return imageBytes;
        }

        File file = new File( path );
        try (FileInputStream inStream = new FileInputStream( file )) {

            imageBytes = inStream.readAllBytes();

        } catch ( IOException e ) {

            if (e instanceof FileNotFoundException) {
                Log.warn( path + " does not exist!" );
            } else if (e instanceof AccessDeniedException) {
                Log.warn( path + " is not accessible!" );
            } else {
                Log.warn( "Error while reading image " + path );
            }
        }

        return imageBytes;
    }
}

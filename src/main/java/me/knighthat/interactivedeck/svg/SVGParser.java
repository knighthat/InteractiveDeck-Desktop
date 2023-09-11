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

package me.knighthat.interactivedeck.svg;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Optional;

public class SVGParser {

    private static final @NotNull SAXSVGDocumentFactory FACTORY;

    static {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        FACTORY = new SAXSVGDocumentFactory( parser );
    }

    public static @NotNull Optional<SVGDocument> fromString( @NotNull String SVGString ) {
        SVGDocument document = null;
        try {
            Reader reader = new StringReader( SVGString );
            document = FACTORY.createSVGDocument( null, reader );
        } catch (IOException ignored) {
        }
        return Optional.ofNullable( document );
    }

    public static @NotNull Optional<SVGDocument> fromURL( @NotNull URL url ) {
        SVGDocument document = null;
        try {
            document = FACTORY.createSVGDocument( url.toExternalForm() );
        } catch (IOException ignored) {
        }
        return Optional.ofNullable( document );
    }
}

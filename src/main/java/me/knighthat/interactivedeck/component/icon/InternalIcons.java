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

import me.knighthat.interactivedeck.svg.SVGNotFound;
import me.knighthat.interactivedeck.svg.SVGParser;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

import java.net.URL;
import java.util.Optional;

public class InternalIcons {

    public final @NotNull SVGDocument PROFILE_ADD;
    public final @NotNull SVGDocument PROFILE_ADD_HOVER;
    public final @NotNull SVGDocument PROFILE_REMOVE;
    public final @NotNull SVGDocument PROFILE_REMOVE_HOVER;
    public final @NotNull SVGDocument PROFILE_CONFIGURE;
    public final @NotNull SVGDocument PROFILE_CONFIGURE_HOVER;

    public InternalIcons() {
        PROFILE_ADD = fromResource( "add" );
        PROFILE_ADD_HOVER = fromResource( "add-hover" );
        PROFILE_REMOVE = fromResource( "remove" );
        PROFILE_REMOVE_HOVER = fromResource( "remove-hover" );
        PROFILE_CONFIGURE = fromResource( "configure" );
        PROFILE_CONFIGURE_HOVER = fromResource( "configure-hover" );
    }

    @NotNull SVGDocument fromResource( @NotNull String name ) {
        String path = "/internal/icons/%s.svg".formatted( name );
        URL url = getClass().getResource( path );
        SVGDocument document = SVGNotFound.DOCUMENT;
        if (url != null) {
            Optional<SVGDocument> parsed = SVGParser.fromURL( url );
            if (parsed.isPresent())
                document = parsed.get();
        }
        return document;
    }
}
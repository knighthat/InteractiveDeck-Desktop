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

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class SVGNotFound {

    public static final @NotNull SVGDocument DOCUMENT;

    /*
     * This string is licensed under MIT - https://mit-license.org/
     * https://www.svgrepo.com/svg/332374/file-unknown
     */
    private static final @NotNull String D = "M854.6 288.7L639.4 73.4c-6-6-14.2-9.4-22.7-9.4H192c-17.7 0-32 14.3-32 32v832c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 " +
            "32-32V311.3c0-8.5-3.4-16.6-9.4-22.6zM790.2 326H602V137.8L790.2 326zm1.8 562H232V136h302v216a42 42 0 0 0 42 42h216v494zM402 549c0 5.4 4.4 9.5 9.8 9.5h32.4c5.4 0 9.8-4.2 9.8-9.4 0-28.2 " +
            "25.8-51.6 58-51.6s58 23.4 58 51.5c0 25.3-21 47.2-49.3 50.9-19.3 2.8-34.5 20.3-34.7 40.1v32c0 5.5 4.5 10 10 10h32c5.5 0 10-4.5 10-10v-12.2c0-6 4-11.5 9.7-13.3 44.6-14.4 75-54 " +
            "74.3-98.9-.8-55.5-49.2-100.8-108.5-101.6-61.4-.7-111.5 45.6-111.5 103zm78 195a32 32 0 1 0 64 0 32 32 0 1 0-64 0z";

    static {
        DOMImplementation implementation = SVGDOMImplementation.getDOMImplementation();

        String namespaceURI = SVGDOMImplementation.SVG_NAMESPACE_URI;
        DOCUMENT = (SVGDocument) implementation.createDocument( namespaceURI, "svg", null );

        SVGSVGElement root = DOCUMENT.getRootElement();
        root.setAttribute( "viewBox", "0 0 1024 1024" );

        Element path = DOCUMENT.createElementNS( namespaceURI, "path" );
        path.setAttribute( "d", D );
        path.setAttribute( "fill", "#000000" );
        root.appendChild( path );
    }
}

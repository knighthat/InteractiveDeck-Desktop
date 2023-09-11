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

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

public class SVGNotFound {

    public static final SVGDocument DOCUMENT;

    /*
     * This string is licensed under CC BY 4.0 - https://creativecommons.org/licenses/by/4.0/
     * https://www.svgrepo.com/svg/441689/page-not-found
     */
    private static final @NotNull String D = "M38.155 140.475L48.988 62.1108L92.869 67.0568L111.437 91.0118L103.396 148.121L38.155 140.475ZM84.013 94.0018L88.827 71.8068L54.046 68.3068L44.192 135" +
            ".457L98.335 142.084L104.877 96.8088L84.013 94.0018ZM59.771 123.595C59.394 123.099 56.05 120.299 55.421 119.433C64.32 109.522 86.05 109.645 92.085 122.757C91.08 123.128 86.59 125.072 " +
            "85.71 125.567C83.192 118.25 68.445 115.942 59.771 123.595ZM76.503 96.4988L72.837 99.2588L67.322 92.6168L59.815 96.6468L56.786 91.5778L63.615 88.1508L59.089 82.6988L64.589 79.0188L68" +
            ".979 85.4578L76.798 81.5328L79.154 86.2638L72.107 90.0468L76.503 96.4988Z";

    static {
        String header = "<svg viewBox=\"-20 0 190 190\" xmlns=\"http://www.w3.org/2000/svg\">";
        String footer = "</svg>";
        String path = "<path d=\"%s\" fill=\"#000000\"/>".formatted( D );

        String formatted = "%s%s%s".formatted( header, path, footer );
        DOCUMENT = SVGParser.fromString( formatted ).get();
    }
}

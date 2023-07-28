/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.menus.component.ibutton;

import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.utils.ColorConverter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class BLabel extends BChild implements JsonSerializable {

    @NotNull
    String text = "";

    @NotNull Font font = new Font("Stardos Stencil", Font.PLAIN, 14);

    public BLabel() {
        super();
        setForeground(Color.WHITE);
    }

    public BLabel( @NotNull String text ) {
        this();
        this.text = text;
    }

    public void text( @NotNull String text ) {
        this.text = text;
        repaint();
    }

    public @NotNull String text() {
        return this.text;
    }

    private @NotNull Map<String, String> font() {
        String weight = switch (font.getStyle()) {
            case Font.PLAIN -> "plain";
            case Font.BOLD -> "bold";
            case Font.ITALIC -> "italic";
            default -> "unknown";
        };

        Map<String, String> font = new LinkedHashMap<>(3);
        font.put("name", this.font.getName());
        font.put("weight", weight);
        font.put("size", String.valueOf(this.font.getSize()));

        return font;
    }

    @Override
    protected void paintComponent( Graphics g ) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getForeground());
        g2d.setFont(this.font);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int width = fontMetrics.stringWidth(text);
        int height = fontMetrics.getHeight();
        int x = ( getWidth() - width ) / 2;
        int y = ( getHeight() - height ) / 2 + fontMetrics.getAscent();
        g2d.drawString(text, x, y);

        super.paintComponent(g);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        /* Template
         * {
         *      "text":"placeholder",
         *      "color":[r, r, r],
         *      "font":
         *      {
         *          "name": "font name",
         *          "weight":"plain",
         *          "size":"14",
         *      }
         * }
         */
        return Map.of("text", text(), "color", ColorConverter.rgb(getForeground()), "font", font());
    }
}

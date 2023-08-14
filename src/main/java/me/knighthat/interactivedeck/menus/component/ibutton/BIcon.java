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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.json.JsonSerializable;
import me.knighthat.interactivedeck.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static me.knighthat.interactivedeck.utils.ColorUtils.DEFAULT_DARK;

final class BIcon extends BChild implements JsonSerializable {

    final @NotNull Dimension arcs;
    final @NotNull Color defOuter;
    final @NotNull Color defInner;

    @Nullable
    Color outer;
    @Nullable
    Color inner;

    {
        arcs = new Dimension(15, 15);
        defOuter = DEFAULT_DARK;
        defInner = DEFAULT_DARK;
    }

    BIcon() {
        super();
    }

    static @NotNull BIcon fromJson(@NotNull JsonObject json) {
        BIcon icon = new BIcon();

        JsonArray outerArray = json.getAsJsonArray("outer");
        Color outer = ColorUtils.fromJson(outerArray);
        JsonArray innerArray = json.getAsJsonArray("inner");
        Color inner = ColorUtils.fromJson(innerArray);

        icon.repaint(outer, inner);

        return icon;
    }

    public void repaint(@Nullable Color outer, @Nullable Color inner) {
        if (outer != null)
            this.outer = outer;
        if (inner != null)
            this.inner = inner;
        repaint();
    }

    public @NotNull Color inner() {
        return this.inner == null ? this.defInner : this.inner;
    }

    public @NotNull Color outer() {
        return this.outer == null ? this.defOuter : this.outer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth() - 1, height = getHeight() - 1;

        /*
         * Enabling antialiasing results in visual artifacts or "residue" on corners
         * TODO: Find a way to enable antialiasing without leaving "residue" after color transition
         *
         * g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         */
        Graphics2D g2d = (Graphics2D) g;
        Color inner = this.inner == null ? defInner : this.inner;
        g2d.setColor(inner);
        g2d.fillRoundRect(0, 0, width, height, arcs.width, arcs.height);

        Color outer = this.outer == null ? defOuter : this.outer;
        g2d.setColor(outer);
        g2d.drawRoundRect(0, 0, width, height, arcs.width, arcs.height);

        super.paintComponent(g);
    }

    @Override
    public @NotNull JsonObject json() {
        /*
         * {
         *      "outer":[r, g, b]
         *      "inner":[r, g, b]
         * }
         */
        JsonObject json = new JsonObject();
        json.add("outer", Json.parse(this.outer()));
        json.add("inner", Json.parse(this.inner()));

        return json;
    }
}

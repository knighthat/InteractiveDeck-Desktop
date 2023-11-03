/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.component.ibutton

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.interactivedeck.settings.Settings
import me.knighthat.interactivedeck.utils.ColorUtils
import me.knighthat.lib.connection.request.RequestJson
import java.awt.*
import java.util.*

internal class ButtonBackground(owner: UUID) : ButtonSubclass(owner), RequestJson {

    companion object {
        /**
         * The curve of button's background
         */
        @JvmField
        val borderRadius = Dimension(15, 15)
    }

    internal var isSelected = false
        set(value) {
            field = value
            repaint()
        }

    var fill: Color
        get() = background
        set(value) {
            // If new color has the same values as old one, then do nothing
            if (fill == value) return

            logAndSendUpdate("background", fill, value)
            background = value
        }

    var border: Color
        get() = foreground
        set(value) {
            // If new color has the same values as old one, then do nothing
            if (border == value) return

            logAndSendUpdate("border", border, value)
            foreground = value
        }

    init {
        background = ColorUtils.DEFAULT_DARK
        foreground = ColorUtils.DEFAULT_DARK
    }

    override fun paintComponent(g: Graphics?) {
        val width = width - 1
        val height = height - 1

        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g2d.color = fill
        g2d.fillRoundRect(0, 0, width, height, borderRadius.width, borderRadius.height)

        g2d.color = if (isSelected) Settings.SETTINGS.selectedColor else border
        g2d.drawRoundRect(0, 0, width, height, borderRadius.width, borderRadius.height)

        super.paintComponent(g)
    }

    override fun update(json: JsonObject) {
        for (entry in json.entrySet()) {

            val value = entry.value
            if (value !is JsonArray)
                continue

            val color = ColorUtils.fromJson(value)
            when (entry.key) {
                "border"     -> foreground = color
                "background" -> background = color
            }
        }
    }

    /**
     * Template
     * ```json
     * {
     *      "inner": [r, g, b]
     *      "outer": [r, g, b]
     * }
     * ```
     */
    override fun serialize(): JsonElement {
        val json = JsonObject()

        json.add("background", ColorUtils.toJson(background))
        json.add("border", ColorUtils.toJson(foreground))

        return json
    }

    /**
     * Template
     * ```json
     * {
     *      "inner": [r, g, b]
     *      "outer": [r, g, b]
     * }
     * ```
     */
    override fun toRequest(): JsonElement = serialize()
}
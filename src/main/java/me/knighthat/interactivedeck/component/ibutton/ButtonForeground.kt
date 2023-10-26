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
import com.google.gson.JsonPrimitive
import me.knighthat.interactivedeck.settings.Settings
import me.knighthat.interactivedeck.utils.ColorUtils
import me.knighthat.interactivedeck.utils.FontUtils
import java.awt.Color
import java.awt.Font
import java.util.*

internal class ButtonForeground(owner: UUID) : ButtonSubclass(owner) {

    init {
        horizontalAlignment = CENTER
        foreground = Color.WHITE
        font = Settings.SETTINGS.defaultButtonFont
    }

    var label: String
        get() = text
        set(value) {
            val oldText = label.trim()
            val newText = value.trim()

            /*
             * If the new text (after trimming) and old text (after trimming)
             * share the same value, then do nothing
             */
            if (newText == oldText) return

            logAndSendUpdate("text", oldText, newText)
            text = value
        }

    var styler: Font
        get() = font
        set(value) {
            if (
                styler.family == value.family
                && styler.style == value.style
                && styler.size == value.size
            ) return

            logAndSendUpdate("font", styler, value)
            font = value
        }

    var fontColor: Color
        get() = foreground
        set(value) {
            // If new color has the same values as old one, then do nothing
            if (fontColor == value) return

            logAndSendUpdate("foreground", fontColor, value)
            foreground = value
        }

    override fun update(json: JsonObject) {
        for (entry in json.entrySet()) {

            val value = entry.value
            when (entry.key) {
                "text"       -> if (value is JsonPrimitive) text = value.asString
                "foreground" -> if (value is JsonArray) foreground = ColorUtils.fromJson(value)
                "font"       -> if (value is JsonObject) font = FontUtils.fromJson(value)
            }
        }
    }

    /**
     *  Template
     *  ```json
     * {
     *      "text": $text,
     *      "color": [r, g, b],
     *      "font":
     *      {
     *          "name": $family,
     *          "weight": $style,
     *          "size": $size,
     *      }
     * }
     * ```
     */
    override fun serialize(): JsonElement {
        val json = JsonObject()
        json.addProperty("text", text)
        json.add("foreground", ColorUtils.toJson(foreground))
        json.add("font", FontUtils.toJson(font))

        return json
    }
}
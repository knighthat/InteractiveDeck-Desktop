/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.settings

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.knighthat.interactivedeck.WorkingDirectory
import me.knighthat.interactivedeck.utils.ColorUtils
import me.knighthat.interactivedeck.utils.FontUtils
import me.knighthat.lib.json.SaveAsJson
import me.knighthat.lib.logging.Log
import org.jetbrains.annotations.Range
import java.awt.Color
import java.awt.Font
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

class Settings : SaveAsJson {

    companion object {
        @JvmField
        val SETTINGS = Settings()

        @JvmStatic
        fun init() {
            val settingsFile = File(WorkingDirectory.path(), SETTINGS.getFullName())
            try {
                FileReader(settingsFile).use { reader ->
                    val json = JsonParser.parseReader(reader)
                    if (!json.isJsonNull && json.isJsonObject)
                        SETTINGS.load(json.asJsonObject)
                }
            } catch (ignored: FileNotFoundException) {
            } catch (e: IOException) {
                Log.exc("Could not read ${SETTINGS.getFullName()}", e, false)
                Log.reportBug()
            }
        }
    }

    var address: String = "0.0.0.0"
    var port: @Range(from = 0x400, to = 0xffff) Int = 9129
    var bufferSize: Int = 1024
    var selectedColor: Color = Color.YELLOW
    var uiFont: Font = Font("Comfortaa", Font.PLAIN, 14)
    var defaultButtonFont: Font = Font("StardosStencil", Font.PLAIN, 14)

    fun load(json: JsonObject) {
        for (entry in json.entrySet())
            when (entry.key) {
                "address"             -> address = json["address"].asString
                "port"                -> port = json["port"].asInt
                "buffer"              -> bufferSize = json["buffer"].asInt
                "selected_color"      -> selectedColor = ColorUtils.fromJson(json["selected_color"])
                "ui_font"             -> uiFont = FontUtils.fromJson(json["ui_font"])
                "default_button_font" -> defaultButtonFont = FontUtils.fromJson(json["default_button_font"])
            }
    }

    fun buffer(): ByteArray = ByteArray(bufferSize)

    fun addressInBytes(): ByteArray = address.split(".", limit = 4).map(String::toByte).toByteArray()

    fun addressWithPort() = "$address:$port"

    override val displayName = "settings file"

    override val fileExtension = "json"

    override val fileName = "settings"

    override fun serialize(): JsonElement {
        val json = JsonObject()

        json.addProperty("address", address)
        json.addProperty("port", port)
        json.addProperty("buffer", bufferSize)
        json.add("selected_color", ColorUtils.toJson(selectedColor))
        json.add("ui_font", FontUtils.toJson(uiFont))
        json.add("default_button_font", FontUtils.toJson(defaultButtonFont))

        return json
    }
}
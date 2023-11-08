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

import com.google.gson.JsonObject
import me.knighthat.interactivedeck.persistent.Persistent
import me.knighthat.interactivedeck.task.Task
import me.knighthat.interactivedeck.utils.ColorUtils
import me.knighthat.lib.component.ibutton.InteractiveButton
import me.knighthat.lib.connection.request.RequestJson
import me.knighthat.lib.json.JsonSerializable
import me.knighthat.lib.logging.Log
import me.knighthat.lib.task.ClientTask
import me.knighthat.lib.util.ShortUUID
import java.awt.Color
import java.awt.Container
import java.awt.Font
import java.io.IOException
import java.util.*
import javax.swing.OverlayLayout

class IButton(
    override val uuid: UUID,
    override val profile: UUID,
    override val posX: Int,
    override val posY: Int,
) : Container(), InteractiveButton, JsonSerializable, RequestJson {

    companion object {
        /**
         * Turns [JsonObject] to [IButton] instance.
         *
         * @param profile this button belongs to.
         * @param json    the json text of this button.
         *
         * @return instance of [IButton].
         *
         * @throws IOException if coordinate **x** or **y** is missing.
         */
        @Throws(IOException::class)
        fun fromJson(profile: UUID, json: JsonObject): IButton {
            if (!json.has("x"))
                throw IOException("Missing coordinate \"x\"")
            if (!json.has("y"))
                throw IOException("Missing coordinate \"y\"")

            val x = json["x"].asInt
            val y = json["y"].asInt
            val uuid: UUID =
                    if (json.has("uuid")) {
                        val idString = json["uuid"].asString
                        UUID.fromString(idString)
                    } else {
                        Log.warn("Button [x=$x,y=$y] from profile $profile does not have an UUID, assigning new one.")
                        UUID.randomUUID()
                    }

            val button = IButton(uuid, profile, x, y)
            button.update(json)

            return button
        }
    }

    private val back: ButtonBackground = ButtonBackground(uuid)
    private val front: ButtonForeground = ButtonForeground(uuid)

    // Properties
    var border: Color
        get() = back.border
        set(value) {
            back.border = value
        }
    var fill: Color
        get() = back.fill
        set(value) {
            back.fill = value
        }
    var label: String
        get() = front.label
        set(value) {
            front.label = value
        }
    var styler: Font
        get() = front.styler
        set(value) {
            front.styler = value
        }
    var fontColor: Color
        get() = front.fontColor
        set(value) {
            front.fontColor = value
        }
    var task: Task? = null
        set(value) {
            // If the new task is the same as the current one, then do nothing
            if (Objects.equals(task, value)) return

            logAndSendUpdate("task", task, value)
            field = value
        }

    init {
        foreground = ColorUtils.TRANSPARENT

        layout = OverlayLayout(this)
        add(front, 0)
        add(back, 1)

        Log.deb("Created button ${ShortUUID.from(uuid)} at [x=$posX,y=$posY]")
    }

    constructor(profile: UUID, posX: Int, posY: Int) : this(UUID.randomUUID(), profile, posX, posY)

    fun toggleSelect() {
        back.isSelected = !back.isSelected
    }

    override fun update(json: JsonObject) {
        for (entry in json.entrySet()) {

            val value = entry.value
            if (value !is JsonObject)
                continue

            when (entry.key) {
                "icon"  -> back.update(value)
                "label" -> front.update(value)
                "task"  -> task = Task.fromJson(value)
            }
        }
    }

    override fun remove() = Persistent.remove(this)

    /**
     * Template
     * ```json
     * {
     *      "uuid": $uuid,
     *      "x": $x,
     *      "y": $y,
     *      "task":
     *      {
     *          Task.json()
     *      }
     *      "icon":
     *      {
     *          BIcon.toRequest()
     *      }
     *      "label":
     *      {
     *          BLabel.json()
     *      }
     * }
     * ```
     */
    override fun toRequest(): JsonObject {
        val json = serialize()
        json.addProperty("profile", profile.toString())
        json.add("icon", back.toRequest())
        if (task !is ClientTask)
            json.remove("task")
        return json
    }

    /**
     * Template
     * ```json
     * {
     *      "uuid": $uuid,
     *      "x": $x,
     *      "y": $y,
     *      "task":
     *      {
     *          Task.json()
     *      }
     *      "icon":
     *      {
     *          BIcon.json()
     *      }
     *      "label":
     *      {
     *          BLabel.json()
     *      }
     * }
     * ```
     */
    override fun serialize(): JsonObject {
        val json = JsonObject()

        json.addProperty("uuid", uuid.toString())
        json.addProperty("x", posX)
        json.addProperty("y", posY)
        if (task != null)
            json.add("task", task!!.serialize())
        json.add("icon", back.serialize())
        json.add("label", front.serialize())

        return json
    }
}
/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.profile

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.knighthat.interactivedeck.WorkingDirectory
import me.knighthat.interactivedeck.component.ibutton.IButton
import me.knighthat.interactivedeck.persistent.Persistent
import me.knighthat.lib.connection.request.AddRequest
import me.knighthat.lib.connection.request.RemoveRequest
import me.knighthat.lib.connection.request.RequestJson
import me.knighthat.lib.exception.ProfileFormatException
import me.knighthat.lib.json.SaveAsJson
import me.knighthat.lib.logging.Log
import me.knighthat.lib.profile.AbstractProfile
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

class Profile(
    override val uuid: UUID,
    isDefault: Boolean,
    displayName: String
) : AbstractProfile<IButton>(isDefault, ArrayList(), displayName, 4, 2, 3), SaveAsJson, RequestJson {

    companion object {
        @Throws(ProfileFormatException::class)
        private fun fromJson(json: JsonObject): Profile {
            if (!json.has("uuid"))
                throw ProfileFormatException("Missing UUID!")
            if (!json.has("default"))
                throw ProfileFormatException("Cannot decide whether profile is default!")

            val uuidString = json["uuid"].asString
            val isDefault = json["default"].asBoolean

            val profile = Profile(
                UUID.fromString(uuidString),
                isDefault,
                ""
            )
            profile.update(json)

            return profile
        }

        @JvmStatic
        @Throws(IOException::class, ProfileFormatException::class)
        fun fromFile(file: File): Profile {
            val reader = FileReader(file)
            val json = JsonParser.parseReader(reader)
            reader.close()
            return fromJson(json.asJsonObject)
        }
    }

    init {
        val profileType = if (isDefault) "default profile" else "profile"
        Log.deb("Created $profileType \"$displayName\" ($uuid)")
    }

    constructor(displayName: String, isDefault: Boolean) : this(UUID.randomUUID(), isDefault, displayName) {
        addButtons(0, 0, columns, rows)
    }

    private fun addButtons(fromX: Int, fromY: Int, toX: Int, toY: Int): JsonArray {
        val added = JsonArray()
        for (y in fromY until toY)
            for (x in fromX until toX) {
                val button = IButton(uuid, x, y)
                buttons.add(button)
                Persistent.add(button)
                added.add(button.serialize())
            }
        Log.info("Added ${added.size()} button(s) to profile \"$displayName\" ($uuid)")
        return added
    }

    private fun removeButtons(conditions: (IButton) -> Boolean): JsonArray {
        val deleted = JsonArray()
        val buttons = buttons.iterator()
        while (buttons.hasNext()) {
            val button = buttons.next()
            if (conditions(button))
                continue
            buttons.remove()        // Removes button from profile's button list
            button.remove()         // Execute removal procedure from IButton
            deleted.add(button.uuid.toString())
        }
        Log.info("Deleted ${deleted.size()} button(s) from profile \"$displayName\" ($uuid)")
        return deleted
    }

    /**
     *  Template
     *  ```json
     * {
     *      "uuid": $uuid,
     *      "displayName": $displayName,
     *      "default": $isDefault,
     *      "rows": $rows,
     *      "columns": $columns,
     *      "gap": $gap,
     *      "buttons":
     *      {
     *          $function
     *      }
     * }
     * ```
     */
    private fun getProfileFormat(function: (IButton) -> JsonObject): JsonObject {

        val json = JsonObject()
        json.addProperty("uuid", uuid.toString())
        json.addProperty("displayName", displayName)
        json.addProperty("default", isDefault)
        json.addProperty("rows", rows)
        json.addProperty("columns", columns)
        json.addProperty("gap", gap)

        // Get the current button list > Applies provided function > add converted JsonObject to array
        val buttons = JsonArray(buttons.size)
        this.buttons.map(function).forEach(buttons::add)
        json.add("buttons", buttons)

        return json
    }

    override var columns = super.columns
        set(value) {
            // If new columns are equal to current rows, then do nothing
            if (columns == value) return

            // If new columns are greater than current rows, then add more buttons
            if (columns < value) {
                val added = addButtons(columns, 0, value, rows)
                AddRequest(uuid, added).send()
            }

            // If new columns are less than current rows,
            // then remove all buttons outside of this range.
            if (columns > value) {
                val deleted = removeButtons { it.posX >= value }
                RemoveRequest(uuid, deleted).send()
            }

            logAndSendUpdate("columns", columns, value)
            field = value
        }

    override var displayName = super.displayName
        set(value) {
            // If new display name is indifferent, then do nothing
            if (displayName == value) return

            logAndSendUpdate("displayName", displayName, value)
            field = value
        }

    override var gap = super.gap
        set(value) {
            // If a new gap is equal to the current gap, then do nothing
            if (gap == value) return

            logAndSendUpdate("gap", gap, value)
            field = value
        }

    override var rows = super.rows
        set(value) {
            // If new rows are equal to current rows, then do nothing
            if (rows == value) return

            // If new rows are greater than current rows, then add more buttons
            if (rows < value) {
                val added = addButtons(0, rows, columns, value)
                AddRequest(uuid, added).send()
            }

            // If new rows are less than current rows,
            // then remove all buttons outside of this range.
            if (rows > value) {
                val deleted = removeButtons { it.posY >= value }
                RemoveRequest(uuid, deleted).send()
            }

            logAndSendUpdate("rows", rows, value)
            field = value
        }

    override val fileExtension = "profile"

    override val fileName = uuid.toString()

    override fun updateButtons(buttonJson: JsonElement) {
        if (buttonJson !is JsonArray)
            return

        for (button in buttonJson)
            try {
                buttons.add(
                    IButton.fromJson(uuid, button.asJsonObject)
                )
            } catch (e: IOException) {
                Log.wexc("Failed to load a button", e, false)
            }

        // Sort button list
        buttons.sortBy { it.posX * it.posY }
    }

    override fun remove() {
        val deletedSize: Int = removeButtons { true }.size()

        val file = File(WorkingDirectory.path(), fileName)
        if (file.exists() && !file.delete()) Log.err("Could not delete ${getFullName()}")

        Persistent.remove(this)

        Log.info("Deleted profile \"$displayName\" ($uuid) with $deletedSize buttons!")
        RemoveRequest { array: JsonArray -> array.add(uuid.toString()) }.send()
    }

    override fun serialize(): JsonElement = getProfileFormat(IButton::serialize)

    override fun toRequest(): JsonElement = getProfileFormat(IButton::toRequest)
}
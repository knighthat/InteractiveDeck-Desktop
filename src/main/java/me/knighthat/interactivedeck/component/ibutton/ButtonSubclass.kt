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

import me.knighthat.interactivedeck.utils.ColorUtils
import me.knighthat.interactivedeck.utils.FontUtils
import me.knighthat.lib.component.LiveComponent
import me.knighthat.lib.connection.request.TargetedRequest
import me.knighthat.lib.json.JsonSerializable
import me.knighthat.lib.logging.Log
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.util.*
import javax.swing.JLabel

internal abstract class ButtonSubclass(override val uuid: UUID) : JLabel(), JsonSerializable, LiveComponent {

    companion object {
        /**
         * Default button's dimension
         */
        @JvmField
        val DIMENSION = Dimension(120, 120)
    }

    init {
        preferredSize = DIMENSION
        minimumSize = DIMENSION
        maximumSize = DIMENSION

        isOpaque = false
    }

    override val target = TargetedRequest.Target.BUTTON

    override fun sendUpdate(property: String, oldValue: Any?, newValue: Any?) {
        super.sendUpdate(
            property,
            when (oldValue) {
                is Font -> FontUtils.toJson(oldValue)
                else    -> oldValue
            },
            when (newValue) {
                is Font -> FontUtils.toJson(newValue)
                else    -> newValue
            }
        )
    }

    override fun logUpdate(property: String, oldValue: Any?, newValue: Any?) {
        Log.buttonUpdate(
            uuid,
            property,
            when (oldValue) {
                is Font -> FontUtils.format(oldValue)
                else    -> oldValue
            },
            when (newValue) {
                is Font -> FontUtils.format(newValue)
                else    -> newValue
            }
        )
    }

    override fun logAndSendUpdate(property: String, oldValue: Any?, newValue: Any?) {
        super.logAndSendUpdate(
            property,
            when (oldValue) {
                is Color -> ColorUtils.toJson(oldValue)
                else     -> oldValue
            },
            when (newValue) {
                is Color -> ColorUtils.toJson(newValue)
                else     -> newValue
            }
        )
    }
}
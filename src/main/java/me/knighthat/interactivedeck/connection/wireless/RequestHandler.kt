/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.connection.wireless

import me.knighthat.interactivedeck.connection.ActionHandler
import me.knighthat.interactivedeck.connection.Client
import me.knighthat.interactivedeck.file.Profile
import me.knighthat.interactivedeck.menus.MenuProperty
import me.knighthat.interactivedeck.menus.NotificationCenter
import me.knighthat.lib.connection.Connection
import me.knighthat.lib.connection.request.*
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.JsonArrayConverter
import me.knighthat.lib.logging.Log
import java.util.*

class RequestHandler : AbstractRequestHandler() {

    private val actionHandler = ActionHandler()

    private fun logClientInfo() {
        assert(Client.isConnected())

        val client = Client.INSTANCE
        val manufacturer = client.manufacturer
        val model = client.model
        val aVer = client.androidVersion

        Log.info("Client $manufacturer $model running on Android $aVer")
        NotificationCenter.setConstantMessage("$manufacturer $model (Android $aVer)")
    }

    override fun handleActionRequest(request: ActionRequest) = actionHandler.process(request.action)

    override fun handleAddRequest(request: AddRequest) {
        val uuids = HashSet<UUID>()
        request.payload
                .asJsonArray
                .forEach {
                    val uuid = UUID.fromString(it.asString)
                    uuids.add(uuid)
                }

        AddRequest {
            for (p in MenuProperty.profiles())
                if (uuids.contains(p.uuid))
                    it.add(p.serialize())
        }.send()
    }

    override fun handlePairRequest(request: Request) {
        if (Client.init(request.payload) == null)
            throw RequestException("Not enough information")

        Log.info("Pairing approved!")
        logClientInfo()

        Connection.setStatus(Connection.Status.CONNECTED)

        val uuids = MenuProperty.profiles().map(Profile::getFileName).toTypedArray()
        PairRequest(
                JsonArrayConverter.fromStringArray(uuids)
        ).send()
    }

    override fun handleRemoveRequest(request: RemoveRequest) {}

    override fun handleUpdateRequest(request: UpdateRequest) {}
}
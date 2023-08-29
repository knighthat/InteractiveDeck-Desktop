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

package me.knighthat.interactivedeck.connection.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.InteractiveDeck;
import me.knighthat.interactivedeck.connection.Client;
import me.knighthat.interactivedeck.connection.Connection;
import me.knighthat.interactivedeck.connection.Status;
import me.knighthat.interactivedeck.connection.wireless.WirelessSender;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.exception.RequestFormatException;
import me.knighthat.interactivedeck.file.Profile;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.interactivedeck.menus.component.action.ActionHandler;
import me.knighthat.interactivedeck.menus.component.action.ActionType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class RequestHandler {

    public static void process( @NotNull Request request ) {
        JsonElement content = request.content();

        switch (request.type()) {
            case PAIR -> {
                if (!Connection.isConnected())
                    handlePairing( content );
            }
            case ACTION -> {
                if (Connection.isConnected())
                    handleAction( content );
            }
            case ADD -> {
                if (Connection.isConnected())
                    handleAdd( content );
            }
            default -> {
            }
        }
    }

    static void handlePairing( @NotNull JsonElement content ) {
        InteractiveDeck.client = Client.init( content );
        if (InteractiveDeck.client == null)
            throw new RequestFormatException( "Not enough information" );

        Log.info( "Pairing approved!" );
        logClientInfo();

        Connection.status( Status.CONNECTED );

        Request request = new PairRequest();
        WirelessSender.send( request );
    }

    static void logClientInfo() {
        assert InteractiveDeck.client != null;

        String deviceInfo = "Client: %s running on Android %s";
        String model = InteractiveDeck.client.model();
        String aVer = InteractiveDeck.client.androidVersion();
        String message = String.format( deviceInfo, model, aVer );
        Log.info( message );
    }

    static void handleAction( @NotNull JsonElement content ) {
        JsonObject json = content.getAsJsonObject();

        String actionStr = json.get( "action" ).getAsString();
        ActionType type = ActionType.valueOf( actionStr );

        String idStr = json.get( "uuid" ).getAsString();
        UUID uuid = UUID.fromString( idStr );

        ActionHandler.process( type, uuid );
    }

    static void handleAdd( @NotNull JsonElement content ) {
        JsonArray json = content.getAsJsonArray();
        Set<UUID> uuids = new HashSet<>( json.size() );

        json.forEach( element -> {
            String uuidStr = element.getAsString();
            try {
                uuids.add( UUID.fromString( uuidStr ) );
            } catch (IllegalArgumentException e) {
                Log.warn( uuidStr + " is not a valid UUID" );
            }
        } );
        Predicate<Profile> condition = p -> uuids.contains( p.uuid() );
        Set<Profile> profiles = MenuProperty.profiles( condition );

        Request request = new AddRequest( profiles );
        WirelessSender.send( request );
    }
}

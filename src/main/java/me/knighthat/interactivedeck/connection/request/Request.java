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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.connection.Connection;
import me.knighthat.interactivedeck.connection.wireless.WirelessSender;
import me.knighthat.interactivedeck.console.Log;
import org.jetbrains.annotations.NotNull;

public class Request {

    public final @NotNull RequestType type;
    protected final @NotNull JsonElement content;

    public Request( @NotNull RequestType type, @NotNull JsonElement content ) {
        this.type = type;
        this.content = content;

        Log.deb( "Request to %s is created!".formatted( type.name() ) );
    }

    public static @NotNull Request parse( @NotNull JsonObject json ) {
        String typeStr = json.get( "type" ).getAsString();
        RequestType type = RequestType.valueOf( typeStr );

        boolean hasContent = json.has( "content" );
        JsonElement content = !hasContent ? JsonNull.INSTANCE : json.get( "content" );

        return new Request( type, content );
    }

    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty( "type", type.toString() );
        json.add( "content", content );

        return json.toString();
    }

    public void send() {
        if (Connection.isConnected())
            WirelessSender.send( this );
    }

    public enum RequestType {
        ADD, REMOVE, UPDATE, PAIR, ACTION
    }
}

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
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.json.Json;
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public final class AddRequest extends TargetedRequest {

    public AddRequest( @NotNull UUID uuid, @NotNull JsonArray payload ) {
        super( RequestType.ADD, Target.BUTTON, uuid, payload );
    }

    public AddRequest( @NotNull Consumer<JsonArray> consumer ) {
        super( RequestType.ADD, Target.PROFILE, null, new JsonArray() );
        consumer.accept( content.getAsJsonArray() );
    }

    @Override
    public @NotNull JsonObject serialize() {
        JsonObject json = super.serialize();

        try {
            JsonArray content = new JsonArray();
            for (byte b : Json.gzipCompress( this.content ))
                content.add( b );
            json.add( "content", content );
        } catch (IOException e) {
            Log.exc( "Failed to compress content", e, false );
            Log.reportBug();
        }

        return json;
    }
}

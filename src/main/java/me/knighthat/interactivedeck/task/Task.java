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

package me.knighthat.interactivedeck.task;

import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.json.JsonSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Task extends JsonSerializable {

    static @Nullable Task fromJson( @NotNull JsonObject json ) {
        if (!json.has( "action_type" ))
            return null;

        TaskAction action = TaskAction.fromJson( json.get( "action_type" ) );
        if (action == null)
            return null;

        List<Object> params = new ArrayList<>();
        switch (action) {

            case BASH_EXEC, AUDIO_PLAYER, OPEN_FILE -> {
                String filePath = json.get( "file_path" ).getAsString();
                params.add( filePath );
            }

            case SWITCH_PROFILE -> {
                String uuidStr = json.get( "profile" ).getAsString();
                UUID uuid = UUID.fromString( uuidStr );
                params.add( uuid );
            }
        }
        return TaskManager.create( action.type, params.toArray() );
    }

    @NotNull TaskAction taskAction();
}

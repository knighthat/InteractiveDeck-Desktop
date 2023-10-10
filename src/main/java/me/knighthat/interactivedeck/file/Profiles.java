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

package me.knighthat.interactivedeck.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.component.ibutton.IButtons;
import me.knighthat.interactivedeck.connection.request.AddRequest;
import me.knighthat.interactivedeck.exception.ProfileFormatException;
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Profiles {

    public static @NotNull Optional<Profile> fromFile( @NotNull File file ) {
        Profile profile = null;

        try (FileReader reader = new FileReader( file )) {

            JsonElement json = JsonParser.parseReader( reader );
            profile = fromJson( json.getAsJsonObject() );

        } catch (ProfileFormatException e) {
            Log.wexc( file.getName() + " does not meet requirements. Skipping...", e, false );
        } catch (JsonParseException e) {
            Log.wexc( file.getName() + " is not a valid JSON file. Skipping...", e, false );
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            Log.exc( "Could not read " + file.getName(), e, true );
        }
        return Optional.ofNullable( profile );
    }

    private static @NotNull Profile fromJson( @NotNull JsonObject json ) {
        if (!json.has( "uuid" ) ||
                !json.has( "displayName" ) ||
                !json.has( "default" ) ||
                !json.has( "rows" ) ||
                !json.has( "columns" ) ||
                !json.has( "gap" ) ||
                !json.has( "buttons" ))
            throw new ProfileFormatException( "Missing information" );

        String idStr = json.get( "uuid" ).getAsString();
        UUID uuid = UUID.fromString( idStr );
        String displayName = json.get( "displayName" ).getAsString();
        boolean isDefault = json.get( "default" ).getAsBoolean();
        int rows = json.get( "rows" ).getAsInt();
        int columns = json.get( "columns" ).getAsInt();
        int gap = json.get( "gap" ).getAsInt();

        List<IButton> buttons = new ArrayList<>();
        json.getAsJsonArray( "buttons" ).forEach( button -> {
            try {
                IButton btn = IButton.fromJson( uuid, button.getAsJsonObject() );
                buttons.add( btn );
            } catch (IOException e) {
                Log.wexc( "Failed to load a button", e, false );
            }
        } );

        return new Profile( uuid, displayName, isDefault, columns, rows, gap, buttons );
    }

    public static @NotNull Profile createDefault() {
        Profile profile = new Profile( "Main", true );

        String msg = "Default profile %s (%s) is created!";
        Log.info( msg.formatted( profile.displayName(), profile.uuid ) );

        return profile;
    }

    public static @NotNull Profile create( @NotNull String displayName ) {
        Profile profile = new Profile( displayName, false );

        String msg = "Profile %s (%s) is created!";
        Log.info( msg.formatted( displayName, profile.uuid ) );

        new AddRequest( array -> array.add( profile.serialize() ) ).send();

        return profile;
    }
}

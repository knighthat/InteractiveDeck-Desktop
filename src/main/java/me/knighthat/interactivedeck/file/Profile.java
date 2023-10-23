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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.component.ibutton.IButton;
import me.knighthat.interactivedeck.menus.MenuProperty;
import me.knighthat.lib.connection.request.AddRequest;
import me.knighthat.lib.connection.request.RemoveRequest;
import me.knighthat.lib.connection.request.RequestJson;
import me.knighthat.lib.exception.ProfileFormatException;
import me.knighthat.lib.json.SaveAsJson;
import me.knighthat.lib.logging.Log;
import me.knighthat.lib.profile.AbstractProfile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
public class Profile extends AbstractProfile<IButton> implements SaveAsJson, RequestJson {

    private static @NotNull Profile fromJson( @NotNull JsonObject json ) {
        if (!json.has( "uuid" ))
            throw new ProfileFormatException( "Missing UUID!" );
        if (!json.has( "default" ))
            throw new ProfileFormatException( "Cannot decide whether profile is default!" );

        String uuidString = json.get( "uuid" ).getAsString();
        boolean isDefault = json.get( "default" ).getAsBoolean();

        Profile profile = new Profile( UUID.fromString( uuidString ), isDefault );
        profile.update( json );

        return profile;
    }

    public static @NotNull Profile fromFile( @NotNull File file ) throws IOException {
        FileReader reader = new FileReader( file );
        JsonElement json = JsonParser.parseReader( reader );
        reader.close();
        return fromJson( json.getAsJsonObject() );
    }

    public static @NotNull Profile createDefault() {
        Profile profile = new Profile( "Main", true );

        String msg = "Default profile %s (%s) is created!";
        Log.info( msg.formatted( profile.getDisplayName(), profile.uuid ) );

        return profile;
    }

    public static @NotNull Profile create( @NotNull String displayName ) {
        Profile profile = new Profile( displayName, false );

        String msg = "Profile %s (%s) is created!";
        Log.info( msg.formatted( displayName, profile.uuid ) );

        new AddRequest( array -> array.add( profile.serialize() ) ).send();

        return profile;
    }

    @Getter
    private final @NotNull UUID uuid;

    Profile( @NotNull UUID uuid, boolean isDefault ) {this( uuid, isDefault, new ArrayList<>(), "", 4, 2, 3 );}

    Profile( @NotNull String displayName, boolean isDefault ) {
        this( UUID.randomUUID(), isDefault, new ArrayList<>( 8 ), displayName, 4, 2, 3 );
        addButtons( 0, getColumns(), 0, getRows() );
    }

    public Profile( @NotNull UUID uuid, boolean isDefault, @NotNull List<IButton> buttons, @NotNull String displayName, int columns, int rows, int gap ) {
        super( isDefault, buttons, displayName, columns, rows, gap );
        this.uuid = uuid;
    }

    private @NotNull JsonArray addButtons( int fromX, int fromY, int toX, int toY ) {
        JsonArray added = new JsonArray();

        for (int y = fromY ; y < toY ; y++)
            for (int x = fromX ; x < toX ; x++) {
                IButton button = new IButton( uuid, x, y );
                getButtons().add( button );
                MenuProperty.add( button );
                added.add( button.serialize() );
            }

        String info = "Added %s button(s) to profile %s (%s)";
        Log.info( info.formatted( added.size(), getDisplayName(), uuid.toString() ) );

        return added;
    }

    private @NotNull JsonArray removeButtons( @NotNull Predicate<IButton> conditions ) {
        JsonArray deleted = new JsonArray();

        Iterator<IButton> buttons = getButtons().iterator();
        while (buttons.hasNext()) {
            IButton button = buttons.next();
            if (!conditions.test( button ))
                continue;

            buttons.remove();
            MenuProperty.remove( button );
            deleted.add( button.getUuid().toString() );
        }

        String info = "Deleted %s button(s) from profile %s (%s)";
        Log.info( info.formatted( deleted.size(), getDisplayName(), uuid.toString() ) );

        return deleted;
    }

    private @NotNull JsonObject getProfileFormat( @NotNull Function<IButton, JsonObject> function ) {
        /* Template
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
         */
        JsonObject json = new JsonObject();

        json.addProperty( "uuid", uuid.toString() );
        json.addProperty( "displayName", getDisplayName() );
        json.addProperty( "default", isDefault() );
        json.addProperty( "rows", getRows() );
        json.addProperty( "columns", getColumns() );
        json.addProperty( "gap", getGap() );

        /*
         * Get the current button list > cast it to IButton list
         * > Use provided function to convert IButton to JsonObject form
         * > add converted JsonObject to array
         */
        JsonArray buttons = new JsonArray( getButtons().size() );
        getButtons().stream()
                    .map( function )
                    .forEach( buttons::add );
        json.add( "buttons", buttons );

        return json;
    }

    @Override
    public void setColumns( int columns ) {
        // If new columns are equal to current rows, then do nothing
        if (getColumns() == columns)
            return;

        // If new columns are greater than current rows, then add more buttons
        if (getColumns() < columns) {
            JsonArray added = addButtons( getColumns(), 0, columns, getRows() );
            new AddRequest( uuid, added ).send();
        }

        // If new columns are less than current rows,
        // then remove all buttons outside of this range.
        if (getColumns() > columns) {
            JsonArray deleted = removeButtons( button -> button.getPosX() >= columns );
            new RemoveRequest( uuid, deleted ).send();
        }

        logAndSendUpdate( "columns", getColumns(), columns );
        super.setColumns( columns );
    }

    @Override
    public void setDisplayName( @NotNull String displayName ) {
        if (displayName.equals( getDisplayName() ))
            return;

        logAndSendUpdate( "displayName", getDisplayName(), displayName );

        super.setDisplayName( displayName );
    }

    @Override
    public void setGap( int gap ) {
        // If a new gap is equal to the current gap, then do nothing
        if (getGap() == gap)
            return;

        logAndSendUpdate( "gap", getGap(), gap );
        super.setGap( gap );
    }

    @Override
    public void setRows( int rows ) {
        // If new rows are equal to current rows, then do nothing
        if (getRows() == rows)
            return;

        // If new rows are greater than current rows, then add more buttons
        if (getRows() < rows) {
            JsonArray added = addButtons( 0, getRows(), getColumns(), rows );
            new AddRequest( uuid, added ).send();
        }

        // If new rows are less than current rows,
        // then remove all buttons outside of this range.
        if (getRows() > rows) {
            JsonArray deleted = removeButtons( btn -> btn.getPosY() >= rows );
            new RemoveRequest( uuid, deleted ).send();
        }

        logAndSendUpdate( "rows", getRows(), rows );
        super.setRows( rows );
    }

    @Override
    protected void updateButtons( @NotNull JsonElement element ) {
        if (!( element instanceof JsonArray array ))
            return;

        for (JsonElement btnJson : array)
            try {
                IButton button = IButton.fromJson( uuid, btnJson.getAsJsonObject() );
                getButtons().add( button );
            } catch (IOException e) {
                Log.wexc( "Failed to load a button", e, false );
            }

        // Sort button list
        getButtons().sort(
                Comparator.comparing( btn -> btn.getPosX() * btn.getPosY() )
        );
    }

    @Override
    public @NotNull String getFileName() {return uuid.toString();}

    @Override
    public @NotNull String getFileExtension() {return "profile";}

    @Override
    public void remove() {
        int deletedSize = removeButtons( btn -> true ).size();

        File file = new File( WorkingDirectory.path(), getFileName() );
        if (file.exists() && !file.delete())
            Log.err( "Could not delete " + getFullName() );

        MenuProperty.remove( this );

        String log = "Deleted profile %s (%s) with %s buttons!";
        Log.info( log.formatted( getDisplayName(), uuid, deletedSize ) );

        new RemoveRequest(
                array -> array.add( uuid.toString() )
        ).send();
    }

    @NotNull
    @Override
    public JsonObject serialize() {return getProfileFormat( IButton::serialize );}

    @NotNull
    @Override
    public JsonObject toRequest() {return getProfileFormat( IButton::toRequest );}
}

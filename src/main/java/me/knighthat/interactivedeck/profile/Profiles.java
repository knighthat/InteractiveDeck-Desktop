/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.knighthat.interactivedeck.WorkingDirectory;
import me.knighthat.interactivedeck.button.Buttons;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.exception.ProfileFormatException;
import me.knighthat.interactivedeck.file.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Profiles {

    private static final @NotNull List<Profile> PROFILES = new ArrayList<>();

    private static @NotNull Profile active;

    public static void init() {
        File[] files = WorkingDirectory.listDir();
        if ( files != null ) {
            walkThroughProfiles( files );

            if ( PROFILES.isEmpty() ) {
                Log.info( "No profiles were found. Creating one" );
                PROFILES.add( createDefaultProfile() );
            }
        } else
            Log.err( "Failed to read directory " + WorkingDirectory.get() );
    }

    static @NotNull Profile createDefaultProfile() {
        Profile profile = new Profile();
        profile.displayName( "Default" );
        profile.isDefault = true;
        profile.row( 2 );
        profile.column( 4 );

        return profile;
    }

    static void walkThroughProfiles( @NotNull File[] files ) {
        for ( File file : files )
            if ( file.getName().endsWith( ".profile" ) )
                try ( FileReader reader = new FileReader( file ) ) {
                    Log.info( "Loading " + file.getName() );
                    load( reader );
                } catch ( ProfileFormatException e ) {
                    Log.warn( file.getName() + " does not meet requirements. Skipping..." );
                } catch ( JsonParseException e ) {
                    Log.warn( file.getName() + " is not a valid JSON file. Skipping..." );
                    Log.warn( "Cause: " + e.getMessage() );
                } catch ( FileNotFoundException ignored ) {
                } catch ( IOException e ) {
                    Log.err( "Could not read " + file.getName() + ". Perhaps permission error?" );
                    Log.err( "Error message: " + e.getMessage() );
                }
        Log.deb( "Found " + PROFILES.size() + " profile(s)" );
    }

    public static @NotNull @Unmodifiable List<Profile> list() {
        return List.copyOf( PROFILES );
    }

    public static @NotNull @Unmodifiable List<Profile> list( @NotNull Collection<UUID> uuids ) {
        List<Profile> profiles = new ArrayList<>();

        map().forEach( ( id, p ) -> {
            if ( uuids.contains( id ) )
                profiles.add( p );
        } );

        return List.copyOf( profiles );
    }

    public static void add( @NotNull Profile... profiles ) {
        Collections.addAll( PROFILES, profiles );
    }

    public static void remove( @NotNull Profile profile ) { PROFILES.remove( profile ); }

    static void load( @NotNull FileReader reader ) throws JsonParseException, ProfileFormatException {
        JsonElement json = JsonParser.parseReader( reader );
        Profile profile = Profile.fromJson( json.getAsJsonObject() );

        profile.buttons().forEach( Buttons::push );

        if ( profile.isDefault )
            active( profile );

        add( profile );
    }

    public static @NotNull Map<UUID, Profile> map() {
        Map<UUID, Profile> map = new HashMap<>( PROFILES.size() );
        PROFILES.forEach( p -> map.put( p.uuid(), p ) );

        return map;
    }

    public static @Nullable Profile get( @NotNull UUID uuid ) {
        Profile profile = null;

        for ( Profile p : list() )
            if ( p.uuid().equals( uuid ) ) {
                profile = p;
                break;
            }

        return profile;
    }

    public static @NotNull Profile active() {
        return active;
    }

    public static void active( @NotNull Profile profile ) { active = profile; }
}

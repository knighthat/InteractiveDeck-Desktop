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

package me.knighthat.interactivedeck;

import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.file.Profiles;
import me.knighthat.interactivedeck.menus.MenuProperty;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkingDirectory {

    private static @NotNull File WORK_DIR;

    public static void init() {
        String osName = System.getProperty( "os.name" );
        String defaultHome = System.getProperty( "user.home" ).concat( "/" );
        String path = defaultHome.concat( ".config/InteractiveDeck" );

        if (osName.startsWith( "Windows" )) {
            path = defaultHome.concat( "InteractiveDeck" );
        } else if (osName.startsWith( "Mac" )) {
            path = defaultHome.concat( ".InteractiveDeck" );
        }
        WORK_DIR = new File( path );

        if (WORK_DIR.exists())
            return;

        Log.info( "Working folder is not exist! Making one.." );
        if (!WORK_DIR.mkdirs())
            Log.err( "Couldn't create working directory at " + WORK_DIR.getAbsolutePath() );
    }

    public static @NotNull File file() {
        return WORK_DIR;
    }

    public static @NotNull String path() {
        return WORK_DIR.getAbsolutePath();
    }

    public static void loadProfiles() {
        List<File> profileFiles = gatherProfiles();

        if (profileFiles.isEmpty())
            createDefaultProfile();

        for (File file : profileFiles) {
            if (!file.getName().endsWith( ".profile" ))
                continue;
            Profiles.fromFile( file ).ifPresent( profile -> {
                MenuProperty.add( profile );

                String msg = "Loaded: %s (%s)";
                msg = String.format( msg, profile.displayName(), profile.uuid );
                Log.info( msg );
            } );
        }
        Log.deb( "Found " + profileFiles.size() + " profile(s)" );
    }

    private static void createDefaultProfile() {
        Log.info( "No profiles were found. Creating one..." );
        MenuProperty.add( Profiles.createDefault() );
    }


    private static List<File> gatherProfiles() {
        List<File> profiles = new ArrayList<>( 0 );
        File[] files = WORK_DIR.listFiles();

        if (files != null) {
            for (File f : files)
                if (f.getName().endsWith( ".profile" ))
                    profiles.add( f );
        } else
            Log.err( "Failed to read directory " + WorkingDirectory.file() );

        return profiles;
    }
}

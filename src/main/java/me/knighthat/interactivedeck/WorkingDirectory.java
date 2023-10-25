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

import me.knighthat.interactivedeck.persistent.Persistent;
import me.knighthat.interactivedeck.profile.Profile;
import me.knighthat.interactivedeck.utils.GlobalVars;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkingDirectory {

    private static File FILE;

    private static List<File> gatherProfiles() {
        List<File> profiles = new ArrayList<>( 0 );
        File[] files = FILE.listFiles();

        if (files != null) {
            for (File f : files)
                if (f.getName().endsWith( ".profile" ))
                    profiles.add( f );
        } else
            Log.err( "Failed to read directory " + path() );

        return profiles;
    }

    public static void init() {
        String workDir = Platform.homeDir();
        workDir += switch (Platform.TYPE) {
            case LINUX -> ".config/";
            case MACOS -> ".";
            default -> "";
        };

        FILE = new File( workDir, GlobalVars.name() );
        if (!FILE.exists() && !FILE.mkdirs())
            Log.err( "Couldn't create working directory at " + FILE.getAbsolutePath() );
        Log.info( "Working directory: " + path() );

        String logPath = System.getProperty( "log.dir" );

        if (logPath == null) {
            logPath = path() + "/logs";
            System.setProperty( "log.dir", logPath );
        }
        Log.info( "Logs location: " + logPath );
    }

    public static @NotNull String path() {return FILE.exists() ? FILE.getAbsolutePath() : ".";}

    public static void loadProfiles() {
        int loadedProfile = 0;
        for (File file : gatherProfiles())
            try {

                Profile profile = Profile.fromFile( file );
                Persistent.add( profile );

                String log = "Loaded profile: %s (%s)";
                Log.info( log.formatted( profile.getDisplayName(), profile.getUuid() ) );

                loadedProfile++;

            } catch (IOException e) {

                Log.exc( "Failed to load profile " + file.getName(), e, false );
                Log.reportBug();

            }

        if (loadedProfile == 0) {
            Log.warn( "No profile found. Creating new one..." );
            Persistent.add( new Profile( "Main", true ) );
        }

        Log.deb( "Found " + loadedProfile + " profile(s)" );
    }
}

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

package me.knighthat.interactivedeck.component.icon;

import me.knighthat.interactivedeck.svg.SVGNotFound;
import me.knighthat.interactivedeck.svg.SVGParser;
import me.knighthat.interactivedeck.utils.Resources;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InternalIcons {

    public static final @NotNull String DIR = "internal/icons/";

    public final @NotNull SVGDocument PROGRAM_ICON;
    public final @NotNull SVGDocument PROFILE_ADD;
    public final @NotNull SVGDocument PROFILE_ADD_HOVER;
    public final @NotNull SVGDocument PROFILE_REMOVE;
    public final @NotNull SVGDocument PROFILE_REMOVE_HOVER;
    public final @NotNull SVGDocument PROFILE_CONFIGURE;
    public final @NotNull SVGDocument PROFILE_CONFIGURE_HOVER;
    public final @NotNull SVGDocument TAB_TEXT;
    public final @NotNull SVGDocument TAB_ICON;
    public final @NotNull SVGDocument TAB_TASK;
    public final @NotNull SVGDocument CONNECTION_ERROR;
    public final @NotNull SVGDocument CONNECTION_CONNECTED;
    public final @NotNull SVGDocument CONNECTION_UNKNOWN;
    public final @NotNull SVGDocument CONNECTION_DISCONNECTED;
    public final @NotNull SVGDocument APP_SETTINGS;
    public final @NotNull SVGDocument APP_SETTINGS_HOVER;
    public final @NotNull SVGDocument COLOR_PALETTE;
    public final @NotNull SVGDocument GOTO_PAGE_TASK_ICON;
    public final @NotNull SVGDocument BASH_EXEC_TASK_ICON;
    public final @NotNull SVGDocument OPEN_FILE_TASK_ICON;
    public final @NotNull SVGDocument PLAY_AUDIO_TASK_ICON;
    public final @NotNull SVGDocument OPEN_WEBSITE_TASK_ICON;
    public final @NotNull SVGDocument RUN_JAR_FILE_TASK_ICON;

    public InternalIcons() {
        Log.info( "Loading icons..." );

        PROGRAM_ICON = fromResource( "program-icon" );
        PROFILE_ADD = fromResource( "add" );
        PROFILE_ADD_HOVER = fromResource( "add-hover" );
        PROFILE_REMOVE = fromResource( "remove" );
        PROFILE_REMOVE_HOVER = fromResource( "remove-hover" );
        PROFILE_CONFIGURE = fromResource( "configure" );
        PROFILE_CONFIGURE_HOVER = fromResource( "configure-hover" );
        TAB_TEXT = fromResource( "tab-text" );
        TAB_ICON = fromResource( "tab-icon" );
        TAB_TASK = fromResource( "tab-task" );
        CONNECTION_ERROR = fromResource( "connection-error" );
        CONNECTION_CONNECTED = fromResource( "connection-connected" );
        CONNECTION_UNKNOWN = fromResource( "connection-unknown" );
        CONNECTION_DISCONNECTED = fromResource( "connection-disconnected" );
        APP_SETTINGS = fromResource( "app-settings" );
        APP_SETTINGS_HOVER = fromResource( "app-settings-hover" );
        COLOR_PALETTE = fromResource( "color-palette" );
        GOTO_PAGE_TASK_ICON = fromResource( "goto-page-task-icon" );
        BASH_EXEC_TASK_ICON = fromResource( "bash-executable-task-icon" );
        OPEN_FILE_TASK_ICON = fromResource( "open-file-task-icon" );
        PLAY_AUDIO_TASK_ICON = fromResource( "play-audio-task-icon" );
        OPEN_WEBSITE_TASK_ICON = fromResource( "open-website-task-icon" );
        RUN_JAR_FILE_TASK_ICON = fromResource( "run-jar-task-icon" );

        Log.info( "Icons loaded!" );
    }

    private @NotNull SVGDocument fromResource( @NotNull String name ) {
        String fullName = name + ".svg";
        SVGDocument document = SVGNotFound.DOCUMENT;

        Optional<JarEntry> jarEntryOptional = Resources.jarEntry( DIR, fullName );
        if (jarEntryOptional.isEmpty()) {
            Log.warn( "File " + fullName + " does NOT exist. Switching to fallback!" );
            return document;
        }
        try (JarFile jar = new JarFile( Resources.getJARLocation() )) {
            document = convertEntryToSVG( jar, jarEntryOptional.get() );
        } catch (IOException e) {
            Log.exc( "Error occurs while reading icon " + fullName, e, true );
        }
        return document;
    }

    private @NotNull SVGDocument convertEntryToSVG( @NotNull JarFile jar, @NotNull JarEntry entry ) throws IOException {
        InputStream inStream = jar.getInputStream( entry );
        return SVGParser.fromInputStream( inStream );
    }
}

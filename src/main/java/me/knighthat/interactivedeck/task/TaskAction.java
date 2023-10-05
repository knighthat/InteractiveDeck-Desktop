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

import com.google.gson.JsonElement;
import me.knighthat.interactivedeck.component.icon.Icons;
import me.knighthat.interactivedeck.component.task.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.svg.SVGDocument;

public enum TaskAction {
    BASH_EXEC( BashExecutor.class, Icons.INTERNAL.BASH_EXEC_TASK_ICON, BashExecutorTaskConfigurator.class ),
    SWITCH_PROFILE( GotoPage.class, Icons.INTERNAL.GOTO_PAGE_TASK_ICON, GotoPageTaskConfigurator.class ),
    AUDIO_PLAYER( AudioPlayer.class, Icons.INTERNAL.PLAY_AUDIO_TASK_ICON, AudioPlayerTaskConfigurator.class ),
    OPEN_FILE( OpenFile.class, Icons.INTERNAL.OPEN_FILE_TASK_ICON, OpenFileTaskConfigurator.class ),
    OPEN_WEBSITE( OpenWebsite.class, Icons.INTERNAL.OPEN_WEBSITE_TASK_ICON, OpenWebsiteTaskConfigurator.class ),
    RUN_JAR_FILE( RunJarFile.class, Icons.INTERNAL.RUN_JAR_FILE_TASK_ICON, RunJarFileTaskConfigurator.class );

    public final @NotNull Class<? extends Task> type;
    public final @NotNull SVGDocument icon;
    public final @NotNull Class<? extends TaskConfigurator> configurator;

    TaskAction( @NotNull Class<? extends Task> type,
                @NotNull SVGDocument icon,
                @NotNull Class<? extends TaskConfigurator> configurator ) {
        this.type = type;
        this.icon = icon;
        this.configurator = configurator;
    }

    public static @Nullable TaskAction fromJson( @NotNull JsonElement element ) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())
            for (TaskAction action : values())
                if (action.name().equalsIgnoreCase( element.getAsString() ))
                    return action;
        return null;
    }
}

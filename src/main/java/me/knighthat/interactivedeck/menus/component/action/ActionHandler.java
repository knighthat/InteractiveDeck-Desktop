/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.menus.component.action;

import me.knighthat.interactivedeck.button.Buttons;
import me.knighthat.interactivedeck.console.Log;
import me.knighthat.interactivedeck.task.BashExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class ActionHandler {

    public static void process( @NotNull ActionType type, @NotNull UUID uuid ) {
        if ( !type.equals( ActionType.PRESS ) )
            return;

        Log.deb( "Is button available: " + Buttons.pull( uuid ).isPresent() );

        Buttons.pull( uuid ).ifPresent( button -> {
            File script = button.script();

            Log.deb( "Is script null: " + ( script == null ) );

            if ( script != null )
                new BashExecutor( script ).execute();
        } );
    }
}

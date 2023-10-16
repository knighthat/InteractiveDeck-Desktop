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
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenWebsite extends ExecutableTask {

    public final @NotNull URI uri;

    public OpenWebsite( @NotNull String uri ) throws URISyntaxException {
        super();
        this.uri = new URI( uri );
    }

    @Override
    protected void executeInternal() {
        if (!Desktop.isDesktopSupported()) {
            Log.warn( "Desktop is not supported" );
            return;
        }
        try {
            Desktop.getDesktop().browse( uri );
        } catch (IOException e) {
            Log.wexc( "Failed to open website \"" + uri + "\"", e, false );
        } catch (UnsupportedOperationException e) {
            Log.warn( "Cannot open website on this platform." );
            Log.warn( "!! THIS IS NOT A BUG !!" );
            Log.warn( "Java does not support this function. I am looking for alternative" );
        }
    }

    @Override
    public @NotNull JsonElement serialize() {
        json.addProperty( "uri", uri.toString() );
        return super.serialize();
    }

    @Override
    public @NotNull TaskAction taskAction() {
        return TaskAction.OPEN_WEBSITE;
    }
}

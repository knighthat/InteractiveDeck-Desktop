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

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import me.knighthat.interactivedeck.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AudioPlayer extends ExecutableFile {

    public AudioPlayer( @NotNull String filePath ) throws FileNotFoundException {
        super( filePath );
    }

    @Override
    protected void executeInternal() {
        try (FileInputStream inStream = new FileInputStream( file )) {
            Player player = new Player( inStream );
            Log.info( "Now playing: " + file.getName() );
            player.play();
            while (!player.isComplete())
                Thread.sleep( 500 );
            Log.info( file.getName() + " has ended!" );
            player.close();
        } catch (FileNotFoundException e) {
            Log.warn( "Audio file " + file.getName() + " does not exist!" );
        } catch (IOException e) {
            Log.wexc( "Failed to play audio file", e, false );
        } catch (JavaLayerException e) {
            Log.exc( "Failed to play audio file", e, false );
        } catch (InterruptedException e) {
            Log.exc( "Audio playback was interrupted while playing", e, true );
            Log.reportBug();
        }
    }

    @Override
    public @NotNull TaskAction taskAction() {
        return TaskAction.AUDIO_PLAYER;
    }
}

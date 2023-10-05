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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.interactivedeck.exception.InvalidFileTypeException;
import me.knighthat.interactivedeck.json.JsonArrayToArray;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunJarFile extends CommandBasedTask {

    private final @NotNull String[] vmArgs;

    public RunJarFile( @NotNull String filePath, @NotNull String[] args, @NotNull String[] vmArgs ) throws FileNotFoundException, InvalidFileTypeException {
        super( filePath, args );
        if (!filePath.endsWith( ".jar" ))
            throw new InvalidFileTypeException( "Not a jar file!" );
        this.vmArgs = vmArgs;
    }

    public @NotNull String[] vmArgs() {return this.vmArgs;}


    @Override
    protected @NotNull String[] command() {
        List<String> command = new ArrayList<>( 3 + vmArgs.length + args.length );
        command.add( "java" );
        command.addAll( Arrays.asList( vmArgs ) );
        command.add( "-jar" );
        command.add( filePath() );
        command.addAll( Arrays.asList( args ) );

        return command.toArray( String[]::new );
    }

    @Override
    public @NotNull JsonElement serialize() {
        JsonObject json = super.serialize().getAsJsonObject();

        JsonArray vmArgs = JsonArrayToArray.fromStringArray( this.vmArgs );
        if (!vmArgs.isEmpty())
            json.add( "vmArgs", vmArgs );

        return json;
    }

    @Override
    public @NotNull TaskAction taskAction() {return TaskAction.RUN_JAR_FILE;}
}

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

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

public final class BashExecutor extends CommandBasedTask {

    public BashExecutor( @NotNull String filePath, @NotNull String[] args ) throws FileNotFoundException {super( filePath, args );}

    public BashExecutor( @NotNull String filePath ) throws FileNotFoundException {this( filePath, new String[0] );}

    @Override
    protected @NotNull String[] command() {return new String[]{ "bash", filePath() };}

    @Override
    public @NotNull TaskAction taskAction() {
        return TaskAction.BASH_EXEC;
    }
}
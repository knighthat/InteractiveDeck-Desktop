/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class YamlFile {

    private final @NotNull YamlImpl yaml;

    public YamlFile( @NotNull File file ) throws FileNotFoundException {
        this.yaml = new YamlImpl(file);
    }

    public boolean contains( @NotNull String path ) {
        return this.yaml.get(path) != null;
    }

    public @NotNull String string( @NotNull String path, @NotNull String def ) {
        return contains(path) ? String.valueOf(this.yaml.get(path)) : def;
    }

    public @NotNull String string( @NotNull String path ) {
        if (!contains(path)) {
            String err = this.yaml.file.getName() + " does NOT have " + path;
            throw new NullPointerException(err);
        }
        return String.valueOf(this.yaml.get(path));
    }

    public @Range ( from = 0x80000000L, to = 0x7FFFFFFF ) int integer( @NotNull String path, @Range ( from = 0x80000000L, to = 0x7FFFFFFF ) int def ) {
        return contains(path) ? Integer.parseInt(string(path)) : def;
    }

    public @Range ( from = 0x80000000L, to = 0x7FFFFFFF ) int integer( @NotNull String path ) {
        if (!contains(path)) {
            String err = this.yaml.file.getName() + " does NOT have " + path;
            throw new NullPointerException(err);
        }
        return Integer.parseInt(string(path));
    }

    public void save() throws IOException {
        this.yaml.save();
    }

    public void set( @NotNull String path, @NotNull Object value ) {
        this.yaml.set(path, value);
    }
}

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
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlImpl {

    protected final @NotNull File file;

    private final @NotNull Yaml instance;
    private final @NotNull Map<String, Object> data;

    public YamlImpl(@NotNull File file) throws FileNotFoundException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        this.file = file;
        this.instance = new Yaml(options);
        this.data = new LinkedHashMap<>();

        FileReader reader = new FileReader(file);
        Map<String, Object> fromFile = instance.load(reader);

        this.data.putAll(fromFile);
    }

    public synchronized @Nullable Object get(@NotNull String key) {
        return this.data.get(key);
    }

    public synchronized void set(@NotNull String key, @Nullable Object value) {
        this.data.put(key, value);
    }


    public synchronized boolean save() throws IOException {
        if (!this.file.canWrite())
            return false;

        Writer writer = new FileWriter(this.file);
        this.instance.dump(this.data, writer);
        writer.flush();
        writer.close();

        return true;
    }
}

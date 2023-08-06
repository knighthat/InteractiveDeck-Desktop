/*
 * Copyright (c) 2023. Knight Hat
 * All rights reserved.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.knighthat.interactivedeck.connection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.knighthat.interactivedeck.json.JsonSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Client implements JsonSerializable {

    private final @NotNull String brand;
    private final @NotNull String device;
    private final @NotNull String manufacturer;
    private final @NotNull String model;
    private final int androidVersion;

    Client( @NotNull String brand,
            @NotNull String device,
            @NotNull String manufacturer,
            @NotNull String model,
            int androidVersion ) {
        this.brand = brand;
        this.device = device;
        this.manufacturer = manufacturer;
        this.model = model;
        this.androidVersion = androidVersion;
    }

    public static @Nullable Client init( @NotNull JsonElement content ) {
        if (!content.isJsonObject())
            return null;

        JsonObject json = content.getAsJsonObject();
        if (!json.has("brand") ||
                !json.has("device") ||
                !json.has("manufacturer") ||
                !json.has("model") ||
                !json.has("androidVersion"))
            return null;

        String brand = json.get("brand").getAsString();
        String device = json.get("device").getAsString();
        String manufacturer = json.get("brand").getAsString();
        String model = json.get("brand").getAsString();
        int version = json.get("androidVersion").getAsInt();

        return new Client(brand, device, manufacturer, model, version);
    }

    public @NotNull String brand() {
        return this.brand;
    }

    public @NotNull String device() {
        return this.device;
    }

    public @NotNull String manufacturer() {
        return this.manufacturer;
    }

    public @NotNull String model() {
        return this.model;
    }

    public int androidVersion() {
        return this.androidVersion;
    }

    @Override
    public @NotNull JsonObject json() {

        JsonElement brand = new JsonPrimitive(this.brand());
        JsonElement device = new JsonPrimitive(this.device());
        JsonElement manufacturer = new JsonPrimitive(this.manufacturer());
        JsonElement model = new JsonPrimitive(this.model());
        JsonElement androidVersion = new JsonPrimitive(this.androidVersion());

        JsonObject json = new JsonObject();
        json.add("brand", brand);
        json.add("device", device);
        json.add("manufacturer", manufacturer);
        json.add("mode", model);
        json.add("androidVersion", androidVersion);

        return json;
    }
}

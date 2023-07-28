package me.knighthat.interactivedeck.json;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Json {

    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger("JSON");

    private final @NotNull Map<String, Object> content = new LinkedHashMap<>();

    public Json() {}

    public void add(@NotNull JsonSerializable object) {
        this.content.putAll(object.serialize());
    }

    public void add(@NotNull String key, @NotNull Object value) {
        this.content.put(key, value);
    }

    public @NotNull String content() {
        StringJoiner builder = new StringJoiner(",", "{", "}");
        this.content.forEach((k, v) -> builder.add(parse(k, v)));

        return builder.toString();
    }

    public @NotNull String parse (@NotNull String key, @NotNull Object value) {
        Object finalValue = "\"null\"";

        if (value instanceof String str) {
            finalValue = "\"" + str + "\"";
        } else if (value instanceof Number number) {
            finalValue = number;
        } else if (value instanceof Map<?,?> map) {
            StringJoiner builder = new StringJoiner(",", "{", "}");
            cast(map).forEach((k, v) -> builder.add(parse(k, v)));
            finalValue = builder;
        } else if (value instanceof List<?> list) {
            StringJoiner builder = new StringJoiner(",", "[", "]");
            cast(list).forEach(builder::add);
            finalValue = builder;
        } else {
            LOGGER.warn(value + " does NOT match any format!");
        }

        return String.format("\"%s\":%s", key, finalValue);
    }

    <K, V> @NotNull Map<String, Object> cast(@NotNull Map<K, V> map) {
        Map<String, Object> newMap = new HashMap<>(map.size());
        for(Map.Entry<K, V> entry : map.entrySet())
            if (!(entry.getKey() instanceof String key)) {
                LOGGER.warn(entry.getKey().toString() + " is a String. Skipping...");
            } else {
                newMap.put(key, entry.getValue());
            }
        return newMap;
    }

    <T> @NotNull List<String> cast(@NotNull List<T> list) {
        List<String> newList = new ArrayList<>(list.size());
        list.forEach(v -> {
            if (v instanceof String str) {
                newList.add("\"" + str + "\"");
            }else if (v instanceof Number) {
                newList.add(String.valueOf(v));
            } else {
                LOGGER.warn(v.toString() + " is not a String. Skipping...");
            }
        });
        return newList;
    }
}

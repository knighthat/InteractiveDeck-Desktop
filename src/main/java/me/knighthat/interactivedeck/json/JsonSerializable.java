package me.knighthat.interactivedeck.json;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface JsonSerializable {

    @NotNull Map<String, Object> serialize();
}

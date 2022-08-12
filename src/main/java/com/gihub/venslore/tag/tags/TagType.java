package com.gihub.venslore.tag.tags;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.Arrays;

public enum TagType {
    NORMAL("Normal", "&c&lNormal Tags", ChatColor.RED);

    @Getter
    private final String name;
    @Getter
    private final String displayName;
    @Getter
    private final ChatColor color;

    TagType(String name, String displayName, ChatColor color) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
    }

    public static TagType getByName(String input) {
        return Arrays.stream(values()).filter((type) -> {
            return type.name().equalsIgnoreCase(input) || type.getName().equalsIgnoreCase(input);
        }).findFirst().orElse(null);
    }
}

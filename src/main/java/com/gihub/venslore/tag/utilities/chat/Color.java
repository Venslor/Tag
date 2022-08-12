package com.gihub.venslore.tag.utilities.chat;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

public class Color {
    public static String translate(String input) {
        try {
            Replacement replacement = new Replacement(input);
            return ChatColor.translateAlternateColorCodes('&', replacement.toString(false));
        }
        catch (Exception ignored) {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }

    public static List<String> translate(List<String> input) {
        return input.stream().map(Color::translate).collect(Collectors.toList());
    }
}


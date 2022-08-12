package com.gihub.venslore.tag.utilities.general;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Tasks {
    public static void runAsync(JavaPlugin plugin, Runnable callable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, callable);
    }

    public static void runAsyncLater(JavaPlugin plugin, Runnable callable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, callable, delay);
    }
}


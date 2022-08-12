package com.gihub.venslore.tag.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.*;


public class Utilities {
    public static String SERVER_VERSION_PACKAGE;

    private final String server;
    private final Plugin plugin;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        SERVER_VERSION_PACKAGE = packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public Utilities(String server, Plugin plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    public static boolean isNumberInteger(String index) {
        try {
            Integer.parseInt(index);
            return false;
        } catch (Exception ignored) {
            return true;
        }
    }
    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        players.addAll(Bukkit.getServer().getOnlinePlayers());
        return players;
    }
}

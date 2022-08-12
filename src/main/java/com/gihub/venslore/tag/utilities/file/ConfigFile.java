package com.gihub.venslore.tag.utilities.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.gihub.venslore.tag.utilities.chat.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigFile
extends YamlConfiguration {
    private File file;
    private JavaPlugin plugin;
    private String name;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        this.plugin = plugin;
        this.name = name;
        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }
        try {
            this.load(this.file);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.file = new File(this.plugin.getDataFolder(), this.name);
        if (!this.file.exists()) {
            this.plugin.saveResource(this.name, false);
        }
        try {
            this.load(this.file);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    public String getString(String path, boolean ignored) {
        return super.getString(path, null);
    }

    public String getString(String path) {
        return Color.translate(super.getString(path, "&bString at path &7'&3" + path + "&7' &bnot found."));
    }

    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(Color::translate).collect(Collectors.toList());
    }

    public List<String> getStringList(String path, boolean ignored) {
        if (!super.contains(path)) {
            return null;
        }
        return super.getStringList(path).stream().map(Color::translate).collect(Collectors.toList());
    }

    public File getFile() {
        return this.file;
    }
}


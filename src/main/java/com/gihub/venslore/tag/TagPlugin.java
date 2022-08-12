package com.gihub.venslore.tag;

import com.gihub.venslore.tag.tags.TagManagement;
import com.gihub.venslore.tag.tags.menu.MenuManager;
import com.gihub.venslore.tag.utilities.command.CommandFramework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gihub.venslore.tag.database.MongoManager;
import com.gihub.venslore.tag.managers.PlayerManagement;
import com.gihub.venslore.tag.managers.player.PlayerData;
import com.gihub.venslore.tag.utilities.Utilities;
import com.gihub.venslore.tag.utilities.file.ConfigFile;
import com.gihub.venslore.tag.utilities.general.Tasks;
import com.gihub.venslore.tag.utilities.register.RegisterManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class TagPlugin
extends JavaPlugin {
    public static TagPlugin INSTANCE;
    private ConfigFile dataBase,tagformat,tags,language;
    private MongoManager mongoManager;
    private PlayerManagement playerManagement;
    private MenuManager menuManager;
    private RegisterManager registerManager;
    private CommandFramework commandFramework;
    private TagManagement tagManagement;
    private List<UUID> ops = new ArrayList<UUID>();

    private void loadLanguages() {
        if (this.language == null) {
            return;
        }
        Arrays.stream(Locale.values()).forEach(language -> {
            if (this.language.getString(language.getPath(), true) == null) {
                if (language.getValue() != null) {
                    this.language.set(language.getPath(), language.getValue());
                } else if (language.getListValue() != null && this.language.getStringList(language.getPath(), true) == null) {
                    this.language.set(language.getPath(), language.getListValue());
                }
            }
        });
        this.language.save();
    }

    public void onEnable() {
        INSTANCE = this;
        this.loadLanguages();
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        this.dataBase = new ConfigFile(this, "database.yml");
        this.tagformat = new ConfigFile(this, "tagformat.yml");
        this.language = new ConfigFile(this, "messages.yml");
        this.tags = new ConfigFile(this, "tags.yml");
        this.commandFramework = new CommandFramework(this);
        this.mongoManager = new MongoManager(this);
        this.playerManagement = new PlayerManagement(this);
        this.menuManager = new MenuManager(this);
        this.registerManager = new RegisterManager();
        this.registerManager.loadListeners(this);
        this.registerManager.loadCommands();
        this.registerManager.loadManagers();
        if (!this.mongoManager.connect()) {
            return;
        }
        Tasks.runAsync(this, () -> Utilities.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = this.playerManagement.getPlayerData(player.getUniqueId());
            if (playerData == null) {
                playerData = this.playerManagement.createPlayerData(player.getUniqueId(), player.getName());
            }
            playerData.loadData();
        }));
    }

    public void onDisable() {
        if (this.commandFramework == null) {
            return;
        }
        for (Player online : Utilities.getOnlinePlayers()) {
            PlayerData playerData = this.playerManagement.getPlayerData(online.getUniqueId());
            playerData.saveData();
        }
        Utilities.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = this.playerManagement.getPlayerData(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        });
        if (this.mongoManager != null && this.mongoManager.getMongoClient() != null) {
            this.mongoManager.getMongoClient().close();
        }
        Bukkit.getScheduler().cancelTasks(this);
        INSTANCE = null;
    }


}


package com.gihub.venslore.tag.listeners;

import java.util.UUID;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.managers.player.PlayerData;
import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.general.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener
implements Listener {
    private TagPlugin plugin = TagPlugin.INSTANCE;

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void handleAsyncLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        this.plugin.getPlayerManagement().createPlayerData(uuid, name);
        PlayerData playerData = this.plugin.getPlayerManagement().getPlayerData(uuid);
        playerData.loadData();
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void handleLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getPlayerManagement().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            return;
        }
        playerData.loadAttachments(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getPlayerManagement().getPlayerData(player.getUniqueId());
        Tasks.runAsyncLater(this.plugin, () -> {
            if (playerData != null && Bukkit.getPlayer(player.getUniqueId()) != null) {
                playerData.loadAttachments(Bukkit.getPlayer(player.getUniqueId()));
            }
        }, 20L);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void handleJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getPlayerManagement().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            player.kickPlayer(Color.translate("&cYour data failed to load, please try joining again."));
            return;
        }
        Tasks.runAsync(this.plugin, () -> {
            if (player.isOp() && !this.plugin.getOps().contains(player.getUniqueId())) {
                this.plugin.getOps().add(player.getUniqueId());
            }
        });
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getPlayerManagement().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            return;
        }
        event.setQuitMessage(null);
        Tasks.runAsync(this.plugin, () -> {
            playerData.saveData();
            this.plugin.getPlayerManagement().deleteData(player.getUniqueId());
        });
        player.removeMetadata("sl-LoggedIn", TagPlugin.INSTANCE);
    }
}


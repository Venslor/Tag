package com.gihub.venslore.tag.listeners;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.tags.TagManagement;
import com.gihub.venslore.tag.utilities.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener
implements Listener {

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void handleChatFormat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        TagManagement tagManagement = TagPlugin.INSTANCE.getTagManagement();
        event.setFormat(CC.translate(tagManagement.getTagPrefix(player) + player.getPlayerListName() + "&7: &f" + event.getMessage()));
    }
}


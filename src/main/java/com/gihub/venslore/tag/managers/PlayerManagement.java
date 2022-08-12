package com.gihub.venslore.tag.managers;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.utilities.general.Tasks;
import com.gihub.venslore.tag.managers.player.PlayerData;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gihub.venslore.tag.utilities.Manager;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
@Getter
@Setter
public class PlayerManagement
extends Manager {
    private Map<UUID, PlayerData> playerData = new HashMap<UUID, PlayerData>();

    public PlayerManagement(TagPlugin plugin) {
        super(plugin);
    }

    public PlayerData createPlayerData(UUID uuid, String name) {
        if (this.playerData.containsKey(uuid)) {
            return this.getPlayerData(uuid);
        }
        this.playerData.put(uuid, new PlayerData(uuid, name));
        return this.getPlayerData(uuid);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return this.playerData.get(uuid);
    }

    public void deleteData(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return;
        }
        this.playerData.remove(uuid);
    }

    public PlayerData loadData(UUID uuid) {
        Document document = this.plugin.getMongoManager().getDocumentation().find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) {
            return null;
        }
        this.createPlayerData(uuid, document.getString("name"));
        return this.getPlayerData(uuid);
    }

    public void saveData(UUID uniqueId, String value, Object key) {
        Tasks.runAsync(this.plugin, () -> {
            Document document = this.plugin.getMongoManager().getDocumentation().find(Filters.eq("uuid", uniqueId.toString())).first();
            if (document != null && document.containsKey(value)) {
                document.put(value, key);
                this.plugin.getMongoManager().getDocumentation().replaceOne(Filters.eq("uuid", uniqueId.toString()), document, new UpdateOptions().upsert(true));
            }
        });
    }
}


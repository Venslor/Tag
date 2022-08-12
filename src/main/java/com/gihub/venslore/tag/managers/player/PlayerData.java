package com.gihub.venslore.tag.managers.player;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.tags.Tag;
import com.gihub.venslore.tag.utilities.general.StringUtils;
import com.gihub.venslore.tag.utilities.general.Tasks;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
@Getter
@Setter
public class PlayerData {
    private TagPlugin plugin = TagPlugin.INSTANCE;
    private final UUID uniqueId;
    private final String playerName;
    private List<String> permissions = new ArrayList<String>();
    private String tag = "", tagColor = "";
    private PermissionAttachment permissionAttachment;

    public boolean hasPermission(String permission) {
        return this.permissions.stream().filter(perm -> perm.equalsIgnoreCase(permission)).findFirst().orElse(null) != null;
    }

    public void loadData() {
        Document document = this.plugin.getMongoManager().getDocumentation().find(Filters.eq("uuid", this.uniqueId.toString())).first();
        if (document == null) {
            return;
        }
        this.permissions = StringUtils.getListFromString(document.getString("permissions"));
        this.tag = document.getString("tag");
        this.tagColor = document.getString("tagColor");
    }

    public void saveData(String key, Object value) {
        Tasks.runAsync(this.plugin, () -> {
            Document document = this.plugin.getMongoManager().getDocumentation().find(Filters.eq("uuid", this.uniqueId.toString())).first();

            if (document != null && document.containsKey(key)) {
                Document update = new Document();
                document.keySet().stream().filter(s -> !s.equalsIgnoreCase(key)).forEach(s -> update.put(s, document.get(s)));
                update.put(key, value);
                this.plugin.getMongoManager().getDocumentation().replaceOne(Filters.eq("uuid", this.uniqueId.toString()), update, new UpdateOptions().upsert(true));
            }
        });
    }

    public void saveData() {
        Document document = new Document();
        document.put("uuid", this.uniqueId.toString());
        document.put("name", this.playerName);
        document.put("tag", this.tag);
        document.put("tagColor", this.tagColor);
        document.put("lowerCaseName", this.playerName.toLowerCase());
        document.put("permissions", StringUtils.getStringFromList(this.permissions));
        this.plugin.getMongoManager().getDocumentation().replaceOne(Filters.eq("uuid", this.uniqueId.toString()), document, new UpdateOptions().upsert(true));
    }

    public Tag getTag() {
        return plugin.getTagManagement().getTag(this.tag);
    }

    public void loadAttachments(Player player) {
        PermissionAttachment attachment;
        if ((attachment = player.addAttachment((Plugin)this.plugin)) == null) {
            return;
        }
        attachment.getPermissions().keySet().forEach(((PermissionAttachment)attachment)::unsetPermission);
        ArrayList<String> playerPermissions = new ArrayList<String>(this.permissions);
        playerPermissions.forEach(permission -> attachment.setPermission(permission, true));
        player.recalculatePermissions();
    }


    public List<String> getAllPermissions() {
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.addAll(this.getPermissions());
        return permissions;
    }

    @ConstructorProperties(value={"uniqueId", "playerName"})
    public PlayerData(UUID uniqueId, String playerName) {
        this.uniqueId = uniqueId;
        this.playerName = playerName;
    }
}


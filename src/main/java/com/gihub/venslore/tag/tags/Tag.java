package com.gihub.venslore.tag.tags;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.utilities.ColorUtil;
import com.gihub.venslore.tag.utilities.chat.Replacement;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
@Setter
@RequiredArgsConstructor
public class Tag {
    private final TagPlugin plugin;
    private final String name;

    private String prefix = "";
    private ChatColor color = ChatColor.WHITE;
    private int weight;
    private String rarity = "&9&lCOMMON";
    private TagType tagType = TagType.NORMAL;

    public String getFormat() {
        Replacement format = new Replacement(plugin.getTagformat().getString("tags-format"));
        format.add("<color>", color.toString());
        format.add("<uniqueColor>", "");
        format.add("<tag>", this.prefix);
        return format.toString();
    }

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("prefix", this.prefix.replace("§", "&"));
        document.put("color", ColorUtil.convertChatColor(this.color));
        document.put("weight", this.weight);
        document.put("rarity", this.rarity.replace("§", "&"));
        document.put("tagType", this.tagType.name());

        plugin.getMongoManager().getTags().replaceOne(Filters.eq("name", this.name), document, new UpdateOptions().upsert(true));
    }

    public void load(Document document) {
        if (document == null) return;

        this.prefix = document.getString("prefix").replace("§", "&").replace("\u00C2", "");
        this.weight = document.getInteger("weight");
        this.color = ColorUtil.getOr(document.getString("color"), ChatColor.WHITE);
        this.rarity = document.getString("rarity");
        this.tagType = TagType.valueOf(document.getString("tagType"));
    }

    public void load() {
        Document document = this.plugin.getMongoManager().getTags().find(Filters.eq("name", this.name)).first();
        this.load(document);
    }

    public void delete() {
        this.plugin.getMongoManager().getTags().deleteOne(Filters.eq("name", this.name));
        this.plugin.getTagManagement().getTags().removeIf(tag -> tag.getName().equalsIgnoreCase(this.name));
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission("tags." + name) || player.hasPermission("tags.*");
    }
    public String getPermission(Tag tag) {
        return "tags." + tag.getName();
    }

}
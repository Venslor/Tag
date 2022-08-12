package com.gihub.venslore.tag.tags;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.managers.player.PlayerData;
import com.gihub.venslore.tag.utilities.ColorUtil;
import com.gihub.venslore.tag.utilities.Manager;
import com.gihub.venslore.tag.utilities.chat.Replacement;
import com.gihub.venslore.tag.utilities.file.ConfigFile;
import com.gihub.venslore.tag.utilities.general.Tasks;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class TagManagement extends Manager {
    private List<Tag> tags = new ArrayList<>();

    public TagManagement(TagPlugin plugin) {
        super(plugin);
        Tasks.runAsync(plugin, this::loadTags);
    }

    public Tag getTag(String name) {
        return this.tags.stream().filter(tags -> tags.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void deleteTag(Tag tag) {
        Document document = plugin.getMongoManager().getTags().find(Filters.eq("name", tag.getName())).first();
        if (document != null) {
            plugin.getMongoManager().getTags().deleteOne(document);
        }
        this.tags.remove(tag);
    }

    public void saveTags() {
        tags.forEach(tag -> {
            Document document = new Document();
            document.put("name", tag.getName());
            document.put("prefix", tag.getPrefix().replace("§", "&"));
            document.put("color", ColorUtil.convertChatColor(tag.getColor()));
            document.put("weight", tag.getWeight());
            document.put("tagType", tag.getTagType().name());
            document.put("rarity", tag.getRarity().replace("§", "&"));

            plugin.getMongoManager().getTags().replaceOne(Filters.eq("name", tag.getName()), document, new ReplaceOptions().upsert(true));
        });
    }

    public void loadTags() {
        this.tags.clear();
        plugin.getMongoManager().getTags().find().into(new ArrayList<>()).forEach(document -> {

            Tag tag = new Tag(plugin, document.getString("name"));
            tag.setPrefix(document.getString("prefix"));
            tag.setWeight(document.getInteger("weight"));
            tag.setRarity(document.getString("rarity"));
            if(document.containsKey("tagType")) {
                tag.setTagType(TagType.NORMAL);
            } else {
                tag.setTagType(TagType.valueOf(document.getString("tagType")));
            }
            try {
                tag.setColor(ChatColor.valueOf(document.getString("color")));
            } catch (Exception ignored) {
            }

            this.tags.add(tag);
        });
    }

    public String getTagPrefix(Player player) {
        PlayerData profile = plugin.getPlayerManagement().getPlayerData(player.getUniqueId());
        Tag tag = profile.getTag();

        if (tag == null) return "";
        if (!player.hasPermission("tags." + tag.getName().toLowerCase()) && !player.hasPermission("tags.*")) return "";

        Replacement format = new Replacement(plugin.getTagformat().getString("tags-format"));
        format.add("<color>", tag.getColor().toString());
        format.add("<tag>", tag.getPrefix());

        return format.toString();
    }

    public void deleteTags() {
        plugin.getMongoManager().getTags().drop();
        this.tags.clear();
    }
}

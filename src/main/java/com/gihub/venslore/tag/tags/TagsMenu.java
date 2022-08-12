package com.gihub.venslore.tag.tags;

import com.gihub.venslore.tag.Locale;
import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.managers.player.PlayerData;
import com.gihub.venslore.tag.tags.menu.Menu;
import com.gihub.venslore.tag.tags.menu.MultiPageMenu;
import com.gihub.venslore.tag.tags.menu.slots.Slot;
import com.gihub.venslore.tag.tags.menu.slots.pages.BackButton;
import com.gihub.venslore.tag.utilities.CC;
import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class TagsMenu extends MultiPageMenu {
    private final Menu menu;
    private final TagType tagType;

    @Override
    public String getPagesTitle(Player player) {
        return "";
    }

    @Override
    public List<Slot> getSwitchableSlots(Player player) {
        List<Slot> slots = new ArrayList<>();

        plugin.getTagManagement().getTags().stream().filter(tag -> tag.getTagType() == tagType).sorted(Comparator.comparingInt(Tag::getWeight)).forEach(tag -> slots.add(new TagSlot(tag)));

        return slots;
    }

    public void onOpen(Player player) {
       Color.translate("&cTags");
    }

    @Override
    public List<Slot> getEveryMenuSlots(Player player) {
        List<Slot> slots = new ArrayList<>();
        if (TagPlugin.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId()).getTag() != null) {
            slots.add(new ResetSlot());
        }
        slots.add(new BackButton(menu, 0));
        slots.add(new BackButton(menu, 8));
        return slots;
    }

    private class ResetSlot extends Slot {

        @Override
        public ItemStack getItem(Player player) {
            PlayerData profile = TagPlugin.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId());
            ItemBuilder item = new ItemBuilder(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            item.setSkullOwner(player.getName());
            item.setName("&7Selected Tag: " + CC.SECONDARY +  (profile.getTag() != null ? profile.getTag().getColor() + profile.getTag().getName() : "&cNone"));
            item.addLoreLine(" ");
            item.addLoreLine("&cReset your Tag!");
            return item.toItemStack();
        }

        @Override
        public int getSlot() {
            return 40;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            PlayerData profile = TagPlugin.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId());

            if (profile.getTag() == null) {
                player.sendMessage(Locale.TAGS_DONT_HAVE_APPLIED.toString());
                return;
            }
            profile.setTag(null);
            profile.saveData();
            player.sendMessage(Locale.TAGS_TAG_REMOVE.toString());
            player.closeInventory();
            open(player);
        }
    }

    @AllArgsConstructor
    private class TagSlot extends Slot {
        private final Tag tag;

        @Override
        public ItemStack getItem(Player player) {
            ItemBuilder item = new ItemBuilder(Material.NAME_TAG);
            PlayerData data = TagPlugin.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId());
            item.setName(tag.getColor() + tag.getName() + " &7\u2503 " + tag.getRarity() + " " + (data.getTag() == tag ? " &7(Selected)" : ""));
            item.setDurability((player.hasPermission("tags." + tag.getName().toLowerCase()) || player.hasPermission("tags.*") ? 5 : 14));
            item.addLoreLine("&7&m");
            item.addLoreLine((player.hasPermission("tags." + tag.getName().toLowerCase()) || player.hasPermission("tags.*") ? "&aUnlocked" : "&cLocked"));
            if ((player.hasPermission("tags." + tag.getName().toLowerCase()) || player.hasPermission("tags.*"))) {
                item.addLoreLine("&7Change your tag to " + tag.getColor() + tag.getPrefix() + "&7.");
            }
            item.addLoreLine("&7");
            item.addLoreLine((player.hasPermission("tags." + tag.getName().toLowerCase()) || player.hasPermission("tags.*") ? CC.PRIMARY +  "Left Click &7to select this tag." : "&cYou don't own this tag."));
            if (data.getTag() == tag) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
            }
            return item.toItemStack();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

            PlayerData data = TagPlugin.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId());

            if (!player.hasPermission("tags." + tag.getName().toLowerCase()) && !player.hasPermission("tags.*")) {
                player.sendMessage(Color.translate("&cYou don't have perms."));
            } else if (data.getTag() == tag) {
                player.sendMessage(Color.translate(tag.getColor() + tag.getName() + "&c tag is already selected."));
            } else {
                player.sendMessage(Color.translate(tag.getColor() + tag.getName() + "&a is now set as your tag."));
                data.setTag(tag.getName());
                data.saveData();
            }
            player.closeInventory();
            open(player);
        }

        @Override
        public int getSlot() {
            return 0;
        }
    }
}

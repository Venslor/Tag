package com.gihub.venslore.tag.tags.menu.slots.pages;

import com.gihub.venslore.tag.tags.menu.MultiPageMenu;
import com.gihub.venslore.tag.tags.menu.slots.Slot;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@AllArgsConstructor
public class JumpToPageSlot extends Slot {

    private final int page;
    private final MultiPageMenu menu;
    private final boolean current;
    private final int slot;

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        this.menu.changePage(player, this.page - this.menu.getPage());
        Slot.playNeutral(player);
    }

    @Override
    public ItemStack getItem(Player player) {
        ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.YELLOW + "Page " + this.page);

        if (this.current) {
            itemMeta.setLore(Arrays.asList("", ChatColor.GREEN + "Current page"));
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }
}

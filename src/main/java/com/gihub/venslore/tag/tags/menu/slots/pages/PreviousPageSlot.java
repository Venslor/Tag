package com.gihub.venslore.tag.tags.menu.slots.pages;

import com.gihub.venslore.tag.tags.menu.MultiPageMenu;
import com.gihub.venslore.tag.tags.menu.slots.Slot;
import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.item.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PreviousPageSlot extends Slot {
    private final MultiPageMenu multiPageMenu;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.ARROW);
        item.setName("&7Previous Page");
        return item.toItemStack();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (this.multiPageMenu.getPage() != 1) {
            this.multiPageMenu.changePage(player, -1);
        } else {
            player.sendMessage(Color.translate("&cYou're on the first page of the menu!"));
        }
    }

    @Override
    public int getSlot() {
        return 3;
    }

}

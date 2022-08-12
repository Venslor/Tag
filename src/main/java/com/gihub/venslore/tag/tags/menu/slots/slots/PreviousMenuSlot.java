package com.gihub.venslore.tag.tags.menu.slots.slots;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.tags.menu.Menu;
import com.gihub.venslore.tag.tags.menu.slots.Slot;
import com.gihub.venslore.tag.utilities.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PreviousMenuSlot extends Slot {
    private final TagPlugin plugin = TagPlugin.INSTANCE;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.ARROW);
        Menu lastMenu = plugin.getMenuManager().getLastOpenedMenus().get(player.getUniqueId());
        if (lastMenu == null) {
            item.setName("&cClose");
        } else {
            item.setName("&cGo Back");
        }
        return item.toItemStack();
    }

    @Override
    public int getSlot() {
        return 39;
    }

    @Override
    public int[] getSlots() {
        return new int[]{41};
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        Menu lastMenu = plugin.getMenuManager().getLastOpenedMenus().get(player.getUniqueId());
        if (lastMenu == null) {
            player.closeInventory();
        } else {
            lastMenu.open(player);
        }
    }
}

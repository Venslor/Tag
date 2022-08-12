package com.gihub.venslore.tag.tags.menu.slots.pages;
import com.gihub.venslore.tag.tags.menu.MultiPageMenu;
import com.gihub.venslore.tag.tags.menu.slots.Slot;
import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AllPagesMenu extends MultiPageMenu {
    private final MultiPageMenu menu;

    @Override
    public void onClose(Player player) {
    }

    @Override
    public String getPagesTitle(Player player) {
        return Color.translate("&7Jump to a page");
    }

    @Override
    public void onOpen(Player player) {
    }


    @Override
    public List<Slot> getSwitchableSlots(Player player) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 0; i < menu.getPages(player); i++) {
            slots.add(new PageButton(i));
        }

        return slots;
    }

    @Override
    public List<Slot> getEveryMenuSlots(Player player) {
        List<Slot> slots = new ArrayList<>();
        return slots;
    }

    @AllArgsConstructor
    private class PageButton extends Slot {
        private final int page;

        @Override
        public ItemStack getItem(Player player) {
            ItemBuilder item = new ItemBuilder(Material.WOOL);
            item.setName("&a&lPage " + page);
            item.setDurability(3);
            item.addLoreLine("&7");
            item.addLoreLine("&aJump to page " + page + ".");
            return item.toItemStack();
        }


        @Override
        public int getSlot() {
            return 0;
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            menu.changePage(player, page - menu.getPage());
        }
    }
}
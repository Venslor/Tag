package com.gihub.venslore.tag.tags.menu.slots.pages;

import com.gihub.venslore.tag.tags.menu.Menu;
import com.gihub.venslore.tag.tags.menu.MultiPageMenu;
import com.gihub.venslore.tag.tags.menu.slots.Slot;
import com.gihub.venslore.tag.utilities.chat.Color;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ViewAllPagesMenu extends Menu {

    @NonNull
    @Getter
    MultiPageMenu menu;

    public ViewAllPagesMenu(MultiPageMenu menu) {
        this.menu = menu;
    }

    @Override
    public String getName(Player player) {
        return Color.translate("Jump to Page");
    }

    @Override
    public List<Slot> getSlots(Player player) {
        List<Slot> slots = new ArrayList<>();
        int index = 10;
        slots.add(new BackButton(menu, 4));

        for (int i = 1; i <= menu.getPages(player); i++) {
            slots.add(new JumpToPageSlot(i, menu, menu.getPage() == i, index++));

            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }
        return slots;
    }
}

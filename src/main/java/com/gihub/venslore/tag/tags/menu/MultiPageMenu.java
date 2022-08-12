package com.gihub.venslore.tag.tags.menu;

import com.gihub.venslore.tag.tags.menu.slots.Slot;
import com.gihub.venslore.tag.tags.menu.slots.pages.MenuInfoSlot;
import com.gihub.venslore.tag.tags.menu.slots.pages.NextPageSlot;
import com.gihub.venslore.tag.tags.menu.slots.pages.PreviousPageSlot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class MultiPageMenu extends Menu {

    List<Slot> slots = new ArrayList<>();
    @Getter
    private int page = 1;

    @Override
    public List<Slot> getSlots(Player player) {

        int minSlot = (int) ((double) (page - 1) * 27);
        int maxSlot = (int) ((double) (page) * 27);

        slots.add(new PreviousPageSlot(this));
        slots.add(new NextPageSlot(this));
        slots.add(new MenuInfoSlot(this));

        AtomicInteger index = new AtomicInteger(0);
        this.getSwitchableSlots(player).forEach(slot -> {
            int current = index.getAndIncrement();
            if (current >= minSlot && current < maxSlot) {
                current -= (int) ((double) (27) * (page - 1)) - 9;
                slots.add(this.getNewSlot(slot, current));
            }
        });
        if (this.getEveryMenuSlots(player) != null) {
            this.getEveryMenuSlots(player).forEach(slot -> {
                slots.removeIf(s -> s.hasSlot(slot.getSlot()));
            });
            slots.addAll(this.getEveryMenuSlots(player));
        }
        return slots;
    }

    private Slot getNewSlot(Slot slot, int s) {
        return new Slot() {
            @Override
            public ItemStack getItem(Player player) {
                return slot.getItem(player);
            }

            @Override
            public int getSlot() {
                return s;
            }

            @Override
            public void onClick(Player player, int s, ClickType clickType) {
                slot.onClick(player, s, clickType);
            }
        };
    }

    @Override
    public String getName(Player player) {
        return this.getPagesTitle(player);
    }

    public void changePage(Player player, int page) {
        this.page += page;
        this.getSlots().clear();
        this.open(player);
    }

    public int getPages(Player player) {
        if (this.getSwitchableSlots(player).isEmpty()) {
            return 1;
        }
        return (int) Math.ceil(getSwitchableSlots(player).size() / (double) 27);
    }

    public void getBlackGlass(int slots2) {
        slots.add(Slot.getGlass(0));
        slots.add(Slot.getGlass(1));
        slots.add(Slot.getGlass(2));
        slots.add(Slot.getGlass(6));
        slots.add(Slot.getGlass(7));
        slots.add(Slot.getGlass(8));
        if (slots2 < 9) {
            slots.add(Slot.getGlass(18));
            slots.add(Slot.getGlass(19));
            slots.add(Slot.getGlass(20));
            slots.add(Slot.getGlass(21));
            slots.add(Slot.getGlass(22));
            slots.add(Slot.getGlass(23));
            slots.add(Slot.getGlass(24));
            slots.add(Slot.getGlass(25));
            slots.add(Slot.getGlass(26));
        } else if (slots2 < 18) {
            slots.add(Slot.getGlass(27));
            slots.add(Slot.getGlass(28));
            slots.add(Slot.getGlass(29));
            slots.add(Slot.getGlass(30));
            slots.add(Slot.getGlass(31));
            slots.add(Slot.getGlass(32));
            slots.add(Slot.getGlass(33));
            slots.add(Slot.getGlass(34));
            slots.add(Slot.getGlass(35));
        } else if (slots2 < 27) {
            slots.add(Slot.getGlass(36));
            slots.add(Slot.getGlass(37));
            slots.add(Slot.getGlass(38));
            slots.add(Slot.getGlass(39));
            slots.add(Slot.getGlass(40));
            slots.add(Slot.getGlass(41));
            slots.add(Slot.getGlass(42));
            slots.add(Slot.getGlass(43));
            slots.add(Slot.getGlass(44));
        }
    }

    private int getInventorySize(List<Slot> slots) {
        int highest = 0;
        if (!slots.isEmpty()) {
            highest = slots.stream().sorted(Comparator.comparingInt(Slot::getSlot).reversed()).map(Slot::getSlot).collect(Collectors.toList()).get(0);
        }
//        for (Slot slot : slots) {
//            if (slot.getSlots() != null) {
//                for (int i = 0; i < slot.getSlots().length; i++) {
//                    if (slot.getSlots()[i] > highest) {
//                        highest = slot.getSlots()[i];
//                    }
//                }
//            }
//        }
        return highest - 9;
//        return /*(int) (Math.ceil((*/highest/* + 1) / 9D) * 9D)*/;
    }


    public abstract String getPagesTitle(Player player);

    public abstract List<Slot> getSwitchableSlots(Player player);

    public abstract List<Slot> getEveryMenuSlots(Player player);
}

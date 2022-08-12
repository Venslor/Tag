package com.gihub.venslore.tag.tags.menu.slots;

import com.gihub.venslore.tag.utilities.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;


public abstract class Slot {

    public static boolean hasSlot(List<Slot> slots, int value) {
        return slots.stream()
                .filter(slot -> slot.getSlot() == value || slot.getSlots() != null
                        && Arrays.stream(slot.getSlots()).anyMatch(i -> i == value))
                .findFirst().orElse(null) != null;
    }

    public static Slot getGlass(int slot) {
        return new Slot() {

            @Override
            public ItemStack getItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE).setName("&7").setDurability(7).toItemStack();
            }

            @Override
            public int getSlot() {
                return slot;
            }
        };
    }



    public static void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.DIG_GRASS, 20.0F, 0.1F);
    }

    public static void playSuccess(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20.0F, 15.0F);
    }

    public static void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.CLICK, 20.0F, 1.0F);
    }

    public abstract ItemStack getItem(Player player);

    public abstract int getSlot();

    public void onClick(Player player, int slot, ClickType clickType) {

    }

    public int[] getSlots() {
        return null;
    }

    public boolean hasSlot(int slot) {
        return slot == this.getSlot() || this.getSlots() != null && Arrays.stream(this.getSlots()).anyMatch(i -> i == slot);
    }

}

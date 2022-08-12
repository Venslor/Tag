package com.gihub.venslore.tag.tags.menu;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.utilities.Manager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MenuManager extends Manager {
    public Map<UUID, Menu> openedMenus = new HashMap<>();
    public Map<UUID, Menu> lastOpenedMenus = new HashMap<>();
    public Map<UUID, Menu> openedMenuTing = new HashMap<>();

    public MenuManager(TagPlugin plugin) {
        super(plugin);
    }
}

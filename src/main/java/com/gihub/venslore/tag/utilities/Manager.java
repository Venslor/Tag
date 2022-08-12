package com.gihub.venslore.tag.utilities;

import java.beans.ConstructorProperties;

import com.gihub.venslore.tag.TagPlugin;

public class Manager {
    protected TagPlugin plugin;

    @ConstructorProperties(value={"plugin"})
    public Manager(TagPlugin plugin) {
        this.plugin = plugin;
    }

    public TagPlugin getPlugin() {
        return this.plugin;
    }
}


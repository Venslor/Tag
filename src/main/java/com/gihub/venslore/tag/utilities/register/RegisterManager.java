package com.gihub.venslore.tag.utilities.register;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.commands.TagsCommand;
import com.gihub.venslore.tag.listeners.ChatListener;
import com.gihub.venslore.tag.listeners.MenuListener;
import com.gihub.venslore.tag.listeners.PlayerListener;
import com.gihub.venslore.tag.utilities.Manager;
import com.gihub.venslore.tag.utilities.RegisterMethod;
import com.gihub.venslore.tag.utilities.command.BaseCommand;
import org.bukkit.event.Listener;

public class RegisterManager {
    private TagsCommand tagsCommand;

    @RegisterMethod
    public void loadCommands() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!BaseCommand.class.isAssignableFrom(field.getType()) || field.getType().getSuperclass() != BaseCommand.class) continue;
            field.setAccessible(true);
            try {
                Constructor<?> constructor = field.getType().getDeclaredConstructor();
                constructor.newInstance();
            }
            catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    @RegisterMethod
    public void loadManagers() {
        for (Field field : (TagPlugin.INSTANCE).getClass().getDeclaredFields()) {
            if (!Manager.class.isAssignableFrom(field.getType()) || field.getType().getSuperclass() != Manager.class) continue;
            field.setAccessible(true);
            try {
                Constructor<?> constructor = field.getType().getDeclaredConstructor(((TagPlugin.INSTANCE)).getClass());
                field.set(TagPlugin.INSTANCE, constructor.newInstance(TagPlugin.INSTANCE));
            }
            catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadListeners(TagPlugin plugin) {
        List<Listener> listeners = new ArrayList();
        listeners.add(new ChatListener());
        listeners.add(new MenuListener());
        listeners.add(new PlayerListener());
        listeners.forEach((listener) -> {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        });
    }

    public TagsCommand getTagsCommand() {
        return this.tagsCommand;
    }
}


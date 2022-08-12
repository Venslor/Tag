package com.gihub.venslore.tag.utilities.command;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.utilities.chat.Clickable;
import com.gihub.venslore.tag.utilities.chat.Color;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;

public abstract class BaseCommand {
    public TagPlugin plugin = TagPlugin.INSTANCE;

    public BaseCommand() {
        this.plugin.getCommandFramework().registerCommands(this, null);
    }

    public abstract void onCommand(CommandArgs var1);
    public void sendMultipleUsageMessage(CommandSender sender, String usageMsg, String commandName, SubCommand[] subCommands) {
        if (sender instanceof Player) {
            sender.sendMessage(Color.translate("&7&m" + StringUtils.repeat("-", 30)));
            sender.sendMessage(Color.translate("&6&l" + commandName + " &7┃ &f" + usageMsg));
            sender.sendMessage(Color.translate("&7&m" + StringUtils.repeat("-", 30)));
            for (SubCommand subCommand : subCommands) {
                Clickable usage = new Clickable("");
                usage.add(Color.translate("&f/" + commandName.toLowerCase() + " &c" + subCommand.name.toLowerCase() + "&7 " + subCommand.args), ChatColor.GRAY + subCommand.usageHover, "", "/" + commandName.toLowerCase() + " " + subCommand.name.toLowerCase());
                boolean flagFirst = true;
                String description = subCommand.usageHover;
                Flag[] flags = subCommand.flags;
                if (flags != null) {
                    if (flags.length > 0) {
                        usage.add(ChatColor.RED + "(", ChatColor.GRAY + description, "");

                        Iterator index = Arrays.stream(flags).iterator();

                        while (index.hasNext()) {
                            Flag data = (Flag) index.next();
                            String required = data.name;
                            if (!flagFirst) {
                                usage.add(ChatColor.RED + " | ", ChatColor.GRAY + description, "");
                            }

                            flagFirst = false;
                            usage.add(ChatColor.AQUA + required, ChatColor.GRAY + data.description, "");
                        }

                        usage.add(ChatColor.RED + ") ", ChatColor.GRAY + description, "");
                    }
                }
                usage.sendToPlayer((Player) sender);
            }
            new Clickable(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 30)).sendToPlayer((Player) sender);
        } else {
            sender.sendMessage(Color.translate("&cUsage: " + usageMsg));
            sender.sendMessage(Color.translate("&7&o" + usageMsg));
        }
    }
}


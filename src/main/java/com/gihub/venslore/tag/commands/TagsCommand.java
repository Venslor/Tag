package com.gihub.venslore.tag.commands;

import com.gihub.venslore.tag.Locale;
import com.gihub.venslore.tag.tags.Tag;
import com.gihub.venslore.tag.tags.TagType;
import com.gihub.venslore.tag.tags.TagsMenu;
import com.gihub.venslore.tag.utilities.ColorUtil;
import com.gihub.venslore.tag.utilities.Utilities;
import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.command.BaseCommand;
import com.gihub.venslore.tag.utilities.command.Command;
import com.gihub.venslore.tag.utilities.command.CommandArgs;
import com.gihub.venslore.tag.utilities.command.SubCommand;
import com.gihub.venslore.tag.utilities.general.StringUtils;
import com.gihub.venslore.tag.utilities.general.Tasks;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCommand extends BaseCommand {

    @Command(name = "tag", aliases = {"tags", "prefix", "prefixes"}, permission = "tag.command.tags")
    public void onCommand(CommandArgs command) {
        Tasks.runAsync(plugin, () -> {
            Player player = command.getPlayer();
            String[] args = command.getArgs();

            if (!player.hasPermission("tags.edit") || args.length == 0) {
                new TagsMenu(null, TagType.NORMAL).open(command.getPlayer());
                return;
            }
                if (args[0].equalsIgnoreCase("save")) {
                    plugin.getTagManagement().saveTags();
                    plugin.getTagManagement().loadTags();
                    player.sendMessage(ChatColor.GREEN + "Done!");
                    return;
                }
            if (args.length >= 2 && args[0].equalsIgnoreCase("delete")) {
                Tag tag = plugin.getTagManagement().getTag(StringUtils.buildMessage(args, 1));

                if (tag == null) {
                    player.sendMessage(Locale.TAG_DONT_EXISTS.toString()
                            .replace("<name>", StringUtils.buildMessage(args, 1)));
                    return;
                }

                tag.delete();

                player.sendMessage(Locale.TAG_DELETED.toString()
                        .replace("<name>", StringUtils.buildMessage(args, 1)));
                return;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    Tag tag = plugin.getTagManagement().getTag(args[1]);

                    if (tag != null) {
                        player.sendMessage(Locale.TAG_ALREADY_CREATED.toString()
                                .replace("<name>", tag.getName()));
                        return;
                    }
                    Tag created = new Tag(this.plugin, args[1]);
                    plugin.getTagManagement().saveTags();
                    created.save();

                    player.sendMessage(Locale.TAG_CREATED.toString()
                            .replace("<name>", created.getName()));
                    return;
                }
                this.sendUsage(player);
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("settag")) {
                    Tag tag = plugin.getTagManagement().getTag(args[1]);

                    if (tag == null) {
                        player.sendMessage(Locale.TAG_DONT_EXISTS.toString()
                                .replace("<name>", args[1]));
                        return;
                    }

                    tag.setPrefix(args[2]);

                    tag.save();

                    player.sendMessage(Locale.TAG_PREFIX_SET.toString()
                            .replace("<name>", tag.getName())
                            .replace("<prefix>", Color.translate(tag.getPrefix())));
                    return;
                }
                if (args[0].equalsIgnoreCase("setcolor")) {
                    Tag tag = plugin.getTagManagement().getTag(args[1]);

                    if (tag == null) {
                        player.sendMessage(Locale.TAG_DONT_EXISTS.toString()
                                .replace("<name>", args[1]));
                        return;
                    }

                    ChatColor color;
                    try {
                        color = ChatColor.valueOf(args[2].toUpperCase());
                    } catch (Exception e) {
                        player.sendMessage(Locale.TAG_INVALID_COLOR.toString());
                        return;
                    }
                    tag.setColor(color);

                    tag.save();

                    player.sendMessage(Locale.TAG_COLOR_SET.toString()
                            .replace("<name>", tag.getName())
                            .replace("<color>", ColorUtil.convertChatColor(tag.getColor(), true)));
                    return;
                }
                if (args[0].equalsIgnoreCase("setweight")) {
                    Tag tag = plugin.getTagManagement().getTag(args[1]);

                    if (tag == null) {
                        player.sendMessage(Locale.TAG_DONT_EXISTS.toString()
                                .replace("<name>", args[1]));
                        return;
                    }
                    if (Utilities.isNumberInteger(args[2])) {
                        player.sendMessage(Locale.USE_NUMBERS.toString());
                        return;
                    }
                    tag.setWeight(Integer.parseInt(args[2]));

                    tag.save();

                    player.sendMessage(Locale.TAG_PREFIX_SET.toString()
                            .replace("<name>", tag.getName())
                            .replace("<prefix>", Color.translate(tag.getPrefix())));
                    return;
                }
                this.sendUsage(player);
            } else {
                this.sendUsage(player);
            }
        });
        return;
    }

    private void sendUsage(CommandSender sender) {
        sendMultipleUsageMessage(sender, "Tag Command Help", "Tag", new SubCommand[]{
                new SubCommand("create", "Create a tag.", "<name>", null),
                new SubCommand("delete", "Delete a tag.", "<name>", null),
                new SubCommand("settag", "Set the tag prefix of a tag.", "<name> <tag>", null),
                new SubCommand("setcolor", "Set the color of a tag.", "<name> <color>", null),
                new SubCommand("settype", "Set the type of a tag.", "<name> <type>", null),
                new SubCommand("setweight", "Set the weight of a tag.", "<name> <weight>", null),
                new SubCommand("save", "Save all the tags to tags.yml and database.", "", null),
        });
    }
}

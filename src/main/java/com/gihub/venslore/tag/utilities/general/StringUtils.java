package com.gihub.venslore.tag.utilities.general;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class StringUtils {

    public static ThreadLocal<DecimalFormat> remaining_seconds = new ThreadLocal<DecimalFormat>() {
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.#");
        }
    };
    public static ThreadLocal<DecimalFormat> remaining_seconds_trailing = new ThreadLocal<DecimalFormat>() {
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.0");
        }
    };

    public static String getEnchantment(String name) {
        String enchant = name;

        if (name.equalsIgnoreCase("sharp") || name.equalsIgnoreCase("sharpness")) {
            enchant = "DAMAGE_ALL";
        }

        if (name.equalsIgnoreCase("ff") || name.equalsIgnoreCase("featherfalling")) {
            enchant = "FEATHER_FALLING";
        }

        if (name.equalsIgnoreCase("fire") || name.equalsIgnoreCase("fireaspect")) {
            enchant = "FIRE_ASPECT";
        }

        if (name.equalsIgnoreCase("kb") || name.equalsIgnoreCase("knock")) {
            enchant = "KNOCKBACK";
        }

        if (name.equalsIgnoreCase("smi") || name.equalsIgnoreCase("smite")) {
            enchant = "DAMAGE_UNDEAD";
        }

        if (name.equalsIgnoreCase("bane") || name.equalsIgnoreCase("baneof") || name.equalsIgnoreCase("baneofarthropods")) {
            enchant = "DAMAGE_ARTHROPODS";
        }

        if (name.equalsIgnoreCase("prot") || name.equalsIgnoreCase("protection")) {
            enchant = "PROTECTION_ENVIRONMENTAL";
        }

        if (name.equalsIgnoreCase("fire") || name.equalsIgnoreCase("fireprot") || name.equalsIgnoreCase("fireprotection")) {
            enchant = "PROTECTION_FIRE";
        }

        if (name.equalsIgnoreCase("blast") || name.equalsIgnoreCase("blastprot") || name.equalsIgnoreCase("blastprotection")) {
            enchant = "PROTECTION_EXPLOSIONS";
        }

        if (name.equalsIgnoreCase("proj") || name.equalsIgnoreCase("projprot") || name.equalsIgnoreCase("projectileprotection")) {
            enchant = "PROTECTION_PROJECTILE";
        }

        if (name.equalsIgnoreCase("loot") || name.equalsIgnoreCase("looting")) {
            enchant = "LOOT_BONUS_MOBS";
        }

        if (name.equalsIgnoreCase("fort") || name.equalsIgnoreCase("fortune")) {
            enchant = "LOOT_BONUS_BLOCKS";
        }

        if (name.equalsIgnoreCase("silk") || name.equalsIgnoreCase("silktouch")) {
            enchant = "SILK_TOUCH";
        }

        if (name.equalsIgnoreCase("pow") || name.equalsIgnoreCase("power")) {
            enchant = "ARROW_DAMAGE";
        }

        if (name.equalsIgnoreCase("pun") || name.equalsIgnoreCase("punch")) {
            enchant = "ARROW_KNOCKBACK";
        }

        if (name.equalsIgnoreCase("fla") || name.equalsIgnoreCase("flame")) {
            enchant = "ARROW_FIRE";
        }

        if (name.equalsIgnoreCase("inf") || name.equalsIgnoreCase("infinity")) {
            enchant = "ARROW_INFINITE";
        }

        if (name.equalsIgnoreCase("unb") || name.equalsIgnoreCase("unbreaking")) {
            enchant = "DURABILITY";
        }

        if (name.equalsIgnoreCase("eff") || name.equalsIgnoreCase("efficiency")) {
            enchant = "DIG_SPEED";
        }

        return enchant.toUpperCase();
    }

    public static String niceBuilder(Collection collection) {
        return niceBuilder(collection, ", ", " and ", ".");
    }

    public static String niceBuilder(Collection collection, String color) {
        return niceBuilder(collection, color + ", ", color + " and ", color + '.');
    }

    public static String niceBuilder(Collection collection, String colonColor, String color) {
        return niceBuilder(collection, colonColor + ", " + color, colonColor + " and " + color, colonColor + '.');
    }

    public static String niceBuilder(Collection collection, String delimiter, String and, String dot) {
        if (collection != null && !collection.isEmpty()) {
            List contents = new ArrayList(collection);
            String last = null;
            if (contents.size() > 1) {
                last = (String) contents.remove(contents.size() - 1);
            }

            StringBuilder builder = new StringBuilder();

            String name;
            for (Iterator iterator = contents.iterator(); iterator.hasNext(); builder.append(name)) {
                name = (String) iterator.next();
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
            }

            if (last != null) {
                builder.append(and).append(last);
            }

            return builder.append(dot != null ? dot : "").toString();
        } else {
            return "";
        }
    }

    public static String getEntityName(Entity entity) {
        switch (entity.getType().name()) {
            case "BLAZE":
                return "Blaze";
            case "CAVE_SPIDER":
                return "Cave Spider";
            case "CREEPER":
                return "Creeper";
            case "ENDERMAN":
                return "Enderman";
            case "IRON_GOLEM":
                return "Iron Golem";
            case "MAGMA_CUBE":
                return "Magma Cube";
            case "PIG_ZOMBIE":
                return "Pig Zombie";
            case "PLAYER":
                return "Player";
            case "SILVERFISH":
                return "Silverfish";
            case "SKELETON":
                return "Skeleton";
            case "SLIME":
                return "Slime";
            case "SPIDER":
                return "Spider";
            case "VILLAGER":
                return "Villager";
            case "WITCH":
                return "Witch";
            case "WITHER":
                return "Wither";
            case "WOLF":
                return "Wolf";
            case "ZOMBIE":
                return "Zombie";
        }
        return "";
    }

    public static String getRemaining(long millis, boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }

    public static long parse(String input) {
        if (input == null || input.isEmpty()) {
            return -1L;
        }

        long result = 0L;

        StringBuilder number = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                String str;

                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }

        return result;
    }

    private static long convert(int value, char unit) {
        switch (unit) {

            case 'y': {
                return value * TimeUnit.DAYS.toMillis(365L);
            }

            case 'M': {
                return value * TimeUnit.DAYS.toMillis(30L);
            }

            case 'd': {
                return value * TimeUnit.DAYS.toMillis(1L);
            }

            case 'h': {
                return value * TimeUnit.HOURS.toMillis(1L);
            }

            case 'm': {
                return value * TimeUnit.MINUTES.toMillis(1L);
            }

            case 's': {
                return value * TimeUnit.SECONDS.toMillis(1L);
            }

            default: {
                return -1L;
            }
        }
    }

    public static Location destringifyLocation(String string) {
        String[] split = string.substring(1, string.length() - 2).split(",");

        World world = Bukkit.getWorld(split[0]);

        if (world == null) return null;

        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);

        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        Location loc = new Location(world, x, y, z);

        loc.setYaw(yaw);
        loc.setPitch(pitch);

        return loc;
    }

    public static String stringifyLocation(Location location) {
        return "[" + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch() + "]";
    }

    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < TimeUnit.MINUTES.toMillis(1)) {
            return (trail ? remaining_seconds_trailing : remaining_seconds).get().format((double) duration * 0.001) + 's';
        }

        return DurationFormatUtils.formatDuration(duration, (duration >= TimeUnit.HOURS.toMillis(1) ? "HH:" : "") + "mm:ss");
    }

    public static String formatInt(int i) {
        int r = i * 1000;
        int sec = r / 1000 % 60;
        int min = r / 60000 % 60;
        int h = r / 3600000 % 24;

        return (h > 0 ? h + ":" : "") + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
    }

    public static Object getTime(int seconds) {
        if (seconds < 60) {
            return seconds + "seconds";
        }

        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;

        if (minutes < 60) {
            if (secondsLeft > 0) {
                return minutes + "minutes " + secondsLeft + "seconds";
            }

            return minutes + "minutes";
        }

        if (minutes < 1440) {
            String time = "";

            int hours = minutes / 60;
            time = hours + "hours";
            int inMins = 60 * hours;
            int leftOver = minutes - inMins;

            if (leftOver >= 1) {
                time = time + " " + leftOver + "minutes";
            }

            if (secondsLeft > 0) {
                time = time + " " + secondsLeft + "seconds";
            }

            return time;
        }
        String time = "";
        int days = minutes / 1440;
        time = days + "days";
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;

        if (leftOver >= 1) {
            if (leftOver < 60) {
                time = time + " " + leftOver + "minutes";
            } else {
                int hours = leftOver / 60;
                time = time + " " + hours + "hours";
                int hoursInMins = 60 * hours;
                int minsLeft = leftOver - hoursInMins;

                if (leftOver >= 1) {
                    time = time + " " + minsLeft + "minutes";
                }
            }
        }

        if (secondsLeft > 0) {
            time = time + " " + secondsLeft + "seconds";
        }

        return time;
    }

    public static void setSlots(int slots, JavaPlugin plugin) {
        slots = Math.abs(slots);

        try {
            Object invoke = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".CraftServer").getDeclaredMethod("getHandle", new Class[0]).invoke(Bukkit.getServer());
            Field declaredField = invoke.getClass().getSuperclass().getDeclaredField("maxPlayers");

            declaredField.setAccessible(true);
            declaredField.set(invoke, slots);

            changeProperties(slots, plugin);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void changeProperties(int slots, JavaPlugin plugin) {
        Path resolve = Paths.get(plugin.getDataFolder().getParentFile().getAbsolutePath()).getParent().resolve("server.properties");

        try {
            List<String> allLines = Files.readAllLines(resolve);

            for (int i = 0; i < allLines.size(); ++i) {
                if (allLines.get(i).startsWith("max-players")) {
                    allLines.remove(i);
                }
            }

            allLines.add("max-players=" + slots);

            Files.write(resolve, allLines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringFromList(List<String> source) {
        if (source.size() == 0) {
            return "Empty";
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String piece : source) {
            builder.append(piece.replace(",", "#%&$"));
            if (i < source.size() - 1) {
                builder.append(", ");
            }
            i++;
        }
        return builder.toString();
    }

    public static List<String> getListFromString(String source) {
        if (source.equals("Empty")) return new ArrayList<>();

        return Arrays.stream(source.split(", ")).map(s -> s.replace("#%&$", ",")).collect(Collectors.toList());
    }

    public static String convertFirstUpperCase(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

    public static String buildMessage(String[] args, int start) {
        if (start >= args.length) {
            return "";
        }
        return ChatColor.stripColor(String.join(" ", Arrays.copyOfRange(args, start, args.length)));
    }
}



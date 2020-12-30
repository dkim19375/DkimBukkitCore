package me.dkim19375.dkim19375core.external;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FormattingUtils {
    private FormattingUtils() {

    }
    public static String formatWithPAPIAndColors(final Player player, final String string) {
        String parsed;
        parsed = PlaceholderAPI.setPlaceholders(player, string);
        parsed = ChatColor.translateAlternateColorCodes('&', parsed);
        return parsed;
    }

    public static String formatWithPAPIAndColors(final Player player, final String string, final char altColorChar) {
        String parsed;
        parsed = PlaceholderAPI.setPlaceholders(player, string);
        parsed = ChatColor.translateAlternateColorCodes(altColorChar, parsed);
        return parsed;
    }

    public static String formatWithColors(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String formatWithColors(final String string, final char altColorChar) {
        return ChatColor.translateAlternateColorCodes(altColorChar, string);
    }
}

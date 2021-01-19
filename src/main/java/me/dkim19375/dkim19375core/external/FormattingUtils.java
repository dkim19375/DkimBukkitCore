package me.dkim19375.dkim19375core.external;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FormattingUtils {
    private FormattingUtils() {
    }

    @NotNull
    public static String formatWithPAPIAndColors(final @NotNull Player player, final @NotNull String string) {
        String parsed;
        parsed = PlaceholderAPI.setPlaceholders(player, string);
        parsed = ChatColor.translateAlternateColorCodes('&', parsed);
        return parsed;
    }

    @NotNull
    public static String formatWithPAPIAndColors(final @NotNull Player player, final @NotNull String string, final char altColorChar) {
        String parsed;
        parsed = PlaceholderAPI.setPlaceholders(player, string);
        parsed = ChatColor.translateAlternateColorCodes(altColorChar, parsed);
        return parsed;
    }

    @NotNull
    public static String formatWithColors(final @NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @NotNull
    public static String formatWithColors(final @NotNull String string, final char altColorChar) {
        return ChatColor.translateAlternateColorCodes(altColorChar, string);
    }

    @NotNull
    public static String formatWithPAPI(final @NotNull String string) {
        return PlaceholderAPI.setPlaceholders(null, string);
    }

    @NotNull
    public static String formatWithPAPI(final @NotNull Player player, final @NotNull String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}

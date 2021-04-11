package me.dkim19375.dkim19375core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerUtils {
    private PlayerUtils() {
    }

    @Nullable
    public static Player getFromAll(final String uuidOrPlayer) {
        if (uuidOrPlayer.matches("^\\w{3,16}$")) {
            return Bukkit.getPlayer(uuidOrPlayer);
        }
        if (uuidOrPlayer.matches("[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}")) {
            final UUID uuid = UUIDUtils.getFromString(uuidOrPlayer);
            if (uuid == null) {
                return null;
            }
            return Bukkit.getPlayer(uuid);
        }
        return null;
    }

    @Nullable
    public static Player getFromAll(final UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @Nullable
    public static Player getPlayerFromUsername(final String username) {
        if (username.matches("^\\w{3,16}$")) {
            return Bukkit.getPlayer(username);
        }
        return null;
    }

    @Nullable
    public static Player getPlayerFromUUID(final String stringUUID) {
        final UUID uuid = UUIDUtils.getFromString(stringUUID);
        if (uuid == null) {
            return null;
        }
        return Bukkit.getPlayer(uuid);
    }

    @Nullable
    public static Player getPlayerFromUUID(final UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }
}

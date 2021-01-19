package me.dkim19375.dkim19375core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerUtils {
    private PlayerUtils() {
    }

    public enum InputTypes {
        VALID_USERNAME, VALID_UUID, INVALID_USERNAME, INVALID_UUID, INVALID
    }

    @Nullable
    public static Player getFromAll(final String uuidOrPlayer) {
        if (uuidOrPlayer.matches("^\\w{3,16}$")) {
            return Bukkit.getPlayer(uuidOrPlayer);
        }
        if (uuidOrPlayer.matches("[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}")) {
            final UUID uuid = UUID.fromString(uuidOrPlayer);
            return Bukkit.getPlayer(uuid);
        }
        return null;
    }

    @Nullable
    public static Player getFromAll(final UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @NotNull
    public static InputTypes getInputType(final String uuidOrPlayer) {
        if (uuidOrPlayer.matches("^\\w{3,16}$")) {
            final Player player = Bukkit.getPlayer(uuidOrPlayer);
            if (player != null) {
                return InputTypes.VALID_USERNAME;
            }
            return InputTypes.INVALID_USERNAME;
        }
        if (uuidOrPlayer.matches("[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}")) {
            final UUID uuid = UUID.fromString(uuidOrPlayer);
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                return InputTypes.VALID_UUID;
            }
        }
        if (uuidOrPlayer.matches("[0-9a-fA-F]{32}")) {
            return InputTypes.INVALID_UUID;
        }
        return InputTypes.INVALID;
    }

    @Nullable
    public static Player getPlayerFromUsername(final String username) {
        if (username.matches("^\\w{3,16}$")) {
            return Bukkit.getPlayer(username);
        }
        return null;
    }

    @Nullable
    public static Player getPlayerFromUUID(final String StringUUID) {
        final UUID uuid = UUID.fromString(StringUUID);
        return Bukkit.getPlayer(uuid);
    }

    @Nullable
    public static Player getPlayerFromUUID(final UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }
}

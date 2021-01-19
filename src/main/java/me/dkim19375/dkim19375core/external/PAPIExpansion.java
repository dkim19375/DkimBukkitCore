package me.dkim19375.dkim19375core.external;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIExpansion extends PlaceholderExpansion {
    private final String identifier;
    private final String authors;
    private final String version;

    public PAPIExpansion(final @NotNull JavaPlugin plugin, final @Nullable String identifier, final @Nullable String authors, final @Nullable String version) {
        if (identifier == null) {
            this.identifier = plugin.getDescription().getName();
        } else {
            this.identifier = identifier;
        }
        if (authors == null) {
            this.authors = plugin.getDescription().getAuthors().toString();
        } else {
            this.authors = authors;
        }
        if (version == null) {
            this.version = plugin.getDescription().getVersion();
        } else {
            this.version = version;
        }
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return authors;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    @Override
    @NotNull
    public String getVersion() {
        return version;
    }

    @Override
    @Nullable
    public String onPlaceholderRequest(final Player player, final @Nullable String identifier) {
        return null;
    }

    @Override
    @Nullable
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        return null;
    }
}

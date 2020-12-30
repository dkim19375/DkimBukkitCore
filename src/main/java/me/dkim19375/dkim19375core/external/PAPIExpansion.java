package me.dkim19375.dkim19375core.external;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public abstract class PAPIExpansion extends PlaceholderExpansion {
    private final String identifier;
    private final String authors;
    private final String version;

    public PAPIExpansion(final JavaPlugin plugin, final @Nullable String identifier, final @Nullable String authors, final @Nullable String version) {
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

    @SuppressWarnings("NullableProblems")
    @Override
    public String getAuthor() {
        return authors;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String getIdentifier() {
        return identifier;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public abstract String onPlaceholderRequest(final Player player, final @Nullable String identifier);
}

package me.dkim19375.dkim19375core.external;

import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {
    private final String resourceId;
    // Example URL: https://api.github.com/repos/dkim19375/dkim19375Core/releases/latest
    private final URL url;
    private final JavaPlugin plugin;

    public UpdateChecker(final @NotNull String resourceId, final @NotNull URL url, final @NotNull JavaPlugin plugin) {
        this.resourceId = resourceId;
        this.url = url;
        this.plugin = plugin;
    }

    public UpdateChecker(final @NotNull String resourceId, final @NotNull JavaPlugin plugin) {
        this.resourceId = resourceId;
        url = null;
        this.plugin = plugin;
    }

    public UpdateChecker(final @NotNull URL url, final @NotNull JavaPlugin plugin) {
        resourceId = null;
        this.url = url;
        this.plugin = plugin;
    }

    public void getSpigotVersion(final @NotNull Consumer<String> version, final @NotNull Consumer<IOException> exception) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
                Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    version.accept(scanner.next());
                }
            } catch (IOException e) {
                exception.accept(e);
            }
        });
    }
    public void getFromRaw(final @NotNull Consumer<String> version, final @NotNull Consumer<IOException> exception) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = url.openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    version.accept(scanner.next());
                }
            } catch (IOException e) {
                exception.accept(e);
            }
        });
    }

    public void getFromGithubJson(final @NotNull Consumer<String> version, final @NotNull Consumer<IOException> exception) {
        JsonUtils.getJson((j) -> {
            JsonElement element = j.get("tag_name");
            String tag = element.getAsString();
            version.accept(tag);
        }, exception, url, plugin);
    }
}

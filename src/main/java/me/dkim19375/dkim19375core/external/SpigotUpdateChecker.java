package me.dkim19375.dkim19375core.external;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class SpigotUpdateChecker {
    private final String resourceId;
    private final JavaPlugin plugin;

    public SpigotUpdateChecker(final String resourceId, final JavaPlugin plugin) {
        this.resourceId = resourceId;
        this.plugin = plugin;
    }

    public void getVersion(final Consumer<String> consumer, final Consumer<IOException> exception) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException e) {
                exception.accept(e);
            }
        });
    }
}

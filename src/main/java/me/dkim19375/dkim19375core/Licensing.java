package me.dkim19375.dkim19375core;

import me.dkim19375.dkim19375core.consumers.TriConsumer;
import me.dkim19375.dkim19375core.external.UpdateChecker;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Licensing {
    public static IOException e;
    public static boolean v;
    public static void validate(CoreJavaPlugin plugin, TriConsumer<Boolean, CoreJavaPlugin, IOException> validated) throws MalformedURLException {
        UpdateChecker c = new UpdateChecker(new URL("https://gist.githubusercontent.com/dkim19375/922e76c0c5797371ff7e1c37fe8e339a/raw/"), plugin);
        c.getFromRaw(v -> {
            switch (v.toLowerCase()) {
                case "true":
                    Licensing.v = true;
                    validated.accept(true, plugin, null);
                    return;
                case "false":
                    Licensing.v = false;
                    validated.accept(false, plugin, null);
                    return;
                default:
                    e = new IOException();
                    validated.accept(null, plugin, e);
                    stop(plugin, e);
            }
        }, e -> {
            Licensing.e = e;
            validated.accept(null, plugin, e);
        });
    }

    public static void stop(CoreJavaPlugin plugin, IOException e) {
        e.printStackTrace();
        plugin.printToConsole(ChatColor.RED + "Please contact dkim19375, as this cannot verify this license!");
    }
}
package me.dkim19375.dkim19375core.external;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsonUtils {
    private JsonUtils() {

    }

    public static void getJson(final @NotNull Consumer<JsonObject> consumer, final @NotNull Consumer<IOException> exception, final @NotNull URL url, final @NotNull JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String string;
            try {
                InputStream inputStream = url.openStream();
                //noinspection UnstableApiUsage
                byte[] byteArray = ByteStreams.toByteArray(inputStream);
                string = new String(byteArray);
            } catch (IOException e) {
                exception.accept(e);
                return;
            }
            final JsonParser parse = new JsonParser();
            final JsonElement json = parse.parse(string);
            final JsonObject jsonObject = json.getAsJsonObject();
            consumer.accept(jsonObject);
        });
    }

    public static void getJson(final @NotNull Consumer<JsonObject> consumer, final @NotNull Consumer<IOException> exception, final @NotNull String url, final @NotNull JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String string;
            try {
                InputStream inputStream = new URL(url).openStream();
                //noinspection UnstableApiUsage
                byte[] byteArray = ByteStreams.toByteArray(inputStream);
                string = new String(byteArray);
            } catch (IOException e) {
                exception.accept(e);
                return;
            }
            final JsonParser parse = new JsonParser();
            final JsonElement json = parse.parse(string);
            final JsonObject jsonObject = json.getAsJsonObject();
            consumer.accept(jsonObject);
        });
    }
}

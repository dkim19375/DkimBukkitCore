package me.dkim19375.dkim19375core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CoreJavaPlugin extends JavaPlugin {
    private static final Logger logger = Bukkit.getLogger();
    private final Set<ConfigFile> files = new HashSet<>();

    public static void log(Level level, String msg) { logger.log(level, msg); }

    @SuppressWarnings("unused")
    public static void log(String msg) { log(Level.INFO, msg); }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        files.forEach(this::reloadConfig);
    }

    public void reloadConfig(ConfigFile config) {
        config.saveDefaultConfig();
        config.reload();
    }

    public void registerCommand(@NotNull String command, @NotNull CommandExecutor executor, @Nullable TabCompleter tabCompleter) {
        final PluginCommand cmd = getCommand(command);
        if (cmd == null) {
            logger.log(Level.SEVERE, "Could not register command: " + command + "!");
            getServer().getPluginManager().disablePlugin(this);
            throw new NullPointerException("[" + getDescription().getName() + "] Could not register command: " + command + "!");
        }
        cmd.setExecutor(executor);
        if (tabCompleter != null) {
            cmd.setTabCompleter(tabCompleter);
        }
    }

    @SuppressWarnings("unused")
    public void registerCommand(@NotNull String command, @NotNull CommandExecutor executor) {
        registerCommand(command, executor, null);
    }

    @SuppressWarnings("unused")
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @SuppressWarnings("unused")
    public void registerConfig(ConfigFile config) {
        unregisterConfig(config);
        files.add(config);
        reloadConfig(config);
    }

    public void unregisterConfig(ConfigFile config) {
        unregisterConfig(config.getName());
    }

    public void unregisterConfig(String config) {
        new HashSet<>(files).forEach(file -> {
            if (file.getName().equals(config)) {
                files.remove(file);
            }
        });
    }

    public Set<ConfigFile> getRegisteredFiles() {
        return files;
    }
}
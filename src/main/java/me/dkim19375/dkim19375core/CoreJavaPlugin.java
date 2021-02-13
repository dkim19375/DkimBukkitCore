package me.dkim19375.dkim19375core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CoreJavaPlugin extends JavaPlugin {
    private static final Logger logger = Bukkit.getLogger();

    public static void log(Level level, String msg) {
        logger.log(level, msg);
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
    }
}
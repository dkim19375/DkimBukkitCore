package me.dkim19375.dkim19375core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CoreJavaPlugin extends JavaPlugin {
    private final Logger logger = Bukkit.getLogger();

    public void printToConsole(String msg) {
        getServer().getConsoleSender().sendMessage("[" + getDescription().getName() + "] " + msg);
    }

    public void log(Level level, String msg) {
        logger.log(level, msg);
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();
}
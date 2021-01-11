package me.dkim19375.dkim19375core;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class CoreJavaPlugin extends JavaPlugin {
    public void printToConsole(String msg) {
        getServer().getConsoleSender().sendMessage("[" + getDescription().getName() + "] " + msg);
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();
}
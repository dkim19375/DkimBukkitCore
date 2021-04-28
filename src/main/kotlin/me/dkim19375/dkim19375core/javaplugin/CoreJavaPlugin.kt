package me.dkim19375.dkim19375core.javaplugin

import me.dkim19375.dkim19375core.annotation.API
import me.dkim19375.dkim19375core.config.ConfigFile
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer
import java.util.logging.Level

@API
abstract class CoreJavaPlugin : JavaPlugin() {
    private val files: MutableSet<ConfigFile> = mutableSetOf()

    override fun reloadConfig() {
        saveDefaultConfig()
        super.reloadConfig()
        files.forEach { config: ConfigFile ->
            this.reloadConfig(
                config
            )
        }
    }

    @API
    fun reloadConfig(config: ConfigFile) {
        config.saveDefaultConfig()
        config.reload()
    }

    @API
    fun registerCommand(command: String, executor: CommandExecutor, tabCompleter: TabCompleter?) {
        val cmd = getCommand(command)
            ?: throw IllegalStateException("[" + description.name + "] Could not register command: " + command + "!")
        cmd.setExecutor(executor)
        tabCompleter ?: return
        cmd.tabCompleter = tabCompleter
    }

    @API
    fun registerCommand(command: String, executor: CommandExecutor) = registerCommand(command, executor, null)

    @API
    fun registerListener(vararg listener: Listener) = listener.forEach {
        server.pluginManager.registerEvents(it, this)
    }

    @API
    fun registerConfig(config: ConfigFile) {
        unregisterConfig(config)
        files.add(config)
        reloadConfig(config)
    }

    @API
    fun unregisterConfig(config: ConfigFile) = unregisterConfig(config.fileName)

    @API
    fun unregisterConfig(configFileName: String) {
        HashSet(files).forEach(Consumer { file: ConfigFile ->
            if (file.fileName == configFileName) {
                files.remove(file)
            }
        })
    }

    @API
    fun getRegisteredFiles(): Set<ConfigFile> {
        return files
    }
}
/*
 * MIT License
 *
 * Copyright (c) 2021 dkim19375
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.dkim19375.dkimbukkitcore.javaplugin

import java.io.File
import me.dkim19375.dkimbukkitcore.function.setLoggingPlugin
import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.file.DataFile
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

@API
abstract class CoreJavaPlugin : JavaPlugin() {
    private val files = mutableSetOf<DataFile>()
    open val defaultConfig = true

    init {
        @Suppress("LeakingThis") setLoggingPlugin(this)
    }

    override fun reloadConfig() {
        if (defaultConfig) {
            saveDefaultConfig()
            super.reloadConfig()
        }
        files.forEach(this::reloadConfig)
    }

    @API fun reloadConfig(config: DataFile) = config.reload()

    @API
    fun registerCommand(command: String, executor: CommandExecutor, tabCompleter: TabCompleter?) {
        val cmd =
            getCommand(command)
                ?: throw IllegalStateException(
                    "[" + description.name + "] Could not register command: " + command + "!"
                )
        cmd.setExecutor(executor)
        tabCompleter ?: return
        cmd.tabCompleter = tabCompleter
    }

    @API
    fun registerCommand(command: String, executor: CommandExecutor) =
        registerCommand(command, executor, null)

    @API
    fun registerListener(vararg listener: Listener) =
        listener.forEach { server.pluginManager.registerEvents(it, this) }

    @API
    fun registerConfig(config: DataFile) {
        unregisterConfig(config)
        files.add(config)
        reloadConfig(config)
    }

    @API fun unregisterConfig(config: DataFile) = unregisterConfig(config.file)

    @API
    fun unregisterConfig(file: File) =
        files.toSet().forEach { dataFile: DataFile ->
            if (file == dataFile.file) {
                files.remove(dataFile)
            }
        }

    @API fun getRegisteredFiles(): Set<DataFile> = files.toSet()
}

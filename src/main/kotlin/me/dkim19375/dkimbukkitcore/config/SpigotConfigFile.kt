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

package me.dkim19375.dkimbukkitcore.config

import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.createFileAndDirs
import me.dkim19375.dkimcore.file.DataFile
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import kotlin.io.path.createDirectories

/**
 * @param plugin The [JavaPlugin] extending class
 * @param file The [File] of the config
 **/

@API
class SpigotConfigFile(
    private val plugin: JavaPlugin,
    file: File,
) : DataFile(file) {
    @API
    var config: FileConfiguration
        private set
    private val parentFolder: File
        get() = file.parentFile

    init {
        saveDefaultConfig()
        config = YamlConfiguration.loadConfiguration(this.file)
    }

    /**
     * This creates the configuration file. If the data folder is invalid, it will be created along with the config file.
     *
     * @return true if and only if the file was created
     */
    @API
    fun createConfig(): Boolean = (parentFolder.exists() != parentFolder.mkdirs()) && parentFolder.createNewFile()

    @API
    fun addIfDoesntExist(key: String, value: Any) {
        if (config.get(key) == null) {
            config[key] = value
        }
    }

    /**
     * @since 1.0.0
     * @param defaults A map containing the default configuration keys and values.
     * This sets the default configuration values as the ones contained in the map.
     */
    @API
    fun addDefaults(defaults: Map<String, Any>) = config.addDefaults(defaults)

    /**
     * @since 1.0.0
     * This saves the configuration file. Saving is required every time you write to it.
     */
    @API
    @Synchronized
    override fun save() {
        super.save()
        config.save(file)
    }

    /**
     * @since 1.0.0
     * This reloads the configuration file, making Java acknowledge and load the new config and its values.
     */
    @API
    @Synchronized
    override fun reload() {
        saveDefaultConfig()
        super.reload()
        config = YamlConfiguration.loadConfiguration(file)
    }

    /**
     * @since 1.0.0
     * @return true if and only if the file or directory is
     * successfully deleted; otherwise
     * This deletes the config file.
     */
    @API
    fun deleteFile(): Boolean = file.delete()

    /**
     * @since 1.0.0
     * @throws IOException in case deletion is unsuccessful
     * This deletes the config file's directory and all it's contents.
     */
    @API
    fun deleteDir(): Boolean = parentFolder.deleteRecursively()

    /**
     * @since 1.0.0
     * @return true if the file was successfully reset
     * This deletes and recreates the file, wiping all its contents.
     */
    @API
    fun reset() {
        deleteFile()
        file.createFileAndDirs()
    }

    /**
     * @since 1.0.0
     * @return true if the directory was successfully wiped
     * Wipe the config file's directory, including the file itself.
     */
    @API
    fun wipeDirectory() {
        deleteDir()
        parentFolder.toPath().createDirectories()
    }

    @API
    fun createSubDirectory(name: String) {
        parentFolder.toPath().createDirectories()
        val subDir = parentFolder.resolve(name).toPath()
        subDir.createDirectories()
    }

    /**
     * This returns true if the config contains the given value, even if it has a default value.
     */
    @API
    fun containsIgnoreDefault(value: String): Boolean = config.isSet(value)

    private fun getResource(): InputStream? =
        plugin.javaClass.classLoader.getResourceAsStream(file.name) ?: plugin.javaClass.getResourceAsStream(file.name)

    @API
    fun saveDefaultConfig() {
        if (!file.exists()) {
            saveResource()
        }
    }

    private fun saveResource() {
        val resource = getResource()
        if (resource == null) {
            path.createFileAndDirs()
            return
        }
        path.parent.createDirectories()
        Files.copy(resource, path)
    }

    /**
     * This returns true if the config contains the given value OR if it has a default value.
     */
    operator fun contains(path: String): Boolean = path in config

    operator fun set(path: String, value: Any) = config.set(path, value)

    inline operator fun <reified V> get(path: String, default: V? = null): V? = config.get(path, default) as? V
}
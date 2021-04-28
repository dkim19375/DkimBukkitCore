package me.dkim19375.dkim19375core.config

import me.dkim19375.dkim19375core.annotation.API
import me.dkim19375.dkim19375core.javaplugin.CoreJavaPlugin
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.*
import java.util.logging.Level
import kotlin.math.max

/**
 * @param plugin The CoreJavaPlugin extending class
 * @param fileName The name of the config file excluding file extensions.
 *
 * Please notice that the constructor does not yet create the YAML-configuration file. To create the file on the disk, use [ConfigFile.createConfig].
 **/

@API
class ConfigFile(private val plugin: CoreJavaPlugin, val fileName: String) {
    private val configFile: File

    @API
    var config: FileConfiguration
        private set
    private val pluginDataFolder: File

    init {
        var fileName = fileName
        fileName = fileName.replace('\\', '/')
        configFile = File(plugin.dataFolder, fileName)
        pluginDataFolder = plugin.dataFolder
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    /**
     * This creates the configuration file. If the data folder is invalid, it will be created along with the config file.
     *
     * @return true if the file was successfully created
     */
    @API
    fun createConfig(): Boolean {
        var success = false
        if (!configFile.exists()) {
            if (!pluginDataFolder.exists() && pluginDataFolder.mkdir()) {
                success = true
            }
            try {
                if (configFile.createNewFile()) {
                    success = true
                }
            } catch (e: IOException) {
                e.printStackTrace()
                success = false
            }
        }
        return success
    }

    /**
     * @since 1.0.0
     * @param key The config key, including the path.
     * @param value the config value.
     * Set a default configuration value: if the entered key already exists (and it holds another value), it will do nothing. If it doesn't, it will create the key with the wanted value.
     */
    @API
    fun addDefault(key: String, value: String?) {
        if (config.getString(key) == null) {
            config[key] = value
            try {
                config.save(configFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * @since 1.0.0
     * @param defaults A map containing the default configuration keys and values.
     * This sets the default configuration values as the ones contained in the map.
     */
    @API
    fun addDefaults(defaults: Map<String, Any>) {
        config.addDefaults(defaults)
    }

    /**
     * @since 1.0.0
     * This saves the configuration file. Saving is required every time you write to it.
     */
    @API
    fun save() {
        try {
            config.save(configFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * @since 1.0.0
     * This reloads the configuration file, making Java acknowledge and load the new config and its values.
     */
    @API
    fun reload() {
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    /**
     * @since 1.0.0
     * @return true if and only if the file or directory is
     * successfully deleted; otherwise
     * This deletes the config file.
     */
    @API
    fun deleteFile(): Boolean {
        return configFile.delete()
    }

    /**
     * @since 1.0.0
     * @throws IOException           in case deletion is unsuccessful
     * This deletes the config file's directory and all it's contents.
     */
    @API
    fun deleteDir(): Boolean {
        return try {
            FileUtils.forceDelete(pluginDataFolder)
            true
        } catch (_: FileNotFoundException) {
            true
        } catch (e: IOException) {
            throw e
        }
    }

    /**
     * @since 1.0.0
     * @return true if the file was successfully reset
     * This deletes and recreates the file, wiping all its contents.
     */
    @API
    fun reset(): Boolean {
        deleteFile()
        return try {
            configFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @since 1.0.0
     * @return true if the directory was successfully wiped
     * Wipe the config file's directory, including the file itself.
     */
    @API
    fun wipeDirectory(): Boolean {
        var success = true
        if (!pluginDataFolder.delete()) {
            success = false
        }
        if (!pluginDataFolder.mkdir()) {
            success = false
        }
        return success
    }

    /**
     * @since 1.0.0
     * @param name The sub directory's name.
     * @return true if and only if the file or directory is
     * successfully deleted; otherwise
     * @throws IOException If the entered string has a file extension or already exists.
     * This will create a sub-directory in the plugin's data folder, which can be accessed with [ConfigFile.pluginDataFolder]
     * If the entered name is not a valid name for a directory or the sub-directory already exists or the data folder does not exist, an IOException will be thrown.
     */
    @API
    fun createSubDirectory(name: String): Boolean {
        if (!pluginDataFolder.exists()) {
            throw IOException("Data folder not found.")
        }
        val subDir = File(pluginDataFolder, name)
        if (subDir.exists()) {
            throw IOException("Sub directory already existing.")
        }
        if (!subDir.isDirectory) {
            throw IOException("The first argument is not a directory.")
        }
        return subDir.mkdir()
    }

    /**
     * @since 1.0.0
     * @param value - Check if it contains the string
     * @return true or false
     * This returns true if the config contains the given value.
     */
    operator fun contains(value: String): Boolean {
        return config.contains(value)
    }

    fun contains(value: String, b: Boolean): Boolean {
        return config.contains(value, b)
    }

    private fun getResource(filename: String): InputStream? {
        return try {
            val url = plugin.javaClass.classLoader.getResource(filename) ?: return null
            val connection = url.openConnection()
            connection.useCaches = false
            connection.getInputStream()
        } catch (ex: IOException) {
            null
        }
    }

    @API
    fun saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource()
        }
    }

    private fun saveResource() {
        require(fileName != "") { "ResourcePath cannot be null or empty" }
        val `in` = getResource(fileName)
            ?: throw IllegalArgumentException("The embedded resource '$fileName' cannot be found in $fileName")
        val outFile = File(pluginDataFolder, fileName)
        val lastIndex = fileName.lastIndexOf('/')
        val outDir = File(pluginDataFolder, fileName.substring(0, max(lastIndex, 0)))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        val logger = Bukkit.getLogger()
        try {
            if (!outFile.exists()) {
                val out: OutputStream = FileOutputStream(outFile)
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                out.close()
                `in`.close()
            } else {
                logger.log(
                    Level.WARNING,
                    "Could not save " + outFile.name + " to " + outFile + " because " + outFile.name + " already exists."
                )
            }
        } catch (ex: IOException) {
            logger.log(Level.SEVERE, "Could not save " + outFile.name + " to " + outFile, ex)
        }
    }
}
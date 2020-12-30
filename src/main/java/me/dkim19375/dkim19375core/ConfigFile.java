package me.dkim19375.dkim19375core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ConfigFile {

    private final File configFile;
    private FileConfiguration config;
    private final File pluginDataFolder;
    private final String fileName;

    /**
     * @since 1.0.0
     * @param pluginDataFolder The plugin's data directory, accessible with JavaPlugin#getDataFolder();
     * @param fileName The name of the config file excluding file extensions.
     *
     * Please notice that the constructor does not yet create the YAML-configuration file. To create the file on the disk, use {@link ConfigFile#createConfig()}.
     */
    public ConfigFile(File pluginDataFolder, String fileName) {
        this.fileName = fileName;
        configFile = new File(pluginDataFolder, fileName);
        this.pluginDataFolder = pluginDataFolder;
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * @since 1.0.0
     * @param javaPlugin A JavaPlugin instance of the plugin using this config
     * @param fileName The name of the config file excluding file extensions.
     *
     * Please notice that the constructor does not yet create the YAML-configuration file. To create the file on the disk, use {@link ConfigFile#createConfig()}.
     */
    public ConfigFile(JavaPlugin javaPlugin, String fileName) {
        this.fileName = fileName;
        configFile = new File(javaPlugin.getDataFolder(), fileName);
        this.pluginDataFolder = javaPlugin.getDataFolder();
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * @since 1.0.0
     * This creates the configuration file. If the data folder is invalid, it will be created along with the config file.
     */
    public void createConfig() {
        if (! configFile.exists()) {
            if (! this.pluginDataFolder.exists()) {
                this.pluginDataFolder.mkdir();
            }
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @since 1.0.0
     * @return The configuration file's directory. To get its name, use {@link ConfigFile#getName()} instead.
     */
    public File getDirectory() {
        return pluginDataFolder;
    }

    /**
     * @since 1.0.0
     * @return The name of the configuration file, including file extensions.
     * This returns the name of the configuration file with the .yml extension. To get the file's directory, use {@link ConfigFile#getDirectory()}.
     */
    public String getName() {
        return fileName;
    }

    /**
     * @since 1.0.0
     * @return The config file.
     * This returns the actual File object of the config file.
     */
    public File getFile() {
        return configFile;
    }

    /**
     * @since 1.0.0
     * @return The FileConfiguration object.
     * This returns the actual FileConfiguration object of the config file.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * @since 1.0.0
     * @param key The config key, including the path.
     * @param value the config value.
     * Set a default configuration value: if the entered key already exists (and it holds another value), it will do nothing. If it doesn't, it will create the key with the wanted value.
     */
    public void addDefault(String key, String value) {
        if (config.getString(key) == null) {
            config.set(key, value);
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @since 1.0.0
     * @param defaults A map containing the default configuration keys and values.
     * This sets the default configuration values as the ones contained in the map.
     */
    public void addDefaults(Map<String,Object> defaults) {
        config.addDefaults(defaults);
    }

    /**
     * @since 1.0.0
     * This saves the configuration file. Saving is required every time you write to it.
     */
    public void save() {
        try {
            config.save(configFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 1.0.0
     * This reloads the configuration file, making Java acknowledge and load the new config and its values.
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * @since 1.0.0
     * @return true if and only if the file or directory is
     *      successfully deleted; otherwise
     * This deletes the config file.
     */
    public boolean deleteFile() {
        return configFile.delete();
    }

    /**
     * @since 1.0.0
     * This deletes the config file's directory and all it's contents.
     */
    public boolean deleteDir() {
        return getDirectory().delete();
    }

    /**
     * @since 1.0.0
     * This deletes and recreates the file, wiping all its contents.
     */
    public void reset() {
        this.deleteFile();
        try {
            configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 1.0.0
     * Wipe the config file's directory, including the file itself.
     */
    public void wipeDirectory() {
        this.getDirectory().delete();
        this.pluginDataFolder.mkdir();
    }

    /**
     * @since 1.0.0
     * @param name The sub directory's name.
     * @return true if and only if the file or directory is
     *      successfully deleted; otherwise
     * @throws IOException If the entered string has a file extension or already exists.
     * This will create a sub-directory in the plugin's data folder, which can be accessed with {@link ConfigFile#getDirectory()}.
     * If the entered name is not a valid name for a directory or the sub-directory already exists or the data folder does not exist, an IOException will be thrown.
     */
    public boolean createSubDirectory(String name) throws IOException {
        if (!pluginDataFolder.exists()) {
            throw new IOException("Data folder not found.");
        }
        File subDir = new File(pluginDataFolder, name);
        if (subDir.exists()) {
            throw new IOException("Sub directory already existing.");
        }
        if (!subDir.isDirectory()) {
            throw new IOException("The first argument is not a directory.");
        }
        return subDir.mkdir();
    }

    /**
     * @since 1.0.0
     * @param value - Check if it contains the string
     * @return true or false
     * This returns true if the config contains the given value.
     */
    public boolean contains(String value) {
        return config.contains(value);
    }

    public boolean contains(String value, boolean b) {
        return config.contains(value, b);
    }

}
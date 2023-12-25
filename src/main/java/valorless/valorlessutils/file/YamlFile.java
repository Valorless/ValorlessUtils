package valorless.valorlessutils.file;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFile extends FileStorage {

    // YamlConfiguration instance associated with this YamlFile
    private YamlConfiguration config;

    /**
     * Constructor for YamlFile class using a file path.
     * @param file The file path as a string.
     */
    public YamlFile(String file) {
        this(new File(file));
    }

    /**
     * Constructor for YamlFile class using a File object.
     * @param file The File object representing the YAML file.
     */
    public YamlFile(File file) {
        super(file);
        reload();
    }

    /**
     * Reloads the YamlConfiguration from the associated file.
     */
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(getFile());
    }

    /**
     * Sets a value in the YamlConfiguration.
     * @param path The path to the value.
     * @param value The value to set.
     */
    public void set(String path, Object value) {
        this.config.set(path, value);
    }

    /**
     * Gets a value from the YamlConfiguration.
     * @param path The path to the value.
     * @return The value at the specified path.
     */
    public Object get(String path) {
        return this.config.get(path);
    }

    /**
     * Gets a typed value from the YamlConfiguration.
     * @param path The path to the value.
     * @param clazz The class of the value.
     * @param <T> The type of the value.
     * @return The typed value at the specified path.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String path, Class<T> clazz) {
        return (T) this.config.get(path);
    }

    /**
     * Checks if the YamlConfiguration contains a value at the specified path.
     * @param path The path to check.
     * @return true if the path exists, false otherwise.
     */
    public boolean contains(String path) {
        return this.config.contains(path);
    }

    /**
     * Sets a value in the YamlConfiguration only if the path does not already exist.
     * @param path The path to the value.
     * @param value The value to set.
     */
    public void setIfNull(String path, Object value) {
        if (!contains(path))
            set(path, value);
    }

    /**
     * Gets a value from the YamlConfiguration or a default value if the path does not exist.
     * @param path The path to the value.
     * @param defaultValue The default value to return if the path does not exist.
     * @param <T> The type of the value.
     * @return The value at the specified path or the default value if the path does not exist.
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String path, T defaultValue) {
        if (contains(path))
            return (T) get(path);
        return defaultValue;
    }

    /**
     * Gets a ConfigurationSection from the YamlConfiguration.
     * @param path The path to the ConfigurationSection.
     * @return The ConfigurationSection at the specified path.
     */
    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    /**
     * Saves the YamlConfiguration to the associated file.
     * @return true if the save is successful, false otherwise.
     */
    public boolean save() {
        try {
            this.config.save(getFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the underlying YamlConfiguration.
     * @return The YamlConfiguration instance.
     */
    public YamlConfiguration getConfig() {
        return config;
    }
}

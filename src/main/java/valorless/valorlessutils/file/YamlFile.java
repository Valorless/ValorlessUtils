package valorless.valorlessutils.file;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFile extends FileStorage {

    private YamlConfiguration config;

    public YamlFile(String file) {
        this(new File(file));
    }

    public YamlFile(File file) {
        super(file);
        reload();
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(getFile());
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    @SuppressWarnings("unchecked")
	public <T> T get(String path, Class<T> clazz) {
        return (T) this.config.get(path);
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public void setIfNull(String path, Object value) {
        if (!contains(path))
            set(path, value);
    }

    @SuppressWarnings("unchecked")
	public <T> T getOrDefault(String path, T defaultValue) {
        if (contains(path))
            return (T) get(path);
        return defaultValue;
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public boolean save() {
        try {
            this.config.save(getFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}

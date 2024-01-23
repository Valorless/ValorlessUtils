package valorless.valorlessutils.config;

import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.utils.Utils;
import valorless.valorlessutils.file.YamlFile;
import valorless.valorlessutils.types.Vector2;
import valorless.valorlessutils.types.Vector3;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private YamlFile file;
    private final JavaPlugin plugin;

    /**
     * Constructor for Config class.
     * @param plugin The JavaPlugin instance.
     * @param file The name of the configuration file.
     */
    public Config(JavaPlugin plugin, String file) {
        this.plugin = plugin;
        this.file = new YamlFile(new File(plugin.getDataFolder(), file));
        if (!this.file.fileExists()) {
            plugin.saveResource(file, false);
            this.file = new YamlFile(new File(plugin.getDataFolder(), file));
        }
        this.Validate();
    }

    // SET
    /**
     * Sets a key-value pair in the configuration file.
     * @param key The key to set.
     * @param value The value to set.
     */
    public void Set(String key, Object value) {
        this.file.getConfig().set(key, value);
    }
    
    /**
     * Sets a Vector2 value in the configuration file.
     * @param <T> The type of the vector components, which must be number.
     * @param key The key to set.
     * @param value The Vector2 value to set.
     */
	public <T extends Number> void SetVector2(String key, Vector2<T> value) {
    	Set(key + ".x", value.x);
    	Set(key + ".y", value.y);
    }
    
    /**
     * Sets a Vector3 value in the configuration file.
     * @param <T> The type of the vector components, which must be number.
     * @param key The key to set.
     * @param value The Vector3 value to set.
     */
	public <T extends Number> void SetVector3(String key, Vector3<T> value) {
    	Set(key + ".x", value.x);
    	Set(key + ".y", value.y);
    	Set(key + ".z", value.z);
    }
    
    // GET
    /**
     * Retrieves a string value from the configuration file.
     * @param key The key to retrieve.
     * @return The string value.
     */
    public String GetString(String key) {
        return this.file.getConfig().getString(key);
    }
    
    /**
     * Retrieves a boolean value from the configuration file.
     * @param key The key to retrieve.
     * @return The boolean value.
     */
    public Boolean GetBool(String key) {
        return this.file.getConfig().getBoolean(key);
    }
    
    /**
     * Retrieves an integer value from the configuration file.
     * @param key The key to retrieve.
     * @return The integer value.
     */
    public Integer GetInt(String key) {
        return this.file.getConfig().getInt(key);
    }
    
    /**
     * Retrieves a float value from the configuration file.
     * @param key The key to retrieve.
     * @return The float value.
     */
    public Double GetFloat(String key) {
        return this.file.getConfig().getDouble(key);
    }
    
    /**
     * Retrieves a Vector2 value from the configuration file.
     * @param <T> The type of the vector components, which must be number.
     * @param key The key to retrieve.
     * @return The Vector2 value.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Number> Vector2<T> GetVector2(String key) {
    	return new Vector2((T)this.file.getConfig().get(key + ".x"), (T)this.file.getConfig().get(key + ".y"));
    }
    
    /**
     * Retrieves a Vector3 value from the configuration file.
     * @param <T> The type of the vector components, which must be number.
     * @param key The key to retrieve.
     * @return The Vector3 value.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Number> Vector3<T> GetVector3(String key) {
    	return new Vector3((T)this.file.getConfig().get(key + ".x"),
    			(T)this.file.getConfig().get(key + ".y"),
    			(T)this.file.getConfig().get(key + ".z"));
    }
    
    /**
     * Retrieves a Material value from the configuration file.
     * @param key The key to retrieve.
     * @return The Material value.
     */
    public Material GetMaterial(String key) {
        return Material.getMaterial(GetString(key));
    }
    
    /**
     * Retrieves a generic object from the configuration file.
     * @param key The key to retrieve.
     * @return The object value.
     */
    public Object Get(String key) {
        return this.file.getConfig().get(key);
    }
    
    /**
     * Retrieves a list of strings from the configuration file.
     * @param key The key to retrieve.
     * @return The list of strings.
     */
    public List<String> GetStringList(String key) {
        return this.file.getConfig().getStringList(key);
    }
    
    /**
     * Retrieves a list of integers from the configuration file.
     * @param key The key to retrieve.
     * @return The list of integers.
     */
    public List<Integer> GetIntList(String key) {
        return this.file.getConfig().getIntegerList(key);
    }
    
    /**
     * Retrieves a list of doubles from the configuration file.
     * @param key The key to retrieve.
     * @return The list of doubles.
     */
    public List<Double> GetDoubleList(String key) {
        return this.file.getConfig().getDoubleList(key);
    }
    
    /**
     * Retrieves a generic list from the configuration file.
     * @param key The key to retrieve.
     * @return The generic list.
     */
    public List<?> GetList(String key) {
        return this.file.getConfig().getList(key);
    }
    
    /**
     * Checks if a key exists in the configuration file.
     * @param key The key to check for.
     * @return true if the key exists, false otherwise.
     */
    public Boolean HasKey(String key) {
        if (Utils.IsStringNullOrEmpty(key)) {
            Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.HasKey() was called with a null or empty key!");
            return null;
        }
        return this.file.getConfig().contains(key, true);
    }
    
    /**
     * Retrieves a ConfigurationSection from the configuration file.
     * @param key The key to retrieve.
     * @return The ConfigurationSection.
     */
    public ConfigurationSection GetConfigurationSection(String key) {
        if (Utils.IsStringNullOrEmpty(key)) {
            Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.HasKey() was called with a null or empty key!");
            return null;
        }
        return this.file.getSection(key);
    }
    
    /**
     * Retrieves the YamlFile instance.
     * @return The YamlFile instance.
     */
    public YamlFile GetFile() {
        return this.file;
    }

    /**
     * Reloads the configuration file.
     */
    public void Reload() {
        this.file.reload();
        this.Validate();
    }
    
    /**
     * Saves the configuration file.
     */
    public void SaveConfig() {
        this.file.save();
    }
    
    /**
     * Inner class to represent a key-value pair for configuration validation.
     */
    public class ValidationListEntry {
        public String key;
        public Object defaultValue;
        
        /**
         * Constructor for ValidationListEntry.
         * @param key The key to validate.
         * @param defaultValue The default value for the key.
         */
        public ValidationListEntry(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
    }
    
    List<ValidationListEntry> validationList = new ArrayList<ValidationListEntry>();
    
    /**
     * Adds a key-value pair to the validation list.
     * @param key The key to add.
     * @param value The default value for the key.
     */
    public void AddValidationEntry(String key, Object value) {
        this.validationList.add(new ValidationListEntry(key, value));
    }
    
    /**
     * Adds a key-value pair to the validation list.
     * @param key The key to add.
     * @param value The default value for the key.
     */
    public <T extends Number> void AddValidationEntry(String key, Vector2<T> value) {
        this.validationList.add(new ValidationListEntry(key + ".x", value.x));
        this.validationList.add(new ValidationListEntry(key + ".y", value.y));
    }
    
    /**
     * Adds a key-value pair to the validation list.
     * @param key The key to add.
     * @param value The default value for the key.
     */
    public <T extends Number> void AddValidationEntry(String key, Vector3<T> value) {
        this.validationList.add(new ValidationListEntry(key + ".x", value.x));
        this.validationList.add(new ValidationListEntry(key + ".y", value.y));
        this.validationList.add(new ValidationListEntry(key + ".z", value.z));
    }
    
    /**
     * Validates the configuration file against the validation list.
     */
    public void Validate() {
        Boolean missing = false;
        if (this.GetBool("debug")) {
            Log.Debug(plugin, "Validating Config");
        }
        
        for (ValidationListEntry item : this.validationList) {
            if (!this.HasKey(item.key)) { 
                Log.Warning(plugin, "Config value '" + item.key + "' is missing, fixing.");
                this.Set(item.key, item.defaultValue);
                missing = true; 
            }
        }
        
        if (missing) { 
            this.SaveConfig(); 
        }
    }
}

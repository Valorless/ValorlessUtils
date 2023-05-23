package valorless.valorlessutils.config;

import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.ValorlessUtils.Utils;
import valorless.valorlessutils.file.YamlFile;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private YamlFile file;
    private final JavaPlugin plugin;

    public Config(JavaPlugin plugin, String file) {
        this.plugin = plugin;
        this.file = new YamlFile(new File(plugin.getDataFolder(), file));
        if(!this.file.fileExists()) {
        	plugin.saveResource(file, false);
        	this.file = new YamlFile(new File(plugin.getDataFolder(), file));
    	}
        this.Validate();
    }

    // SET
    public void Set(String key, Object value) {
        this.file.getConfig().set(key, value);
    }
    
    // GET
    public String GetString(String key) {
        return this.file.getConfig().getString(key);
    }
    
    public Boolean GetBool(String key) {
        return this.file.getConfig().getBoolean(key);
    }
    
    public Integer GetInt(String key) {
        return this.file.getConfig().getInt(key);
    }
    
    public Double GetFloat(String key) {
        return this.file.getConfig().getDouble(key);
    }
    
    public Material GetMaterial(String key) {
    	return Material.getMaterial(GetString(key));
    }
    
    public Object Get(String key) {
        return this.file.getConfig().get(key);
    }
    
    public List<String> GetStringList(String key) {
        return this.file.getConfig().getStringList(key);
    }
    
    public List<Integer> GetIntList(String key) {
        return this.file.getConfig().getIntegerList(key);
    }
    
    public List<Double> GetDoubleList(String key) {
        return this.file.getConfig().getDoubleList(key);
    }
    
    public List<?> GetList(String key) {
        return this.file.getConfig().getList(key);
    }
    
    public Boolean HasKey(String key) {
		if (Utils.IsStringNullOrEmpty(key)) { 
			Log.Error(plugin, "[ValorlessUtils] " + plugin.getName() + ".config.HasKey() was called with a null or empty key!"); 
			return null;
		}
    	return this.file.getConfig().contains(key, true);
    }

    public void Reload() {
        this.file.reload();
        this.Validate();
    }
    
    public void SaveConfig() {
    	this.file.save();
    }
    
    public class ValidationListEntry {
		public String key;
		public Object defaultValue;
		
		public ValidationListEntry(String key, Object defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
	}
	
	List<ValidationListEntry> validationList = new ArrayList<ValidationListEntry>();
	
	public void AddValidationEntry(String key, Object value) {
		this.validationList.add(new ValidationListEntry(key,value));
	}
	
	public void Validate() {
		Boolean missing = false;
		if(this.GetBool("debug")) { Log.Debug(plugin, "Validating Config"); }
		
		for(ValidationListEntry item : this.validationList) {
			if(!this.HasKey(item.key)) { 
    			Log.Warning(plugin, "Config value '" + item.key + "' is missing, fixing.");
    			this.Set(item.key, item.defaultValue);
    			missing = true; 
    		}
		}
		
		if(missing) { this.SaveConfig(); }
	}
}

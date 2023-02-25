package valorless.valorlessutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class ValorlessUtils extends JavaPlugin implements Listener {
	public static JavaPlugin thisPlugin;
	String Name = "§7[§6Valorless§bUtils§7]§r";
	
	public void onLoad() {
		thisPlugin = this;
	}
	
	public void YEET() {
		Log.Info(thisPlugin, "Fuck.");
	}
	
	@Override
    public void onEnable() {
		Config.Initiate(thisPlugin);
		
		Config.AddValidationEntry(thisPlugin, "debug", false);
		
		//getServer().getPluginManager().registerEvents(new PlacementBlocker(), this);
		//getServer().getPluginManager().registerEvents(new ArmorStands(), this);
		
		if(Config.GetBool(thisPlugin, "debug")) {
			Log.Info(thisPlugin, "Debugging enabled.");
		}
	
		if(Config.GetBool(thisPlugin, "debug")) {
			Log.Info(thisPlugin, "§aEnabled!");
		}
		
		AddCommand("valorlessutils", "vu");
	}
    
    @Override
    public void onDisable() {
    	if(Config.GetBool(thisPlugin, "debug")) {
    		Log.Info(thisPlugin, "§cDisabled!");
    	}
    }
    
    
    
    public static ValorlessUtils GetInstance() {
    	return (ValorlessUtils) thisPlugin;
    }

    
    public void AddCommand(String command, String...alias) {
    	getCommand(command).setExecutor(this);
		if(!Utils.IsStringNullOrEmpty(alias[0])) {
			getCommand(alias[0]).setExecutor(this);
		}
    }
    
 // Functions
    
    public static class Utils {
    	
        public static boolean IsStringNullOrEmpty(String string) {
    		if(string == null) return true;
    		else if(string.isEmpty()) return true;
    		else if(string.isBlank()) return true;
    		else if(string.trim().isEmpty()) return true;
    		else if(string.length() == 0) return true;
    		else return false;
    	}
    }
    
    public static class Log {
    	
    	public static void Info(JavaPlugin caller, String msg) {
    		Logger.getLogger("Minecraft").log(Level.INFO, "[" + caller.getName() + "] " + msg);
    	}
    	
    	public static void Warning(JavaPlugin caller, String msg) {
    		Logger.getLogger("Minecraft").log(Level.WARNING, "[" + caller.getName() + "] " + msg);
    	}

    	public static void Error(JavaPlugin caller, String msg) {
    		Logger.getLogger("Minecraft").log(Level.SEVERE, "[" + caller.getName() + "] " + msg);
    	}
    	
    	public static void Debug(JavaPlugin caller, String msg) {
    		if(Config.GetBool(thisPlugin, "debug") == true) {
    			Logger.getLogger("Minecraft").log(Level.WARNING, "[DEBUG]: [" + caller.getName() + "] " + msg);
    		}
    	}
    	
    }
    
    public static class Tags {

    	@SuppressWarnings({ "unchecked", "rawtypes" })
    	public static void Set(JavaPlugin caller, PersistentDataContainer container, String key, Object value, PersistentDataType type) {
    		container.set(new NamespacedKey(caller, key), type, value);
    	}
    	
    	@SuppressWarnings({ "unchecked", "rawtypes" })
    	public static Object Get(JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type) {
    		return container.get(new NamespacedKey(caller, key), type);
    	}
    	
    	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    	public static Boolean Has(JavaPlugin caller, PersistentDataContainer container, String key,PersistentDataType type) { //Dont be lazy and make this one already..
    		boolean result = false;
    		try {
    			container.has(new NamespacedKey(caller, key), type);
    		}
    		catch(Exception e) {
    			
    		}
    		finally {
    			
    		}
    		
    		return false;
    	}
    	
    }
    
    /*public static class Lang {
    	
    	// Example, new Placeholder(this, "%target%", "PlayerName");
    	public static class Placeholder {
    		public String plugin;
    		public String key;
    		public String value;
    		
    		public Placeholder(String plugin, String key, String value) {
    			this.plugin = plugin;
    			this.key = key;
    			this.value = value;
    		}
    	}
    	
    	static List<Placeholder> placeholders = new ArrayList<Placeholder>();
    	
    	public static void AddPlaceholder(JavaPlugin caller, String key, String value) {
    		if (caller == null) { Log.Error(thisPlugin, "Lang.AddPlaceholder() was called without a null caller!"); return; }
    		if (Utils.IsStringNullOrEmpty(key)) { Log.Debug(caller, "[ValorlessUtils] Lang.AddPlaceholder() was called without a null or empty key!"); return; }
    		if (Utils.IsStringNullOrEmpty(value)) { Log.Debug(caller, "[ValorlessUtils] Lang.AddPlaceholder() was called without a null or empty value!\nWas this intentional?"); }
    		for(int i = 0; i < placeholders.size(); i++){
    			Log.Error(caller, "[PH] " + placeholders.get(i).plugin + placeholders.get(i).key + placeholders.get(i).value);
    			if(placeholders.get(i).plugin == caller.getName()) {
    				Log.Error(caller, "[PH] " + placeholders.get(i).plugin + caller.getName());
    				if(placeholders.get(i).key == key) {
        				Log.Error(caller, "[PH] " + placeholders.get(i).key + key);
    					placeholders.set(i, new Placeholder(caller.getName(),key,value));
        				Log.Debug(caller, "Updated Placeholder " + placeholders.get(i).key + " - " + placeholders.get(i).value);
        				return;
        			}else {
        				placeholders.add(new Placeholder(caller.getName(),key,value));
        				Log.Debug(caller, "Added Placeholder " + placeholders.get(i).key + " - " + placeholders.get(i).value);
        				return;
        			}
    			} else {
    				placeholders.add(new Placeholder(caller.getName(),key,value));
    				Log.Debug(caller, "Added Placeholder " + placeholders.get(i).key + " - " + placeholders.get(i).value);
    				return;
    			}
    		}
    		if(placeholders.size() == 0) {
    			placeholders.add(new Placeholder(caller.getName(),key,value));
				Log.Debug(caller, "Added Placeholder " + key + " - " + value);
    		}
    	}
    	
    	public static String Parse(JavaPlugin caller, String text) {
    		if (caller == null) { Log.Error(thisPlugin, "Lang.Parse() was called without a null caller!"); return ""; }
    		if (Utils.IsStringNullOrEmpty(text)) { Log.Debug(caller, "[ValorlessUtils] Lang.Parse() was called without a null or empty text!\nWas this intentional?"); }
    		Log.Debug(caller, "[PARSE] " + text);
    		if(text.contains("&")) text = text.replace("&", "§");

    		for(Placeholder placeholder : placeholders) {
				//Log.Warning(caller, placeholder.plugin + placeholder.key + placeholder.value);
    			if(placeholder.plugin.equalsIgnoreCase(caller.getName())) {
    				//Log.Warning(caller, placeholder.plugin + placeholder.key + placeholder.value);
    				if(text.contains(placeholder.key)) { 
    					text = text.replace(placeholder.key, placeholder.value); 
    				}
    			}
    		}
    		
    		return text;
    	}

    	public static String Get(JavaPlugin caller, String key) {
    		if (caller == null) { Log.Error(thisPlugin, "Lang.Get() was called without a null caller!"); return ""; }
    		if (Utils.IsStringNullOrEmpty(key)) { Log.Error(caller, "[ValorlessUtils] Lang.Get() was called without a null or empty key!"); return ""; }
    		return Parse(caller, Config.GetString(caller, key));
    	}
    }*/
    
    public static class Config {
    	
    	public static void Initiate(JavaPlugin caller) {
    		if (caller == null) { Log.Error(thisPlugin, "Config.Initiate() was called without a null caller!"); return; }
    		Load(caller);
    	}
    	
    	public static void Load(JavaPlugin caller) {
    		if (caller == null) { Log.Error(thisPlugin, "Config.Load() was called without a null caller!"); return; }
        	File configFile = new File(caller.getDataFolder(), "config.yml");
        	if(!configFile.exists()) {
        		Log.Info(caller, "No config exists, creating new.");
        		caller.saveDefaultConfig();
        	}
        	else {
        		Log.Info(caller, "Loading config.");
        		caller.getConfig();
        		Validate(caller);
        		caller.saveConfig();
        	}
        }
    	
    	public static void Reload(JavaPlugin caller) {
    		caller.reloadConfig();
    		Validate(caller);
    	}
    	
    	public static Boolean HasKey(JavaPlugin caller, String key) {
    		if (caller == null) { Log.Error(thisPlugin, "Config.HasKey() was called without a null caller!"); return null; }
    		if (Utils.IsStringNullOrEmpty(key)) { Log.Error(caller, "[ValorlessUtils] Config.HasKey() was called without a null or empty key!"); return null; }
    		Boolean i = false;
    		if(caller.getConfig().contains(key, true)) {
    			i = true;
    		}
    		return i;
    	}
    	
    	
    	// GET
    	
    	public static String GetString(JavaPlugin caller, String key) {
    		String i = "";
    		i = caller.getConfig().getString(key);
    		return i;
    	}
    	
    	public static Boolean GetBool(JavaPlugin caller, String key) {
    		Boolean i = false;
    		i = caller.getConfig().getBoolean(key);
    		return i;
    	}
    	
    	public static Integer GetInt(JavaPlugin caller, String key) {
    		Integer i = 0;
    		i = caller.getConfig().getInt(key);
    		return i;
    	}
    	
    	public static Double GetFloat(JavaPlugin caller, String key) {
    		Double i = 0.0;
    		i = caller.getConfig().getDouble(key);
    		return i;
    	}
    	
    	public static Color GetColor(JavaPlugin caller, String key) {
    		Color i = Color.WHITE;
    		i = caller.getConfig().getColor(key);
    		return i;
    	}
    	
    	public static Object Get(JavaPlugin caller, String key) {
    		Object i = caller.getConfig().get(key);
    		return i;
    	}
    	
    	// SET
    	
    	public static void SetString(JavaPlugin caller, String key, String value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static void SetBool(JavaPlugin caller, String key, Boolean value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static void SetInt(JavaPlugin caller, String key, Integer value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static void SetFloat(JavaPlugin caller, String key, Double value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static void SetColor(JavaPlugin caller, String key, Color value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static void Set(JavaPlugin caller, String key, Object value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static class ValidationListEntry {
    		public JavaPlugin plugin;
    		public String key;
    		public Object defaultValue;
    		
    		public ValidationListEntry(JavaPlugin plugin, String key, Object defaultValue) {
    			this.plugin = plugin;
    			this.key = key;
    			this.defaultValue = defaultValue;
    		}
    	}
    	
    	static List<ValidationListEntry> validationList = new ArrayList<ValidationListEntry>();
    	
    	public static void AddValidationEntry(JavaPlugin caller, String key, Object value) {
    		validationList.add(new ValidationListEntry(caller,key,value));
    	}
    	
    	public static void Validate(JavaPlugin caller) {
    		Boolean missing = false;
    		if(GetBool(caller, "debug")) { Log.Debug(caller, "Validating Config"); }
    		
    		for(ValidationListEntry item : validationList) {
    			if(!HasKey(item.plugin, item.key)) { 
        			Log.Warning(item.plugin,"Config value '" + item.key + "' is missing, fixing.");
        			Set(item.plugin, item.key, item.defaultValue);
        			missing = true; 
        		}
    		}
    		
    		if(missing) { caller.saveConfig(); }
    	}
    }
}

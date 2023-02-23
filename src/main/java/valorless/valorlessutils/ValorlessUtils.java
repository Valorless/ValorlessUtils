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
    	
    }
    
    public static class Tags {

    	@SuppressWarnings({ "unchecked", "rawtypes" })
    	public static void Set(PersistentDataContainer container, String key, Object value, PersistentDataType type) {
    		container.set(new NamespacedKey(Bukkit.getPluginManager().getPlugin("SakuraHats"), key), type, value);
    	}
    	
    	@SuppressWarnings({ "unchecked", "rawtypes" })
    	public static Object Get(PersistentDataContainer container, String key, PersistentDataType type) {
    		return container.get(new NamespacedKey(Bukkit.getPluginManager().getPlugin("SakuraHats"), key), type);
    	}
    	
    	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    	public static boolean Has(PersistentDataContainer container, String key,PersistentDataType type) { //Dont be lazy and make this one already..
    		boolean result = false;
    		try {
    			container.has(new NamespacedKey(Bukkit.getPluginManager().getPlugin("SakuraHats"), key), type);
    		}
    		catch(Exception e) {
    			
    		}
    		finally {
    			
    		}
    		
    		return false;
    	}
    	
    }
    
    public static class Lang {
    	
    	/*public static class Placeholders{
    		
    		static Map<String,String> list = new HashMap<String,String>();
    		
    		public static String target = "";
    		public static String sender = "";
    		public static String hat = "";
    		public static String plugin = "§7[§dSakura§bHats§7]§r";
    		
    		public static void Add(String placeholder, String value) {
    			list.put(placeholder, value);
    		}
    	}*/
    	
    	// Example, new Placeholder(this, "%target%", "PlayerName");
    	public static class Placeholder {
    		public JavaPlugin plugin;
    		public String key;
    		public String value;
    		
    		public Placeholder(JavaPlugin plugin, String key, String value) {
    			this.plugin = plugin;
    			this.key = key;
    			this.value = value;
    		}
    	}
    	
    	static List<Placeholder> placeholders = new ArrayList<Placeholder>();
    	
    	public static void AddPlaceholder(JavaPlugin caller, String key, String value) {
    		placeholders.add(new Placeholder(caller,key,value));
    	}
    	
    	public static String Parse(JavaPlugin caller, String text) {
    		text = text.replace("&", "§");

    		for(Placeholder placeholder : placeholders) {
    			if(placeholder.plugin.equals(caller)) {
    				if(text.contains(placeholder.key)) { 
    					text = text.replace(placeholder.key, placeholder.value); 
    				}
    			}
    		}
    		
    		return text;
    	}

    	public static String Get(JavaPlugin caller, String key) {
    		return Parse(caller, Config.GetString(caller, key));
    	}
    }
    
    public static class Config {
    	
    	public static void Initiate(JavaPlugin caller) {
    		Load(caller);
    	}
    	
    	public static void Load(JavaPlugin caller) {
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
    	
    	public static void SetInt(JavaPlugin caller, String key, String value) {
    		caller.getConfig().set(key, value);
    	}
    	
    	public static void SetFloat(JavaPlugin caller, String key, String value) {
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
    		if(GetBool(caller, "debug")) { Log.Info(caller, "Validating Config"); }
    		
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

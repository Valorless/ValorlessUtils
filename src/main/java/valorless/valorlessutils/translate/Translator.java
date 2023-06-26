package valorless.valorlessutils.translate;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.file.YamlFile;
import valorless.valorlessutils.json.JsonUtils;

import com.google.common.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;


public class Translator {
	
	String language = "en_us";
	Map<String, String> languageMap = new HashMap<String, String>();
	
	public Translator(String key) {
		this.language = key;
		this.languageMap = LoadLanguage(key);
	}
	
	public String Translate(String translationKey) {
		if(languageMap == null) {
			return translationKey;
		}else {
			return languageMap.get(translationKey);
		}
		
	}
	
	public String GetLanguageKey() {
		return language;
	}
	
	Map<String, String> LoadLanguage(String key){
		String json = GetLanguageFileContent(key);
		Type mapType = new TypeToken<Map<String, String>>(){}.getType();  
		Map<String, String> son = new Gson().fromJson(json, mapType);
		return son;
	}
	
	public void SetLanguage(String key) {
		languageMap = LoadLanguage(key);
	}
	
	public String GetLanguageFileContent(String key) {
		String path = String.format("%s/languages/%s.lang", ValorlessUtils.thisPlugin.getDataFolder(), key);
		
		File languageFile;
		try {
			languageFile = new File(path);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
        if(!languageFile.exists()) {
        	languageFile.getParentFile().mkdirs();
        	ValorlessUtils.thisPlugin.saveResource("languages\\" + key + ".lang", false);
        }
        String content = "";
		try {
			Path filePath = Path.of(path);
			return Files.readString(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

package valorless.valorlessutils.translate;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;

import com.google.common.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class Translator {

    // Default language set to "en_us"
    String language = "en_us";
    
    // Map to store language translations
    Map<String, String> languageMap = new HashMap<String, String>();
    
    // List of all available languages
    final String[] languages = {
    		"da_dk (Danish)",
    		"de_de (German)",
    		"en_gb (English)",
    		"en_us (American English)",
    		"es_es (Spanish)",
    		"fr_fr (French)",
    		"ja_jp (Japanese)",
    		"ko_kr (Korean)",
    		"nl_nl (Dutch)",
    		"ru_ru (Russian)",
    		"tr_tr (Turkish)",
    		"zh_cn (Chinese (Simplified))"
    };

    /**
     * Constructor for Translator class.
     * @param key The language key to initialize the translator with.
     */
    public Translator(String key) {
        this.language = key;
		this.languageMap = LoadLanguage(key);
    }

    /**
     * Translates a given key to the corresponding language.
     * @param translationKey The key to be translated.
     * @return The translated string or the original key if translation is not available.
     */
    public String Translate(String translationKey) {
        if (languageMap == null) {
            return translationKey;
        } else {
            return languageMap.get(translationKey);
        }
    }

    /**
     * Gets the current language key.
     * @return The current language key.
     */
    public String GetLanguageKey() {
        return language;
    }

    /**
     * Loads language translations from a file.
     * @param key The language key.
     * @return A map of translations.
     */
    Map<String, String> LoadLanguage(String key)  {
    	try {
    		String json = GetLanguageFileContent(key);
    		
        	@SuppressWarnings("serial")
        	Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        	Map<String, String> son = new Gson().fromJson(json, mapType);
        	return son;
    	} catch(Exception e) {
    		String clas = Thread.currentThread().getStackTrace()[3].getClassName();
    		String method = Thread.currentThread().getStackTrace()[3].getMethodName();
    		String err = String.format("Failed to load language '%s'!\n'%s()' (in '%s')", key, method, clas);
    		Log.Error(ValorlessUtils.thisPlugin, err);
    		Log.Error(ValorlessUtils.thisPlugin, "Supported Languages: ");
    		for (String lang : languages) {
    			Log.Error(ValorlessUtils.thisPlugin, lang);
    		}
    		
    		String json = GetLanguageFileContent("en_us");
        	@SuppressWarnings("serial")
        	Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        	Map<String, String> son = new Gson().fromJson(json, mapType);
        	return son;
    	}
    }

    /**
     * Sets the language for translation.
     * @param key The language key to set.
     */
    public void SetLanguage(String key) {
		languageMap = LoadLanguage(key);
    }

    /**
     * Reads the content of a language file.
     * @param key The language key.
     * @return The content of the language file as a string.
     */
    public String GetLanguageFileContent(String key) {
        String path = String.format("%s/languages/%s.lang", ValorlessUtils.thisPlugin.getDataFolder(), key);

        File languageFile;
        try {
            languageFile = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            ValorlessUtils.thisPlugin.saveResource("languages\\" + key + ".lang", false);
        }

        try {
            Path filePath = Path.of(path);
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

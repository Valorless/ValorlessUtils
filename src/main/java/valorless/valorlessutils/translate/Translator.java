package valorless.valorlessutils.translate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import valorless.valorlessutils.Server;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.logging.Log;

import com.google.common.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Handles loading, caching, and retrieving Minecraft item/block translation strings
 * for a given {@link Language}.
 *
 * <p>On construction, the translator attempts to load the appropriate {@code .lang}
 * file from the plugin's data folder. If the file is not present it will be downloaded
 * from the ValorlessUtils GitHub repository. When no server version is available (e.g.
 * during unit-tests or early startup) a bundled fallback file is used instead.</p>
 *
 * <p>All translation keys follow the standard Minecraft namespaced-key format
 * (e.g. {@code "block.minecraft.stone"}).</p>
 *
 * @see Language
 */
public class Translator {

	/** The URL pattern for downloading language files from the ValorlessUtils GitHub repository.
	 * The placeholders are replaced with the server version and language code, respectively.
	 */
	final String BRANCH_MAIN_PATH = "https://raw.githubusercontent.com/Valorless/ValorlessUtils/refs/heads/main/languages/%s/%s.lang";
	final String BRANCH_DEV_PATH = "https://raw.githubusercontent.com/Valorless/ValorlessUtils/refs/heads/dev/languages/%s/%s.lang";

    /** The active language used for all translation look-ups. Defaults to {@link Language#EN_US}. */
	Language language = Language.EN_US;

    /**
     * In-memory cache of translation key → localised string pairs loaded from the
     * active language file.  {@code null} entries indicate a missing or corrupt file.
     */
    Map<String, String> languageMap = new HashMap<String, String>();

	/**
	 * Creates a new {@code Translator} and immediately loads translations for the
	 * given {@link Language}.
	 *
	 * @param language the language whose translation file should be loaded; must not
	 *                 be {@code null}.
	 */
	public Translator(Language language) {
		this.language = language;
		this.languageMap = loadLanguage(language);
	}

    /**
     * Creates a new {@code Translator} and immediately loads translations for the
     * language identified by {@code key}.
     *
     * @param key the language code (e.g. {@code "en_us"}) used to look
     *            up the corresponding {@link Language} constant.
     * @deprecated Use {@link #Translator(Language)} instead.  This constructor will
     *             be removed in a future release.
     */
	@Deprecated(since = "Replaced by Translator(Language), will still work until removed.", forRemoval = true)
    public Translator(String key) {
		Language language = Language.getLanguage(key);
		this.language = language;
		this.languageMap = loadLanguage(language);
    }

    /**
     * Returns the localised string for the given Minecraft translation key.
     *
     * <p>If the language map has not been loaded (e.g. the language file could not be
     * found or parsed), the original {@code translationKey} is returned unchanged so
     * that callers always receive a non-{@code null} result.</p>
     *
     * @param translationKey the Minecraft translation key to look up
     *                       (e.g. {@code "item.minecraft.diamond_sword"}).
     * @return the localised display name, or {@code translationKey} itself if no
     *         mapping is available.
     */
    public String translate(String translationKey) {
        if (languageMap == null) {
            return translationKey;
        } else {
            return languageMap.get(translationKey);
        }
    }

	/**
	 * Returns the localised string for the given Minecraft translation key.
	 *
	 * @param translationKey the Minecraft translation key to look up.
	 * @return the localised display name, or {@code translationKey} if no mapping is
	 *         available.
	 * @deprecated Use {@link #translate(String)} instead.  This method will be
	 *             removed in a future release.
	 */
	@Deprecated(since = "Replaced by translate()", forRemoval = true)
	public String Translate(String translationKey) {
		return translate(translationKey);
	}

    /**
     * Returns the {@link Language} that is currently active for this translator.
     *
     * @return the active {@link Language}; never {@code null}.
     */
    public Language getLanguage() {
        return language;
    }

	/**
	 * Returns the language code for the currently active language
	 * (e.g. {@code "en_us"}).
	 *
	 * @return the language code string.
	 * @deprecated Use {@link #getLanguage()} and call {@link Language#getCode()} on
	 *             the result instead.  This method will be removed in a future release.
	 */
	@Deprecated(since = "Replaced by getLanguage()", forRemoval = true)
	public String GetLanguageKey() {
		return language.getCode();
	}

    /**
     * Loads the translation map for the specified {@link Language}.
     *
     * <p>The resolution order is:</p>
     * <ol>
     *   <li>If the server version is {@link valorless.valorlessutils.Server.Version#NULL},
     *       the bundled {@code fallback.lang} resource is used.</li>
     *   <li>If the language file already exists in the plugin data folder, it is read
     *       directly from disk.</li>
     *   <li>Otherwise the file is downloaded from the ValorlessUtils GitHub repository
     *       (see {@link #downloadLanguage(Language)}).</li>
     * </ol>
     *
     * <p>If an error occurs at any stage, the method logs the failure and falls back to
     * the bundled {@code fallback.lang} language file.</p>
     *
     * @param language the language to load; must not be {@code null}.
     * @return a {@link Map} of translation key → localised string pairs, or the
     *         fallback map if loading fails.
     */
    Map<String, String> loadLanguage(Language language)  {
    	try {
    		String json = "";
    		if(ValorlessUtils.getServerVersion() == Server.Version.NULL) {
    			json = loadFallbackLanguage();
    		}
    		else if(fileExists(language)) {
    			json = getLanguageFileContent(language);
    		}else {
    			json = downloadLanguage(language);
    		}
    		
        	Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        	Map<String, String> son = new Gson().fromJson(json, mapType);
        	Log.info(ValorlessUtils.plugin, String.format("Loaded language %s (%s).", language.getLang(), language.getCode()));
        	return son;
    	} catch(Exception e) {
    		String clas = Thread.currentThread().getStackTrace()[3].getClassName();
    		String method = Thread.currentThread().getStackTrace()[3].getMethodName();
    		String err = String.format("Failed to load language '%s'!\n'%s()' (in '%s')", language.getCode(), method, clas);
    		Log.error(ValorlessUtils.plugin, err);
			Log.error(ValorlessUtils.plugin, "Attempting to load fallback language.");
    		
    		String json = loadFallbackLanguage();
        	Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            return new Gson().fromJson(json, mapType);
    	}
    }
    
    /**
     * Reads the bundled {@code fallback.lang} resource from the plugin's data folder,
     * copying it from the plugin JAR if it has not yet been extracted.
     *
     * <p>This method is used when the server version cannot be determined (e.g. during
     * early startup or in test environments).</p>
     *
     * @return the raw JSON content of the fallback language file, or {@code null} if an
     *         I/O error occurs.
     */
    public String loadFallbackLanguage() {
    	String path = String.format("%s/languages/fallback.lang", ValorlessUtils.plugin.getDataFolder());

        File languageFile;
        try {
            languageFile = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            ValorlessUtils.plugin.saveResource("languages\\" + "fallback.lang", true);
        }

        try {
            Path filePath = Path.of(path);
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * Reads the bundled {@code fallback.lang} resource from the plugin's data folder.
	 *
	 * @return the raw JSON content of the fallback language file, or {@code null} if an
	 *         I/O error occurs.
	 * @deprecated Use {@link #loadFallbackLanguage()} instead.  This method will be
	 *             removed in a future release.
	 */
	@Deprecated(since = "Replaced by loadFallbackLanguage(String)", forRemoval = true)
	public String LoadFallbackLanguage() {
		return loadFallbackLanguage();
	}

    /**
     * Checks whether the {@code .lang} file for the given {@link Language} already
     * exists in the plugin's data folder for the current server version.
     *
     * <p>The expected path is:
     * {@code <dataFolder>/languages/<serverVersion>/<languageCode>.lang}.</p>
     *
     * @param language the language whose file presence should be checked; must not be
     *                 {@code null}.
     * @return {@code true} if the file exists and can be found; {@code false} otherwise.
     */
    public boolean fileExists(Language language) {
    	String path = String.format("%s/languages/%s/%s.lang", ValorlessUtils.plugin.getDataFolder(),
    			ValorlessUtils.getServerVersionString(), language.getCode());

        File languageFile;
        try {
            languageFile = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return languageFile.exists();
	}

	/**
	 * Checks whether the {@code .lang} file for the language identified by {@code key}
	 * exists in the plugin's data folder for the current server version.
	 *
	 * @param key the language code (e.g. {@code "en_us"}).
	 * @return {@code true} if the file exists; {@code false} otherwise.
	 * @deprecated Use {@link #fileExists(Language)} instead.  This method will be
	 *             removed in a future release.
	 */
	@Deprecated(since = "Replaced by fileExists()", forRemoval = true)
	public boolean FileExists(String key) {
		return fileExists(Language.getLanguage(key));
	}

	/**
     * Switches the active language to {@code language} and reloads the translation map.
     *
     * <p>After this call, subsequent {@link #translate(String)} invocations will return
     * strings in the new language.</p>
     *
     * @param language the new language to use; must not be {@code null}.
     */
    public void setLanguage(Language language) {
		languageMap = loadLanguage(language);
    }

	/**
	 * Switches the active language to the one identified by {@code key} and reloads
	 * the translation map.
	 *
	 * @param key the language code (e.g. {@code "en_us"}).
	 * @deprecated Use {@link #setLanguage(Language)} instead.  This method will be
	 *             removed in a future release.
	 */
	@Deprecated(since = "Replaced by setLanguage()", forRemoval = true)
	public void SetLanguage(String key) {
		setLanguage(Language.getLanguage(key));
	}

    /**
     * Reads and returns the raw JSON content of the {@code .lang} file for the given
     * {@link Language} from the plugin's data folder.
     *
     * <p>The file is expected at:
     * {@code <dataFolder>/languages/<serverVersion>/<languageCode>.lang}.</p>
     *
     * @param language the language whose file should be read; must not be {@code null}.
     * @return the file content as a {@link String}, or {@code null} if an I/O error
     *         occurs or the file does not exist.
     */
    public String getLanguageFileContent(Language language) {
    	String path = String.format("%s/languages/%s/%s.lang", ValorlessUtils.plugin.getDataFolder(),
    			ValorlessUtils.getServerVersionString(), language.getCode());

        try {
            Path filePath = Path.of(path);
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
        
    /**
     * Downloads the {@code .lang} file for the specified {@link Language} from the
     * ValorlessUtils GitHub repository and saves it to the plugin's data folder.
     *
     * <p>The method tries the following branches in order until a successful download
     * is achieved:</p>
     * <ol>
     *   <li>{@code main}</li>
     *   <li>{@code dev}</li>
     * </ol>
     *
     * <p>The remote URL pattern is:
     * {@code https://raw.githubusercontent.com/Valorless/ValorlessUtils/refs/heads/<branch>/languages/<serverVersion>/<languageCode>.lang}.</p>
     *
     * <p>If all download attempts fail, {@link #loadFallbackLanguage()} is returned as
     * a last resort.</p>
     *
     * @param language the language to download; must not be {@code null}.
     * @return the raw JSON content of the downloaded language file, the content of the
     *         fallback language file if all downloads fail, or {@code null} if even the
     *         fallback cannot be read.
     */
    public String downloadLanguage(Language language) {
		long startTime = System.currentTimeMillis();
    	
    	List<String> netpathList = List.of(
    			String.format(
    	                BRANCH_MAIN_PATH,
    	                ValorlessUtils.getServerVersionString(), language.getCode()),
    			String.format(
    	                BRANCH_DEV_PATH,
    	                ValorlessUtils.getServerVersionString(), language.getCode())
    			);

		List<String> branches = List.of("main", "dev");
    	

        String path = String.format("%s/languages/%s/%s.lang", ValorlessUtils.plugin.getDataFolder(),
                ValorlessUtils.getServerVersionString(), language.getCode());

        File languageFile;
        try {
            languageFile = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

		int i = 0;
        for(String netpath : netpathList) {
        	Log.info(ValorlessUtils.plugin, String.format("Downloading '%s' language file from GitHub - Branch: %s..\nPlease wait.", language.getCode(), branches.get(i)));
        	i++;
			try {
        		// Create necessary directories
        		languageFile.getParentFile().mkdirs();

        		// Open a connection to the URL
        		URL url = new URL(netpath);
        		URLConnection connection = url.openConnection();
        		connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        		try (InputStream in = connection.getInputStream()) {
        			// Copy the content from the URL to the local file
        			Files.copy(in, languageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        			long endTime = System.currentTimeMillis();
        			long duration = endTime - startTime;
        			Log.info(ValorlessUtils.plugin, String.format("Download success. %sms", duration));
        		}
        	} catch (IOException e) {
        		long endTime = System.currentTimeMillis();
        		long duration = endTime - startTime;
        		Log.error(ValorlessUtils.plugin, String.format("Download failed. %sms", duration));
        		//e.printStackTrace();
        		continue; // Try the next URL if the download fails
        	}

        	// Read and return the file content
        	try {
        		return Files.readString(languageFile.toPath());
        	} catch (IOException e) {
        		e.printStackTrace();
        		continue; // Try the next URL if reading the file fails
        	}
        }
        Log.error(ValorlessUtils.plugin, String.format("All download attempts for '%s' language file failed. Falling back to default language.", language.getCode()));
        return loadFallbackLanguage(); // Return null if all download attempts failed
    }
}

package valorless.valorlessutils.translate;

import valorless.annotations.NotNull;
import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.color.Lang;
import valorless.valorlessutils.logging.Log;

import java.util.Arrays;

/**
 * Enum representing supported languages for translation.
 */
public enum Language {
    DA_DK("da_dk", "Danish"),
    DE_DE("de_de", "German"),
    EN_GB("en_gb", "English"),
    EN_PT("en_pt", "Pirate Speak"),
    EN_US("en_us", "American English"),
    ES_ES("es_es", "Spanish"),
    FR_FR("fr_fr", "French"),
    JA_JP("ja_jp", "Japanese"),
    KO_KR("ko_kr", "Korean"),
    NL_NL("nl_nl", "Dutch"),
    PL_PL("pl_pl", "Polish"),
    PT_BR("pt_br", "Portuguese (Brasil)"),
    PT_PT("pt_pt", "Portuguese (Portugal)"),
    RU_RU("ru_ru", "Russian"),
    TR_TR("tr_tr", "Turkish"),
    ZH_CN("zh_cn", "Chinese (Simplified)");

    private final String code;
    private final String lang;

    Language(String code, String lang) {
        this.code = code;
        this.lang = lang;
    }

    /**
     * Returns the language code (e.g., "en_us").
     *
     * @return the language code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the language name (e.g., "English").
     *
     * @return the language name
     */
    public String getLang() {
        return lang;
    }

    /**
     * Returns a string representation of the Language enum constant.
     *
     * @return a string in the format "Language{code='code', lang='lang'}"
     */
    @Override
    public String toString() {
        return String.format("Language{code='%s', lang='%s'}", code, lang);
    }

    /**
     * Returns the Language enum constant corresponding to the given language code.
     *
     * @param code the language code (e.g., "en_us")
     * @return the corresponding Language enum constant, or Language.EN_US if the code is not found
     */
    @NotNull
    public static Language getLanguage(String code) {
        Language language = Arrays.stream(Language.values()).filter(lang -> lang.code.equalsIgnoreCase(code)).findFirst().orElse(null);
        if (language == null) {
            Log.error(ValorlessUtils.plugin, String.format("Language code '%s' not found. Defaulting to EN_US.", code));
            Log.error(ValorlessUtils.plugin, "Supported Languages: ");
            Arrays.stream(Language.values()).forEach(lang ->
                    Log.error(ValorlessUtils.plugin, String.format("%s (%s)", lang.getCode(), lang.getLang())));
        }

        return language != null ? language : Language.EN_US;
    }
}

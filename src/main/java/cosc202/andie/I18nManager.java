package cosc202.andie;

import java.util.*;

/**
 * I18N Manager for ANDIE to manage Locale and ResourceBundles
 * @author shari838
 */
public class I18nManager {
    private static ResourceBundle bundle;

    /**
     * Set language locale for current selected language
     * @param locale 
     */
    public static void init(Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;  // Default to English
        }
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Return translation of sentence
     * @param key
     * @return 
     */
    public static String get(String key) {
        return bundle.getString(key);
    }
}
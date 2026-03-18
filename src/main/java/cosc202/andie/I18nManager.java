package cosc202.andie;

import java.util.*;

public class I18nManager {
    private static ResourceBundle bundle;

    public static void init(Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;  // Default to English
        }
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    public static String get(String key) {
        return bundle.getString(key);
    }
}
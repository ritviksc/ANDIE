package cosc202.andie;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class SettingsActions {

    /**
     * A list of actions for the setting menu.
     */
    protected ArrayList<Action> actions;
    public Action language;

    /**
     * <p>
     * Create a set of setting menu actions.
     * </p>
     */
    public SettingsActions() {
        actions = new ArrayList<>();
        language = new LanguageAction(I18nManager.get("Language_title"), null, I18nManager.get("Language_desc"), KeyEvent.VK_L);
        actions.add(language);
    }

    /**
     * <p>
     * Create a menu containing the list of setting actions.
     * </p>
     *
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu settingsMenu = new JMenu(I18nManager.get("Setting_title"));

        JMenuItem languageItem = new JMenuItem(language);
        //still works a shortcut function but but only when drop down menu is visible, visually shows shortcut command
        languageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Andie.shortcut | KeyEvent.SHIFT_DOWN_MASK));
        settingsMenu.add(languageItem);

        return settingsMenu;
    }

    /**
     * <p>
     * Action to change app language.
     * </p>
     *
     */
    public class LanguageAction extends ImageAction {

        /**
         * <p>
         * Create a new language action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        LanguageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the language-action action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the LanguageAction is triggered. It
         * prompts the user for a language preference, then restarts application
         * with chosen language.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Properties props = new Properties();
                try (FileInputStream in = new FileInputStream("src/main/resources/config.properties")) {
                    props.load(in);
                } catch (Exception ex) {
                    System.out.println("Error reading config.properties!");
                    return;
                }

                String[] options = {"English", "Dutch"};

                String choice = (String) JOptionPane.showInputDialog(
                        null,
                        I18nManager.get("Choose_desc"),
                        I18nManager.get("Lan_tooltip"),
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == null) {
                    return;
                }

                Locale newLocale = null;

                switch (choice) {
                    case "Dutch":
                        newLocale = new Locale("nl");
                        props.setProperty("language", "nl");
                        break;
                    case "English":
                        props.setProperty("language", "en");
                        break;
                    default:
                        break;
                }

                try (FileOutputStream out = new FileOutputStream("src/main/resources/config.properties")) {
                    props.store(out, "Updated language");
                } catch (Exception ex) {
                    System.out.println("Error writing to config.properties!");
                    return;
                }

                JOptionPane.showMessageDialog(null, I18nManager.get("Lan_successful"));

                boolean readyToClose = true;

                // Close all windows safely
                for (java.awt.Window window : java.awt.Window.getWindows()) {
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispatchEvent(new WindowEvent((JFrame) window, WindowEvent.WINDOW_CLOSING));
                        if (!target.windowClosed) {
                            readyToClose = false;
                            break;
                        }
                    }
                }
                if (!readyToClose) {
                    return;
                }
                // Restart app with new language preference.
                SwingUtilities.invokeLater(() -> {
                    try {
                        Andie.main(new String[]{});
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, I18nManager.get("Lan_error"));
            }
        }

    }

}

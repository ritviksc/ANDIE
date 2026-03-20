package cosc202.andie;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SettingsActions {
     /**
     * A list of actions for the setting menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of setting menu actions.
     * </p>
     */
    public SettingsActions() {
        actions = new ArrayList<>();
        actions.add(new LanguageAction("Language", null, "Set app language preference", KeyEvent.VK_L));
    }

    /**
     * <p>
     * Create a menu containing the list of setting actions.
     * </p>
     *
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Settings");

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
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
         * prompts the user for a language preference, then restarts application with chosen language.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream("src\\main\\resources\\config.properties")) {
                props.load(in);
            } catch (Exception ex) {
                System.out.println("Error reading config.properties!");
                return;
            }

            String[] options = {"English", "Dutch"};

            String choice = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose your language:",
                    "Language Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == null) return;

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

            try (FileOutputStream out = new FileOutputStream("src\\main\\resources\\config.properties")) {
                props.store(out, "Updated language");
            } catch (Exception ex) {
                System.out.println("Error writing to config.properties!");
                return;
            }

            I18nManager.init(newLocale);

            JOptionPane.showMessageDialog(null, "Language set, restarting...");

            // Close all windows safely
            for (java.awt.Window window : java.awt.Window.getWindows()) {
                window.dispose();
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
            JOptionPane.showMessageDialog(null, "Error changing language!");
        }
    }

}

}



package cosc202.andie;

import static cosc202.andie.ImageAction.target;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
        actions.add(new LanguageAction(I18nManager.get("Language_title"), null, I18nManager.get("Language_desc"),
                KeyEvent.VK_L));
        actions.add(new PopUpAction(I18nManager.get("Popup_title"), null, I18nManager.get("Popup_desc"), KeyEvent.VK_P));
        actions.add(new DocumentAction(I18nManager.get("Document_title"), null, I18nManager.get("Document_desc"), KeyEvent.VK_D));
    }

    /**
     * <p>
     * Create a menu containing the list of setting actions.
     * </p>
     *
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(I18nManager.get("Setting_title"));

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
    public class PopUpAction extends ImageAction {

        /**
         * <p>
         * Show pop-up message when Andie starts up, if disabled.
         * </p>
         *
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         *                 null).
         */
        PopUpAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the pop-up action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the PopUpAction is triggered. It
         * enables welcome pop-up whenever Andie starts up.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Preferences pref = Andie.prefs;
            pref.putBoolean("showWelcome", true); // enable pop up
        }

    }

    /**
     * <p>
     * Action to change app language.
     * </p>
     *
     */
    public class DocumentAction extends ImageAction {

        /**
         * <p>
         * Open Andie documentation page in seperate page in default browser.
         * </p>
         *
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         *                 null).
         */
        DocumentAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the document action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the DocumentAction is triggered. It
         * opens up the offical Andie documentation hosted on GitLab pages.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().browse(new URI("https://your-link.com"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         *                 null).
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

                String[] options = { "English", "Dutch" };

                String choice = (String) JOptionPane.showInputDialog(
                        null,
                        I18nManager.get("Choose_desc"),
                        I18nManager.get("Lan_tooltip"),
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

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
                        Andie.main(new String[] {});
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

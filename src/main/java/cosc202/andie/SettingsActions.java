package cosc202.andie;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.prefs.Preferences;

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
    public Action popUp;
    public Action document;

    /**
     * <p>
     * Create a set of setting menu actions.
     * </p>
     */
    public SettingsActions() {
        actions = new ArrayList<>();

        language = new LanguageAction(I18nManager.get("Language_title"), null, I18nManager.get("Language_desc"), KeyEvent.VK_L);
        actions.add(language);
        popUp = new PopUpAction(I18nManager.get("Popup_title"), null, I18nManager.get("Popup_desc"), KeyEvent.VK_P);
        actions.add(popUp);
        document = new DocumentAction(I18nManager.get("Document_title"), null, I18nManager.get("Document_desc"), KeyEvent.VK_U);
        actions.add(document);

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
        // Visual shortcut, binded in andie
        languageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Andie.shortcut | KeyEvent.SHIFT_DOWN_MASK));
        settingsMenu.add(languageItem);
        
        JMenuItem popUpItem = new JMenuItem(popUp);
        // Visual shortcut, binded in andie
        popUpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Andie.shortcut | KeyEvent.SHIFT_DOWN_MASK));
        settingsMenu.add(popUpItem);
        
        JMenuItem documentItem = new JMenuItem(document);
        // Visual shortcut, binded in andie
        documentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, Andie.shortcut | KeyEvent.SHIFT_DOWN_MASK));
        settingsMenu.add(documentItem);


        return settingsMenu;
    }

    /**
     * <p>
     * Action to change pop-up behaviour.
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
            if (pref.getBoolean("showWelcome", true) == true) {
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("popup_set"),
                        I18nManager.get("no_change"),
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }
            pref.putBoolean("showWelcome", true); // enable pop up
        }

    }

    /**
     * <p>
     * Action to open up Andie documentation.
     * </p>
     *
     */
    public class DocumentAction extends ImageAction {
        private static final String url = "https://andie-aa9d21.cspages.otago.ac.nz/docs/";

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
                Desktop.getDesktop().browse(new URI(url));
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
                boolean languageToggle = false; // false = English, true = Dutch
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

                // Check if already selected
                switch (choice) {
                    case "Dutch":
                        if (Andie.prefs.get("lang", "en").equals("nl")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    I18nManager.get("lang_set"),
                                    I18nManager.get("no_change"),
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        languageToggle = true;
                        break;

                    case "English":
                        if (Andie.prefs.get("lang", "en").equals("en")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    I18nManager.get("lang_set"),
                                    I18nManager.get("no_change"),
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        break;
                }

                // Inform user BEFORE closing UI
                JOptionPane.showMessageDialog(null, I18nManager.get("Lan_successful"));

                boolean readyToClose = true;

                // (handles unsaved work)
                for (Window window : Window.getWindows()) {
                    if (window instanceof JFrame frame) {
                        frame.dispatchEvent(
                                new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

                        // If still open --> user cancelled
                        if (frame.isDisplayable()) {
                            readyToClose = false;
                            break;
                        }
                    }
                }

                if (!readyToClose) {
                    return;
                }

                // Save preference
                if (languageToggle) {
                    Andie.prefs.put("lang", "nl");
                } else {
                    Andie.prefs.put("lang", "en");
                }

                for (Window window : Window.getWindows()) {
                    window.dispose();
                }

                // 3. reset static state
                Andie.activeButton = null;
                ImagePanel.windowClosed = true;

                // Restart full app
                SwingUtilities.invokeLater(() -> {
                    try {
                        Andie.main(new String[] {}); // restart ANDIE
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, I18nManager.get("Lan_error"));
            }
        }
    }
}
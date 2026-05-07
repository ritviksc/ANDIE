package cosc202.andie;

import static cosc202.andie.ImageAction.target;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import javax.swing.*;
import javax.imageio.*;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 *
 * <p>
 * This class is the entry point for the program. It creates a Graphical User
 * Interface (GUI) that provides access to various image editing and processing
 * operations.
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @author Steven Mills
 * @version 1.0
 */
public class Andie {

    static JButton activeButton;
    //get control for windows and command for macos
    static final int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    private static final String url = "https://andie-aa9d21.cspages.otago.ac.nz/docs/";
    public static Preferences prefs = Preferences.userNodeForPackage(Andie.class);


    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     *
     * <p>
     * This method sets up an interface consisting of an active image (an
     * {@code ImagePanel}) and various menus which can be used to trigger
     * operations to load, save, edit, etc. These operations are implemented
     * {@link ImageOperation}s and triggered via {@code ImageAction}s grouped by
     * their general purpose into menus.
     * </p>
     *
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     *
     * @throws Exception if something goes wrong.
     */
    public static void createAndShowGUI() throws Exception {
        // Load ANDIE's language before building UI
        String language = prefs.get("lang", "en");

        Locale locale = new Locale(language);
        I18nManager.init(locale.equals(new Locale("en")) ? null : locale);

        // Set up the main GUI frame
        JFrame frame = new JFrame("ANDIE");

        Image image = ImageIO.read(Andie.class.getClassLoader().getResource("icon.png"));
        frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // The main content area is an ImagePanel
        ImagePanel imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // create the top row for all the main actions headers
        JPanel mainRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainRow.setBackground(Color.WHITE);
        
        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        JButton fileButton = new JButton(I18nManager.get("file_Title"));
        mainRow.add(fileButton);
        bindFileShortcuts(frame.getRootPane(), fileActions);
        
        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        JButton editButton = new JButton(I18nManager.get("Edit_title"));
        mainRow.add(editButton);
        bindEditShortcuts(frame.getRootPane(), editActions);
        
        // View actions control how the image is displayed, but do not alter its actual content
        ViewActions viewActions = new ViewActions();
        JButton viewButton = new JButton(I18nManager.get("View_title"));
        mainRow.add(viewButton);
        bindViewShortcuts(frame.getRootPane(), viewActions);
        
        // Filters apply a per-pixel operation to the image, generally based on a local window
        FilterActions filterActions = new FilterActions();
        JButton filterButton = new JButton(I18nManager.get("Filter_title"));
        mainRow.add(filterButton);
        bindFilterShortcuts(frame.getRootPane(), filterActions);
        
        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        JButton colourButton = new JButton(I18nManager.get("colour_title"));
        mainRow.add(colourButton);
        
        // Macros action controls
        MacroActions macroActions = new MacroActions();
        JButton macroButton = new JButton("Macro");
        mainRow.add(macroButton);
        bindMacroShortcuts(frame.getRootPane(), macroActions);
        
        // Language action controls language of the app
        SettingsActions settingsActions = new SettingsActions();
        JButton settingsButton = new JButton(I18nManager.get("Setting_title"));
        bindSettingsShortcuts(frame.getRootPane(), settingsActions); 
        mainRow.add(settingsButton);

        // aesthetic fix on the buttons
        styleMenuButton(fileButton);
        styleMenuButton(editButton);
        styleMenuButton(viewButton);
        styleMenuButton(filterButton);
        styleMenuButton(colourButton);
        styleMenuButton(settingsButton);
        styleMenuButton(macroButton);

        JPanel toolbarPanel = new JPanel(new CardLayout());
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        toolbarPanel.setVisible(false);

        // add functions to toolbar
        toolbarPanel.add(editActions.createToolBar(), "EDIT");
        toolbarPanel.add(viewActions.createToolBar(), "VIEW");
        toolbarPanel.add(colourActions.createToolBar(), "COLOUR");
        toolbarPanel.add(macroActions.createToolBar(), "MACRO");
        toolbarPanel.add(filterActions.createToolBar(), "FILTER");

        CardLayout cardLayout = (CardLayout) toolbarPanel.getLayout();

        editButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(toolbarPanel, "EDIT");
            setActiveButton(editButton);
            toolbarPanel.setVisible(true);
        });

        viewButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(toolbarPanel, "VIEW");
            setActiveButton(viewButton);
            toolbarPanel.setVisible(true);
        });

        colourButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(toolbarPanel, "COLOUR");
            setActiveButton(colourButton);
            toolbarPanel.setVisible(true);
        });

        filterButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(toolbarPanel, "FILTER");
            setActiveButton(filterButton);
            toolbarPanel.setVisible(true);
        });

        macroButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(toolbarPanel, "MACRO");
            setActiveButton(macroButton);
            toolbarPanel.setVisible(true);
        });

        // drop down menu for file
        JMenu fileMenu = fileActions.createMenu();
        JPopupMenu filePopup = fileMenu.getPopupMenu();

        fileButton.addActionListener(e -> {
            filePopup.show(fileButton, 0, fileButton.getHeight());
        });

        JMenu settingsMenu = settingsActions.createMenu();
        JPopupMenu settingsPopup = settingsMenu.getPopupMenu();

        settingsButton.addActionListener(e -> {
            settingsPopup.show(settingsButton, 0, settingsButton.getHeight());
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(mainRow);
        topPanel.add(toolbarPanel);

        frame.add(topPanel, BorderLayout.NORTH);
        
        
        // Save on exit functionality
        // Reuses export code which is inefficient
        // but such is technical debt as it was
        // implemented in such a way that this was 
        // fastest without having to retool the program
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                target.windowClosed = true;

                if (target.getImage().isSaved()) {
                    e.getWindow().dispose();
                } else if (target.getImage() != null && !target.getImage().isSaved()) {

                    Object[] saveOptions = { I18nManager.get("save_menu_save"), I18nManager.get("save_menu_save_as"),
                            I18nManager.get("save_menu_exit_without_saving"), I18nManager.get("save_menu_cancel") };

                    int saveOption = JOptionPane.showOptionDialog(null,
                            I18nManager.get("save_menu_message"), I18nManager.get("save_menu_title"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null,
                            saveOptions, saveOptions[0]);

                    switch (saveOption) {

                        case 0: // Save

                            try {
                                target.getImage().save();
                                e.getWindow().dispose();
                            } catch (Exception ex) {
                                System.exit(1);
                            }
                            break;

                        case 1: // Save as

                            JFileChooser fileChooser = new JFileChooser();

                            FileNameExtensionFilter jpegFormat = new FileNameExtensionFilter("JPEG Image (*.jpg)",
                                    "jpg");
                            FileNameExtensionFilter pngFormat = new FileNameExtensionFilter("PNG Image (*.png)", "png");
                            FileNameExtensionFilter giffFormat = new FileNameExtensionFilter("GIF Image (*.gif)",
                                    "gif");
                            // adds the file times as a choosable ooption
                            fileChooser.addChoosableFileFilter(pngFormat);
                            fileChooser.addChoosableFileFilter(jpegFormat);
                            fileChooser.addChoosableFileFilter(giffFormat);

                            fileChooser.setAcceptAllFileFilterUsed(false);
                            fileChooser.setFileFilter(jpegFormat);

                            int result = fileChooser.showSaveDialog(target);

                            if (result == JFileChooser.APPROVE_OPTION) {
                                try {
                                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();

                                    // gets the filter that the user selects (pngFormat)
                                    FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser
                                            .getFileFilter();

                                    // gets the acutal format from filter
                                    String selectedFormat = filter.getExtensions()[0].toLowerCase();

                                    // if the file name is left as empty user has wnarnign message pop up
                                    if (fileChooser.getSelectedFile().getName().trim().isEmpty()) {
                                        JOptionPane.showMessageDialog(fileChooser, I18nManager.get("no_File_Name"),
                                                I18nManager.get("nfn_Title"), JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                    // check for image to have transparency when saved as jpeg
                                    if (selectedFormat.equals("jpg") && target.getImage().hasTransparency()) {
                                        JOptionPane.showMessageDialog(fileChooser,
                                                "Your image has a transparency, select another format",
                                                "Transparency warning", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }

                                    target.getImage().saveAs(imageFilepath, selectedFormat);

                                } catch (Exception ex) {
                                    System.exit(1);
                                }
                            }
                            break;

                        case 2: // Exit without saving

                            e.getWindow().dispose();
                            break;

                        case 3: // Cancel/close
                            target.windowClosed = false;
                            break;

                    }
                } else {
                    e.getWindow().dispose();
                }
            }
        });

        frame.pack();
        frame.setVisible(true);
        Bomb bomb = new Bomb(imagePanel);
        boolean showWelcome = prefs.getBoolean("showWelcome", true);

        if (showWelcome) {
            showWelcomePopup(frame, prefs);
        }
    }
    //binds the shortcuts for all the file options
    private static void bindFileShortcuts(JRootPane rootPane, FileActions fileActions) {
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcut), "open", fileActions.open);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcut), "save", fileActions.save);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcut | KeyEvent.SHIFT_DOWN_MASK), "saveAs", fileActions.saveAs);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_E, shortcut | KeyEvent.SHIFT_DOWN_MASK), "export", fileActions.export);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcut), "exit", fileActions.exit);
    }
    //binds the shortcuts for all the edit options
    private static void bindEditShortcuts(JRootPane rootPane, EditActions editActions){   
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcut), "undo", editActions.undoAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcut | KeyEvent.SHIFT_DOWN_MASK), "redo", editActions.redoAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_R, shortcut | KeyEvent.SHIFT_DOWN_MASK), "resize", editActions.resizeAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_H, shortcut | KeyEvent.SHIFT_DOWN_MASK),"flip horizontally", editActions.flipHorizontalAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_V, shortcut | KeyEvent.SHIFT_DOWN_MASK),"flip vertically", editActions.flipVerticalAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, shortcut | KeyEvent.SHIFT_DOWN_MASK), "rotate 90 degrees clockwise", editActions.rotate90CAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, shortcut | KeyEvent.SHIFT_DOWN_MASK), "rotate 90 degrees anti-clockwise", editActions.rotate90CCAction);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, shortcut | KeyEvent.SHIFT_DOWN_MASK), "rotate 180 degrees", editActions.rotate180Action);
    }
    //binds the shortcuts for all the view options     
    private static void bindViewShortcuts(JRootPane rootPane, ViewActions viewActions){
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, shortcut), "zoom in", viewActions.zoomIn);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, shortcut), "zoom out", viewActions.zoomOut);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_F, shortcut | KeyEvent.SHIFT_DOWN_MASK), "zoom full", viewActions.zoomFull);
    }

    //binds the shortcuts for all the filter options
    private static void bindFilterShortcuts(JRootPane rootPane, FilterActions filterActions){ 
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_M, shortcut | KeyEvent.SHIFT_DOWN_MASK), "mean", filterActions.mean);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_K, shortcut | KeyEvent.SHIFT_DOWN_MASK), "sharpen", filterActions.sharpen);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_G, shortcut | KeyEvent.SHIFT_DOWN_MASK), "gaussian", filterActions.gaussian);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_D, shortcut | KeyEvent.SHIFT_DOWN_MASK), "median", filterActions.median);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_E, shortcut | KeyEvent.SHIFT_DOWN_MASK), "emboss", filterActions.emboss);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_B, shortcut | KeyEvent.SHIFT_DOWN_MASK), "sobel", filterActions.sobel);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_C, shortcut | KeyEvent.SHIFT_DOWN_MASK), "contrastMask", filterActions.contrastMask);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_J, shortcut | KeyEvent.SHIFT_DOWN_MASK), "fir", filterActions.fir);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcut | KeyEvent.SHIFT_DOWN_MASK), "randomScattering", filterActions.randomScattering);
        
    }
    //binds the shortcuts for all the macro options
    private static void bindMacroShortcuts(JRootPane rootPane, MacroActions macroActions){
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcut | KeyEvent.SHIFT_DOWN_MASK), "startMacro", macroActions.startMacro);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_W, shortcut | KeyEvent.SHIFT_DOWN_MASK), "stopMacro", macroActions.stopMacro);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_L, shortcut | KeyEvent.SHIFT_DOWN_MASK), "loadMacro", macroActions.loadMacro);
    }
    
    //binds the shortcuts for all the settings options
    private static void bindSettingsShortcuts(JRootPane rootPane, SettingsActions settingsActions){
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_I, shortcut | KeyEvent.SHIFT_DOWN_MASK), "language", settingsActions.language);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcut | KeyEvent.SHIFT_DOWN_MASK), "popUp", settingsActions.popUp);
        bindShortcut(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_U, shortcut | KeyEvent.SHIFT_DOWN_MASK), "document", settingsActions.document);
    }
    
    
    private static void bindShortcut(JRootPane rootPane, KeyStroke keyStroke, String name, Action action) {
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(keyStroke, name);
        actionMap.put(name, action);
    }

    /**
     * makes some aesthetic changes to the main menu button, change colour when
     * the mouse hovers over
     *
     * @param button
     */
    private static void styleMenuButton(JButton button) {
        Color normalColour = Color.WHITE;
        Color hoverColour = new Color(240, 240, 240);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(normalColour);
        button.setMargin(new Insets(4, 12, 4, 12));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(hoverColour);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(normalColour);
                }
            }
        });
    }

    // chnages the aestheics of the button that is currerntly selects also changes
    // back the previously selected menu button
    private static void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE);
        }

        button.setBackground(new Color(240, 240, 240));
        activeButton = button;
    }

    private static void showWelcomePopup(JFrame frame, Preferences prefs) {

        // Check if user disabled it
        boolean showWelcome = prefs.getBoolean("showWelcome", true);
        if (!showWelcome)
            return;

        JDialog dialog = new JDialog(frame, I18nManager.get("welcome"), true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(10, 10));

        // Padding
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel(I18nManager.get("welcome"), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        dialog.add(title, BorderLayout.NORTH);

        // Center content
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel text = new JLabel(I18nManager.get("get_started"));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text1 = new JLabel(I18nManager.get("fav_editor"));
        text1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel link = new JLabel(I18nManager.get("doc_link"));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.setAlignmentX(Component.CENTER_ALIGNMENT);

        link.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unused")
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        center.add(Box.createVerticalStrut(10));
        center.add(text);
        center.add(text1);
        center.add(Box.createVerticalStrut(10));
        center.add(link);

        dialog.add(center, BorderLayout.CENTER);

        // Bottom section
        JPanel bottom = new JPanel(new BorderLayout());

        JCheckBox dontShow = new JCheckBox(I18nManager.get("dont_show"));

        JButton ok = new JButton(I18nManager.get("get_started_button"));
        ok.addActionListener(e -> {
            if (dontShow.isSelected()) {
                prefs.putBoolean("showWelcome", false);
            }
            dialog.dispose();
        });

        bottom.add(dontShow, BorderLayout.WEST);
        bottom.add(ok, BorderLayout.EAST);

        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
   

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     *
     * <p>
     * Creates and launches the main GUI in a separate thread. As a result, this
     * is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     *
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();

            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        });

    }
}

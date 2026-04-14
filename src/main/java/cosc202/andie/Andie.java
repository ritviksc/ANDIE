package cosc202.andie;

import static cosc202.andie.ImageAction.target;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.imageio.*;
import java.util.Locale;
import java.util.Properties;

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
    
    private static JButton activeButton;

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
    
    private static void createAndShowGUI() throws Exception {
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

        //create the top row for all the main actions headers
        JPanel mainRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainRow.setBackground(new Color(255, 255, 255));
        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        JButton fileButton = new JButton(I18nManager.get("file_Title"));
        mainRow.add(fileButton); 
        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        JButton editButton = new JButton(I18nManager.get("Edit_title"));
        mainRow.add(editButton);
        // View actions control how the image is displayed, but do not alter its actual content
        ViewActions viewActions = new ViewActions();
        JButton viewButton = new JButton(I18nManager.get("View_title"));
        mainRow.add(viewButton);
        // Filters apply a per-pixel operation to the image, generally based on a local window
        FilterActions filterActions = new FilterActions(); 
        JButton filterButton = new JButton(I18nManager.get("Filter_title"));
        mainRow.add(filterButton);
        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        JButton colourButton = new JButton(I18nManager.get("colour_title"));
        mainRow.add(colourButton);
        // Language action controls language of the app
        SettingsActions languageActions = new SettingsActions();  
        JButton settingsButton = new JButton(I18nManager.get("Setting_title"));
        mainRow.add(settingsButton);
        
        
        
           
        //aesthetic fix on the buttons
        styleMenuButton(fileButton);
        styleMenuButton(editButton);
        styleMenuButton(viewButton);
        styleMenuButton(filterButton);
        styleMenuButton(colourButton);
        styleMenuButton(settingsButton);
        
        JPanel toolbarPanel = new JPanel(new CardLayout());
        toolbarPanel.setBackground(new Color(255, 255, 255));
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        toolbarPanel.setVisible(false);
        
        //add functions to toolbar
        toolbarPanel.add(editActions.createToolBar(), "EDIT");
        toolbarPanel.add(viewActions.createToolBar(), "VIEW");
        toolbarPanel.add(colourActions.createToolBar(), "COLOUR");
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
        
        //drop down menu for file
        JMenu fileMenu = fileActions.createMenu();
        JPopupMenu filePopup = fileMenu.getPopupMenu();

        fileButton.addActionListener(e -> {
            filePopup.show(fileButton, 0, fileButton.getHeight());
        });
//        //drop down menu for filter
//        JMenu filterMenu = filterActions.createMenu();
//        JPopupMenu filterPopup = filterMenu.getPopupMenu();
//        
//        filterButton.addActionListener( e -> {
//                filterPopup.show(filterButton, 0, filterButton.getHeight());
//        });
        //drop down menu for settings
        JMenu settingsMenu = languageActions.createMenu();
        JPopupMenu settingsPopup = settingsMenu.getPopupMenu();

        settingsButton.addActionListener(e -> {
            settingsPopup.show(settingsButton, 0, settingsButton.getHeight());
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(mainRow);
        topPanel.add(toolbarPanel);

        frame.add(topPanel, BorderLayout.NORTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                target.windowClosed = true;
                
                if (target.getImage().isSaved()) {
                    e.getWindow().dispose();
                } else if (target.getImage() != null && !target.getImage().isSaved()) {

                    Object[] saveOptions = {I18nManager.get("save_menu_save"), I18nManager.get("save_menu_save_as"), I18nManager.get("save_menu_exit_without_saving"), I18nManager.get("save_menu_cancel")};

                    int saveOption = JOptionPane.showOptionDialog(null,
                            I18nManager.get("save_menu_message"), I18nManager.get("save_menu_title"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null,
                            saveOptions, saveOptions[0]);

                    switch (saveOption) {

                        case 0:

                            try {
                                target.getImage().save();
                                e.getWindow().dispose();
                            } catch (Exception ex) {
                                System.exit(1);
                            }
                            break;

                        case 1:

                            JFileChooser fileChooser = new JFileChooser();
                            int result = fileChooser.showSaveDialog(target);

                            if (result == JFileChooser.APPROVE_OPTION) {
                                try {
                                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                                    target.getImage().saveAs(imageFilepath);
                                    e.getWindow().dispose();
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, I18nManager.get("save_menu_save_as_error"));
                                }
                            } else if (result == JFileChooser.CANCEL_OPTION) {
                                target.windowClosed = false;
                            }

                            break;

                        case 2:

                            e.getWindow().dispose();
                            break;

                        case 3:
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
    }
    /**makes some aesthetic changes to the main menu button, change colour when the mouse hovers over
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
    //chnages the aestheics of the button that is currerntly selects also changes back the previously selected menu button
    private static void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE);
        }
        
        button.setBackground(new Color(240, 240, 240));
        activeButton = button;
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
        Properties props = new Properties();
        String myPath = "src/main/resources/config.properties";
        Path path = Paths.get(myPath);
        try {
            // Attempt to create the file. If it already exists, it throws FileAlreadyExistsException.
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignored) {
            // File already exists, no need to create it again.
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
            throw e;
        }

        // Ensure parent directories exist
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        try (FileInputStream in = new FileInputStream(path.toFile())) {
            props.load(in);
        }

        String language = props.getProperty("language", "en");

        if (language.equals("en")) {
            I18nManager.init(null);
        } else {
            Locale locale = new Locale(language);
            I18nManager.init(locale);
        }
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

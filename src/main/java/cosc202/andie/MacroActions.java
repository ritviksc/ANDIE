/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author loekvanbroekhoven
 */
public class MacroActions extends ToolbarActions {
    
    private static JButton activeButton;
    private static File macroFolder;    
    
    public MacroActions(){
        actions = new ArrayList<>();
        actions.add(new StartMacroAction("start recording", new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Macro/start.png")), "Starts recording", null));
        actions.add(new StopMacroAction("stop recording", new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Macro/stop.png")), "Stops recording", null));
        actions.add(new LoadMacroAction("load macro", new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Macro/load.png")), "Loads Macro", null));
    }
    
    @Override
    public JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.WHITE);

        for (Action action : actions) {
            JButton button = new JButton(action);

            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setText("");
            button.setToolTipText((String) action.getValue(Action.SHORT_DESCRIPTION));
            button.setMargin(new Insets(5, 10, 5, 10));
            button.setBackground(Color.WHITE);

            if (action instanceof StartMacroAction startMacroAction) {
                startMacroAction.setButton(button);
            }

            toolBar.add(button);
        }

        return toolBar;
    }
    
    //highlights the active button, so when start is push its it hightlighted
    private static void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE);
        }

        button.setBackground(new Color(240, 240, 240));
        activeButton = button;
    }
    //clears the active button from being hightlighed, start return to normal when stop is clicked
    private static void clearActiveButton(){
        if(activeButton != null){
            activeButton.setBackground(Color.WHITE);
            activeButton = null;
        }
        
    }
    //checks if there is a Macros folder, else then it creates one in home directory,
    private static void createMacroFolder() {
    if (macroFolder == null) {
        macroFolder = new File(System.getProperty("user.home"), "Macros_Folder");

        if (!macroFolder.exists()) {
            macroFolder.mkdirs();
        }
    }
}
          
    public class StartMacroAction extends ImageAction {
        
        private JButton button;       

        StartMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        
        public void setButton(JButton button){
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            //checks to see if image is loaded in
            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(target, I18nManager.get("no_Image"));
                return;
            }
            
            MacroManager.start();
            setActiveButton(button);
            
        }
    }
    
    public class StopMacroAction extends ImageAction {

        StopMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Macro");
            
            createMacroFolder();

            chooser.setCurrentDirectory(macroFolder);
            
            FileNameExtensionFilter macroFormat = new FileNameExtensionFilter("Macro (*.macro)", "macro");
            chooser.addChoosableFileFilter(macroFormat);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(macroFormat);

            int result = chooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                //macro extension
                if (!file.getName().toLowerCase().endsWith(".macro")) {
                    file = new File(file.getAbsolutePath() + ".macro");
                }

                try {
                    boolean saved = MacroManager.stop(file);

                    if (!saved) {
                        JOptionPane.showMessageDialog(null, "No operations recorded.");
                    }
                    
                    clearActiveButton();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving macro.");
                }
            }
        }
    }
    
    public class LoadMacroAction extends ImageAction {

        LoadMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(target, I18nManager.get("no_Image"));
                return;
            }
            createMacroFolder();

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load Macro");
            chooser.setCurrentDirectory(macroFolder);
            
            

            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    ArrayList<ImageOperation> ops = MacroManager.loadMacro(file);

                    for (ImageOperation op : ops) {
                        target.getImage().apply(op);
                    }

                    target.repaint();
                    target.getParent().revalidate();

                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading macro.");
                }
            }
        }
    }
    
    
        
}

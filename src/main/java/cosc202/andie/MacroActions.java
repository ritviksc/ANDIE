/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

/**
 *
 * @author loekvanbroekhoven
 */
public class MacroActions extends ToolbarActions {
    
    public MacroActions(){
        actions = new ArrayList<>();
        actions.add(new StartMacroAction("start recording", new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Macro/start.png")), "Starts recording", null));
        actions.add(new StopMacroAction("stop recording", new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Macro/stop.png")), "Stops recording", null));
        actions.add(new LoadMacroAction("load macro", new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Macro/load.png")), "Loads Macro", null));
    }
       
    public class StartMacroAction extends ImageAction {

        StartMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e){
            MacroManager.start();
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
                JOptionPane.showMessageDialog(null, "No image loaded.");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load Macro");

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

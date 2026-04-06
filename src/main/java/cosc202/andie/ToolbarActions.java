/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.awt.Color;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *
 * @author loekvanbroekhoven
 */  
public abstract class ToolbarActions {
    protected ArrayList<Action> actions;

    public JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.WHITE);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        for (Action action : actions) {
            JButton button = createToolbarButton(action);
            toolBar.add(button);
        }

        return toolBar;
    }

    private JButton createToolbarButton(Action action) {
        JButton button = new JButton(action);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setText("");
        button.setToolTipText((String) action.getValue(Action.SHORT_DESCRIPTION));
        button.setMargin(new Insets(5, 10, 5, 10));
        button.setBackground(Color.WHITE);

        Color normal = Color.WHITE;
        Color hover = new Color(240, 240, 240);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normal);
            }
        });

        return button;
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Bomb {

    private static final int minDelay = 5000;
    private static final int maxDelay = 10000;
    private static final int fuse = 3000;

    private static final int bWidth = 88;
    private static final int bHeight = 72;

    private Random rand = new Random();
    private final ImagePanel panel;

    public Bomb(ImagePanel panel) {
        this.panel = panel;
        //creates and initail delay before loading in a bom
        Timer initialDelay = new Timer(rand.nextInt(maxDelay - minDelay) + minDelay, (ActionEvent e1) -> {
            spawnBomb();
            
        });
        //removes repeats
        initialDelay.setRepeats(false);
        //starts timer then calls spawnBomb()
        initialDelay.start();    
    }

    public void spawnBomb() {
       
        
        ImageIcon icon = new ImageIcon(
                Andie.class.getClassLoader().getResource("bomb.png")
        );

        JLabel bomb = new JLabel(new ImageIcon(
                icon.getImage().getScaledInstance(bWidth, bHeight, Image.SCALE_SMOOTH)
        ));
        //sets the size of the label of the bomb so matches the scalled icon
        bomb.setSize(bWidth, bHeight);
        //sets the location of the bomb by random
        bomb.setLocation(
                rand.nextInt(panel.getWidth() - bWidth),
                rand.nextInt(panel.getHeight() - bHeight)
        );

        //set the fuse timer that makes it so if the bomb goes off unless it is clicked and timer is stopped
        Timer fuseTimer = new Timer(fuse, e -> {
            JOptionPane.showMessageDialog(panel, "Failed to defuse bomb", "Bomb Exploded", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });
        //stops timer repeat
        fuseTimer.setRepeats(false);
        //starts the fuse timer
        fuseTimer.start();

        bomb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //stops the fuse timer
                fuseTimer.stop();
                //removes bomb objecy from the panel
                panel.remove(bomb);
                //refreshes
                panel.repaint();

                //random delay between the max and min delay variables
                int delay = rand.nextInt(maxDelay - minDelay) + minDelay;

                //runs a timer for delay period and then calls the spawnBomb method again to repeat
                Timer nextBomb = new Timer(delay, (ActionEvent e1) -> {
                    spawnBomb();
                });
                //removes repeats
                nextBomb.setRepeats(false);
                //starts timer and than calls to spawn a new bomb
                nextBomb.start();
            }
        });
        
        panel.setLayout(null);
        panel.add(bomb);
        panel.setComponentZOrder(bomb, 0);
        panel.repaint();
    }
}

    


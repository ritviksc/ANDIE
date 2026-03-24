package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Colour menu.
 *
 * </p>
 *
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel
 * directly without reference to the rest of the image. This includes conversion
 * to greyscale in the sample code, but more operations will need to be added.
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
public class ColourActions {

    /**
     * A list of actions for the Colour menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<>();
<<<<<<< HEAD
        actions.add(new ConvertToGreyAction("Greyscale", null, "Convert to greyscale", KeyEvent.VK_G));
        actions.add(new ThresholdAction("Threshold", null, "Apply threshold", KeyEvent.VK_T));
        actions.add(new InversionAction("Invert", null, "Invert colours", KeyEvent.VK_I));
        actions.add(new ChannelSwapAction("Channel Swap", null, "Swap RGB channels", KeyEvent.VK_C));
=======
        actions.add(new ConvertToGreyAction(I18nManager.get("greyscale"), null, I18nManager.get("greyscale_desc"), KeyEvent.VK_G));
        actions.add(new ThresholdAction(I18nManager.get("threshold"), null, I18nManager.get("threshold_desc"), KeyEvent.VK_T));
        actions.add(new InversionAction(I18nManager.get("invert"), null, I18nManager.get("invert_desc"), KeyEvent.VK_I));
        actions.add(new ChannelSwapAction(I18nManager.get("channel_swap"), null, I18nManager.get("channel_swap_desc"), KeyEvent.VK_C));
>>>>>>> b04e159c7ab70f9d8cbd8a4482fdb2a9a40739f1
    }

    /**
     * <p>
     * Create a menu containing the list of Colour actions.
     * </p>
     *
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {

        JMenu fileMenu = new JMenu(I18nManager.get("colour_title"));


        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     *
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to apply a threshold to an image.
     * </p>
     */
    public class ThresholdAction extends ImageAction {

        ThresholdAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String input = JOptionPane.showInputDialog(I18nManager.get("threshold_prompt"));

            if (input == null) {
                return;
            }

            int threshold;

            try {
                threshold = Integer.parseInt(input);

                // Validate range (error prevention)
                if (threshold < 0 || threshold > 255) {
                    JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("threshold_invalid"),
                        I18nManager.get("error_title"),
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

            } catch (NumberFormatException ex) {
                // Handle invalid number input
                JOptionPane.showMessageDialog(
                    null,
                    I18nManager.get("threshold_invalid"),
                    I18nManager.get("error_title"),
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            target.getImage().apply(new ImageThresholdingFilter(threshold));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class InversionAction extends ImageAction {

        InversionAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            target.getImage().apply(new ImageInversion());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class ChannelSwapAction extends ImageAction {

        ChannelSwapAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Apply the ColourChannelSwap operation
            target.getImage().apply(new ColourChannelSwapping());
            target.repaint();
            target.getParent().revalidate();
        }
    }

}

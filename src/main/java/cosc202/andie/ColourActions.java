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
public class ColourActions extends ToolbarActions {

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<>();
        actions.add(new ConvertToGreyAction(I18nManager.get("greyscale"),
                new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Colour/greyscale.png")),
                I18nManager.get("greyscale_desc"),
                KeyEvent.VK_G));
        actions.add(new ThresholdAction(I18nManager.get("threshold"),
                new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Colour/dark.png")),
                I18nManager.get("threshold_desc"),
                KeyEvent.VK_T));
        actions.add(new InversionAction(I18nManager.get("invert"),
                new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Colour/invert.png")),
                I18nManager.get("invert_desc"),
                KeyEvent.VK_I));
        actions.add(new ChannelSwapAction(I18nManager.get("channel_swap"),
                new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Colour/rgb.png")),
                I18nManager.get("channel_swap_desc"),
                KeyEvent.VK_C));
        actions.add(new BrightnessAndContrastAction(I18nManager.get("brightness_contrast"),
                new ImageIcon(Andie.class.getClassLoader().getResource("ToolbarIcons/Colour/Brightness.png")),
                I18nManager.get("brightness_contrast_desc"),
                KeyEvent.VK_B));

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
     *
     * <p>
     * Prompts the user to enter a threshold value between 0 and 255. Pixels
     * above the threshold are set to white, and those below are set to black.
     * </p>
     */
    public class ThresholdAction extends ImageAction {

        ThresholdAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the threshold action is triggered.
         * </p>
         *
         * <p>
         * Validates user input and applies thresholding if valid. Displays
         * error dialogs for invalid input or missing image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // No image loaded error message
            if (target.getImage() == null || target.getImage().getCurrentImage() == null) {
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("threshold_no_image"),
                        I18nManager.get("error_title"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

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
                            I18nManager.get("threshold_out_of_range"),
                            I18nManager.get("error_title"),
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

            } catch (NumberFormatException ex) {
                // Handle invalid number input
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("threshold_not_number"),
                        I18nManager.get("error_title"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // target.getImage().apply(new ConvertToGrey());
            target.getImage().apply(new ImageThresholdingFilter(threshold));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to invert the colours of an image.
     * </p>
     *
     * <p>
     * Each pixel's RGB values are inverted (e.g., 255 - value), producing a
     * negative image effect.
     * </p>
     */
    public class InversionAction extends ImageAction {

        InversionAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the inversion action is triggered.
         * </p>
         *
         * <p>
         * Applies a colour inversion operation to the image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            target.getImage().apply(new ImageInversion());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to swap the colour channels of an image.
     * </p>
     *
     * <p>
     * Allows the user to select a permutation of RGB channels (e.g., GBR, BGR)
     * which rearranges how colours are displayed.
     * </p>
     */
    public class ChannelSwapAction extends ImageAction {

        ChannelSwapAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the channel swap action is triggered.
         * </p>
         *
         * <p>
         * Prompts the user to select a channel order and applies the
         * corresponding transformation.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            //No image loaded error
            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("channel_no_image"),
                        I18nManager.get("error_title"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String[] colourOptions = {"RGB", "RBG", "GRB", "GBR", "BRG", "BGR"};
            JComboBox<String> colourChannelCycle = new JComboBox<>(colourOptions);

            int option = JOptionPane.showOptionDialog(
                    null,
                    colourChannelCycle,
                    I18nManager.get("channel_prompt"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );

            if (option != JOptionPane.OK_OPTION) {
                return;
            }

            int selectedIndex = colourChannelCycle.getSelectedIndex(); // 0–5
            target.getImage().apply(new ColourChannelSwapping(selectedIndex));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to adjust brightness and contrast of an image.
     * </p>
     */

    public class BrightnessAndContrastAction extends ImageAction {

        BrightnessAndContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // No image loaded error
            if (target.getImage() == null || target.getImage().getCurrentImage() == null) {
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("brightness_no_image"),
                        I18nManager.get("error_title"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String brightnessInput = JOptionPane.showInputDialog(I18nManager.get("brightness_prompt"));
            if (brightnessInput == null) {
                return;
            }

            String contrastInput = JOptionPane.showInputDialog(I18nManager.get("contrast_prompt"));
            if (contrastInput == null) {
                return;
            }

            double brightness;
            double contrast;

            try {
                brightness = Double.parseDouble(brightnessInput);
                contrast = Double.parseDouble(contrastInput);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("brightness_contrast_not_number"),
                        I18nManager.get("error_title"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            target.getImage().apply(new BrightnessAndContrastAdjustment(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}

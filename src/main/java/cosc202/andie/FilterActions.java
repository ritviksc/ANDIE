package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 *
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood. This includes a mean filter (a simple blur)
 * in the sample code, but more operations will need to be added.
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @author Steven Mills, Robert Hannaford
 * @version 1.1
 */
public class FilterActions {

    /**
     * A list of actions for the Filter menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        actions = new ArrayList<>();
        actions.add(new MeanFilterAction(I18nManager.get("Mean"), null, I18nManager.get("Mean_desc"), KeyEvent.VK_M));
        actions.add(new SharpenFilterAction(I18nManager.get("Sharpen"), null, I18nManager.get("Sharpen_desc"), KeyEvent.VK_S));
        actions.add(new GaussianFilterAction(I18nManager.get("Gaussian"), null, I18nManager.get("Gaussian_desc"), KeyEvent.VK_G));
        actions.add(new MedianFilterAction(I18nManager.get("Median"), null, I18nManager.get("Median_desc"), KeyEvent.VK_E));
    }

    /**
     * <p>
     * Create a menu containing the list of Filter actions.
     * </p>
     *
     * @return The filter menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(I18nManager.get("Filter_title"));

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to blur an image with a mean filter.
     * </p>
     *
     * @see MeanFilter
     */
    public class MeanFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the mean-filter-action action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the MeanFilterAction is triggered. It
         * prompts the user for a filter radius, then applies an appropriately
         * sized {@link MeanFilter}.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, I18nManager.get("Filter_no_image"), I18nManager.get("Filter_error_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Determine the radius - ask the user.
            int radius = 1;

            do {
                

                int minRadius = 0;
                int maxRadius = 10;

                SpinnerNumberModel radiusModel = new SpinnerNumberModel(minRadius, minRadius, maxRadius, 1);

                JSpinner radiusSpinner = new JSpinner(radiusModel);

                JSpinner.NumberEditor editor = new JSpinner.NumberEditor(radiusSpinner, "#");
                JFormattedTextField radiusInput = editor.getTextField();
                radiusSpinner.setEditor(editor);
                
                NumberFormatter formatter = (NumberFormatter) radiusInput.getFormatter();
                
                formatter.setValueClass(Integer.class);

                formatter.setAllowsInvalid(false);
                formatter.setCommitsOnValidEdit(true);
                
                int option = JOptionPane.showOptionDialog(null, radiusSpinner, I18nManager.get("Filter_radius_msg"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                    // Check the return value from the dialog box.
                
                if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    radius = radiusModel.getNumber().intValue();
                }
            }while (radius > 10 || radius <= 0); //Enforce a hard limit on a O(n^4) operation

            // Create and apply the filter
            target.getImage().apply(new MeanFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to sharpen an image with a sharpening filter.
     * </p>
     *
     * @see SharpenFilter
     */
    public class SharpenFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the sharpen-filter-action action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * It then applies a {@link SharpenFilter}
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, I18nManager.get("Filter_no_image"), I18nManager.get("Filter_error_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and apply the filter
            target.getImage().apply(new SharpenFilter());
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to apply a Gaussian blur an image with a Gaussian blurring filter.
     * </p>
     *
     * @see GaussianFilter
     */
    public class GaussianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Gaussian-filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        GaussianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Gaussian-filter-action action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the GaussianFilterAction is triggered.
         * It then applies a {@link GaussianFilter}
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, I18nManager.get("Filter_no_image"), I18nManager.get("Filter_error_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Determine the radius - ask the user.
            int radius = 1;

            do {
                

                int minRadius = 0;
                int maxRadius = 10;

                SpinnerNumberModel radiusModel = new SpinnerNumberModel(minRadius, minRadius, maxRadius, 1);

                JSpinner radiusSpinner = new JSpinner(radiusModel);

                JSpinner.NumberEditor editor = new JSpinner.NumberEditor(radiusSpinner, "#");
                JFormattedTextField radiusInput = editor.getTextField();
                radiusSpinner.setEditor(editor);
                
                NumberFormatter formatter = (NumberFormatter) radiusInput.getFormatter();
                
                formatter.setValueClass(Integer.class);

                formatter.setAllowsInvalid(false);
                formatter.setCommitsOnValidEdit(true);
                
                int option = JOptionPane.showOptionDialog(null, radiusSpinner, I18nManager.get("Filter_radius_msg"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                    // Check the return value from the dialog box.
                
                if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    radius = radiusModel.getNumber().intValue();
                }
            }while (radius > 10 || radius <= 0); //Enforce a hard limit on a O(n^4) operation

            // Create and apply the filter
            target.getImage().apply(new GaussianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to apply a Median blur an image with a Median blurring filter.
     * </p>
     *
     * @see MedianFilter
     */
    public class MedianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Median-filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Median-filter-action action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It then applies a {@link MedianFilter}
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, I18nManager.get("Filter_no_image"), I18nManager.get("Filter_error_title"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Determine the radius - ask the user.
            int radius = 0;
            do {
                

                int minRadius = 0;
                int maxRadius = 10;

                SpinnerNumberModel radiusModel = new SpinnerNumberModel(minRadius, minRadius, maxRadius, 1);

                JSpinner radiusSpinner = new JSpinner(radiusModel);

                JSpinner.NumberEditor editor = new JSpinner.NumberEditor(radiusSpinner, "#");
                JFormattedTextField radiusInput = editor.getTextField();
                radiusSpinner.setEditor(editor);
                
                NumberFormatter formatter = (NumberFormatter) radiusInput.getFormatter();
                
                formatter.setValueClass(Integer.class);

                formatter.setAllowsInvalid(false);
                formatter.setCommitsOnValidEdit(true);
                
                int option = JOptionPane.showOptionDialog(null, radiusSpinner, I18nManager.get("Filter_radius_msg_median"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                    // Check the return value from the dialog box.
                
                if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    radius = radiusModel.getNumber().intValue();
                }
            }while (radius > 10 || radius <= 0); //Enforce a hard limit on a O(n^4) operation
                // Create and apply the filter
                target.getImage().apply(new MedianFilter(radius));
                target.repaint();
                target.getParent().revalidate();
            }

        }

    }

package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 *
 * <p>
 * The Edit menu is very common across a wide range of applications. There are a
 * lot of operations that a user might expect to see here. In the sample code
 * there are Undo and Redo actions, but more may need to be added.
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 *
 * @author Steven Mills
 * @version 1.0
 */
public class EditActions {

    /**
     * A list of actions for the Edit menu.
     */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<>();
        actions.add(new UndoAction(
            I18nManager.get("Undo"),
            null,
            I18nManager.get("Undo_desc"),
            KeyEvent.VK_Z
        ));

        actions.add(new RedoAction(
            I18nManager.get("Redo"),
            null,
            I18nManager.get("Redo_desc"),
            KeyEvent.VK_Y
        ));

        actions.add(new ResizeAction(
            I18nManager.get("Resize"),
            null,
            I18nManager.get("Resize_desc"),
            KeyEvent.VK_R
        ));

        actions.add(new RotateAction90C(
            I18nManager.get("Rotate90C"),
            null,
            I18nManager.get("Rotate90C_desc"),
            KeyEvent.VK_F
        ));

        actions.add(new RotateAction90CC(
            I18nManager.get("Rotate90CC"),
            null,
            I18nManager.get("Rotate90CC_desc"),
            KeyEvent.VK_1
        ));

        actions.add(new RotateAction180(
            I18nManager.get("Rotate180"),
            null,
            I18nManager.get("Rotate180_desc"),
            KeyEvent.VK_2
        ));

        actions.add(new FlipActionHorizontal(
            I18nManager.get("FlipHorizontal"),
            null,
            I18nManager.get("FlipHorizontal_desc"),
            KeyEvent.VK_3
        ));

        actions.add(new FlipActionVertically(
            I18nManager.get("FlipVertical"),
            null,
            I18nManager.get("FlipVertical_desc"),
            KeyEvent.VK_V
        ));
    }

    /**
     * <p>
     * Create a menu containing the list of Edit actions.
     * </p>
     *
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        JMenu editMenu = new JMenu(I18nManager.get("Edit_title"));

        for (Action action : actions) {
            editMenu.add(new JMenuItem(action));
        }

        return editMenu;
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     *
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the UndoAction is triggered. It undoes
         * the most recently applied operation.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            target.getImage().undo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     *
     * @see EditableImage#redo()
     */
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the RedoAction is triggered. It redoes
         * the most recently undone operation.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            target.getImage().redo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to resize an image {@link ImageOperation}.
     * </p>
     *
     * @see ImageResize
     */
    public class ResizeAction extends ImageAction {

        /**
         * <p>
         * Create a new resize action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        ResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the resize action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the ResizeAction is triggered. It resizes the image by specfic percentage (out of 100) as set by user.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            // Determine the scale factor percentage given
            int factor = 100;

            if (!target.getImage().hasImage()){
                // Show Error Dialog if no image is loaded
                JOptionPane.showMessageDialog(
                    null,                        
                    I18nManager.get("error_resize"), // message
                    "Error",                      // title
                    JOptionPane.ERROR_MESSAGE     
                );
                return;
            }

            // Pop-up dialog box to ask for the factor value.
            SpinnerNumberModel factorModel = new SpinnerNumberModel(100, 10, 200, 10);
            JSpinner factorSpinner = new JSpinner(factorModel);
            int option = JOptionPane.showOptionDialog(null, factorSpinner, I18nManager.get("scale_percentage"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(factorSpinner, "#");
            JFormattedTextField radiusInput = editor.getTextField();
            factorSpinner.setEditor(editor);
                
            NumberFormatter formatter = (NumberFormatter) radiusInput.getFormatter();
                
            formatter.setValueClass(Integer.class);

            formatter.setAllowsInvalid(false);
            formatter.setCommitsOnValidEdit(true);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                factor = factorModel.getNumber().intValue();
            }

            if (target.getImage().hasImage()){
                target.getImage().apply(new ImageResize((double)factor));
                target.repaint();
                target.getParent().revalidate();
            } 
        }
    }

     /**
     * <p>
     * Action to flip an image horizontally {@link ImageOperation}.
     * </p>
     *
     * @see ImageFlipHorizontal#apply(java.awt.image.BufferedImage)
     */
    public class FlipActionHorizontal extends ImageAction {

        /**
         * <p>
         * Create a new horizontal flip action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FlipActionHorizontal(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the flip action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FlipActionHorizontally is triggered. It flips image horizontally as requested by user.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {


            if (!target.getImage().hasImage()){
                // Show Error Dialog if no image is loaded
                JOptionPane.showMessageDialog(
                    null,                        
                    I18nManager.get("error_flip"), // message
                    "Error",                      // title
                    JOptionPane.ERROR_MESSAGE     
                );
                return;
            }

            if (target.getImage().hasImage()){
                target.getImage().apply(new ImageFlipHorizontal());
                target.repaint();
                target.getParent().revalidate();
            } 
        }
    }

         /**
     * <p>
     * Action to flip an image vertically {@link ImageOperation}.
     * </p>
     *
     * @see ImageFlipVertically#apply(java.awt.image.BufferedImage)
     */
    public class FlipActionVertically extends ImageAction {

        /**
         * <p>
         * Create a new vertical flip action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        FlipActionVertically(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the flip action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the FlipActionVertically is triggered. It flips image vertically as requested by user.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {


            if (!target.getImage().hasImage()){
                // Show Error Dialog if no image is loaded
                JOptionPane.showMessageDialog(
                    null,                        
                    I18nManager.get("error_flip"), // message
                    "Error",                      // title
                    JOptionPane.ERROR_MESSAGE     
                );
                return;
            }

            if (target.getImage().hasImage()){
                target.getImage().apply(new ImageFlipVertically());
                target.repaint();
                target.getParent().revalidate();
            } 
        }
    }

    /**
     * <p>
     * Action to rotate an image 90 degrees clock wise {@link ImageOperation}.
     * </p>
     *
     * @see ImageRotate90C#apply(java.awt.image.BufferedImage)
     */
    public class RotateAction90C extends ImageAction {

        /**
         * <p>
         * Create a new horizontal rotate action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        RotateAction90C(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the RotateAction90C is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the RotateAction90C is triggered. It rotates image as requested by user.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {


            if (!target.getImage().hasImage()){
                // Show Error Dialog if no image is loaded
                JOptionPane.showMessageDialog(
                    null,                        
                    I18nManager.get("error_rotate"), // message
                    "Error",                      // title
                    JOptionPane.ERROR_MESSAGE     
                );
                return;
            }

            if (target.getImage().hasImage()){
                target.getImage().apply(new ImageRotate90C());
                target.repaint();
                target.getParent().revalidate();
            } 
        }
    }

    /**
     * <p>
     * Action to rotate an image 90 degrees counter clock wise {@link ImageOperation}.
     * </p>
     *
     * @see ImageRotate90CC#apply(java.awt.image.BufferedImage)
     */
    public class RotateAction90CC extends ImageAction {

        /**
         * <p>
         * Create a new rotate action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        RotateAction90CC(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the RotateAction90CC is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the RotateAction90CC is triggered. It flips rotates image requested by user.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {


            if (!target.getImage().hasImage()){
                // Show Error Dialog if no image is loaded
                JOptionPane.showMessageDialog(
                    null,                        
                    I18nManager.get("error_rotate"), // message
                    "Error",                      // title
                    JOptionPane.ERROR_MESSAGE     
                );
                return;
            }

            if (target.getImage().hasImage()){
                target.getImage().apply(new ImageRotate90CC());
                target.repaint();
                target.getParent().revalidate();
            } 
        }
    }

    /**
     * <p>
     * Action to rotate an image 180 degrees {@link ImageOperation}.
     * </p>
     *
     * @see ImageRotate180#apply(java.awt.image.BufferedImage)
     */
    public class RotateAction180 extends ImageAction {

        /**
         * <p>
         * Create a new rotate action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if
         * null).
         */
        RotateAction180(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the RotateAction180 is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the RotateAction180 is triggered. It rotates image as requested by user.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        @Override
        public void actionPerformed(ActionEvent e) {


            if (!target.getImage().hasImage()){
                // Show Error Dialog if no image is loaded
                JOptionPane.showMessageDialog(
                    null,                        
                    I18nManager.get("error_rotate"), // message
                    "Error",                      // title
                    JOptionPane.ERROR_MESSAGE     
                );
                return;
            }

            if (target.getImage().hasImage()){
                target.getImage().apply(new ImageRotate180());
                target.repaint();
                target.getParent().revalidate();
            } 
        }
    }



    

}

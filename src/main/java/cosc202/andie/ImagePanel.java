
package cosc202.andie;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * <p>
 * UI display element for {@link EditableImage}s.
 * </p>
 *
 * <p>
 * This class extends {@link JPanel} to allow for rendering of an image, as well
 * as zooming in and out.
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
public class ImagePanel extends JPanel {

    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;
    private Point startPoint; // Where the user clicks first
    private Point endPoint; // Where user releases mouse after dragging it
    private Rectangle selection; // Rectangle formed between starPoint and endPoint

    /**
     * <p>
     * The zoom-level of the current view. A scale of 1.0 represents actual
     * size; 0.5 is zoomed out to half size; 1.5 is zoomed in to one-and-a-half
     * size; and so forth.
     * </p>
     *
     * <p>
     * Note that the scale is internally represented as a multiplier, but
     * externally as a percentage.
     * </p>
     */
    private double scale;

    /**
     * A boolean to check if the window is ready to be closed
     */
    public static boolean windowClosed = true;

    /**
     * <p>
     * Create a new ImagePanel and register mouse actions.
     * Using those mouse actions, determine what Image Operation is to be carried
     * out, if it is the case.
     * </p>
     *
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * </p>
     */
    public ImagePanel() {
        image = new EditableImage();
        scale = 1.0;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!image.hasImage())
                    return;
                startPoint = new Point(
                        (int) (e.getX() / scale),
                        (int) (e.getY() / scale));
                endPoint = startPoint;
                selection = null;
            }

            public void mouseReleased(MouseEvent e) {
                if (!image.hasImage())
                    return;
                endPoint = new Point(
                        (int) (e.getX() / scale),
                        (int) (e.getY() / scale));

                updateSelection();
                repaint();

                if (selection == null) return;
                
                if (selection != null) {
                    String[] options = {
                            I18nManager.get("crop_rectangle"),
                            I18nManager.get("crop_ellipse"),
                            I18nManager.get("draw_line"),
                            I18nManager.get("draw_rectangle"),
                            I18nManager.get("draw_ellipse"),
                            I18nManager.get("cancel_crop")
                    };

                    int choice = JOptionPane.showOptionDialog(
                            ImagePanel.this,
                            I18nManager.get("crop_action"),
                            I18nManager.get("crop_selection"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                    switch (choice) {
                        case 0: // Crop as rectangle
                            Rectangle normalized = getSelectionRectangle(startPoint, endPoint);
                            ImageOperation op = new CropOperation(normalized);
                            image.apply(op);
                            selection = null;
                            repaint();
                            break;
                        case 1: // Crop as ellipse
                            Rectangle normalizedE = getSelectionRectangle(startPoint, endPoint);
                            ImageOperation opEllipse = new CropEllipse(normalizedE);
                            image.apply(opEllipse);
                            selection = null;
                            repaint();
                            break;
                        case 2: // Draw line
                            ShapeOptions lineOptions = getShapeOptions();
                            if (lineOptions != null) {
                                ImageOperation opLine = new DrawLineOperation(lineOptions.color, startPoint, endPoint);
                                image.apply(opLine);
                            }
                            selection = null;
                            repaint();
                            break;
                        case 3: // Rectangle
                            ShapeOptions rectOptions = getShapeOptions();
                            if (rectOptions != null) {
                                ImageOperation opR = new DrawRectangle(selection, rectOptions.color,
                                        rectOptions.filled);
                                image.apply(opR);
                            }
                            selection = null;
                            repaint();
                            break;

                        case 4: // Ellipse
                            ShapeOptions ellipseOptions = getShapeOptions();
                            if (ellipseOptions != null) {
                                ImageOperation opE = new DrawEllipse(selection, ellipseOptions.color,
                                        ellipseOptions.filled);
                                image.apply(opE);
                            }
                            selection = null;
                            repaint();
                            break;
                        default:
                            selection = null;
                            repaint();
                            break;
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!image.hasImage())
                    return;
                endPoint = new Point(
                        (int) (e.getX() / scale),
                        (int) (e.getY() / scale));
                updateSelection();
                repaint();
            }
        });
    }

    /**
     * <p>
     * Get the currently displayed image
     * </p>
     *
     * @return the image currently displayed.
     */
    @SuppressWarnings("NonPublicExported")
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     *
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc.
     * </p>
     *
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100 * scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     *
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc. The zoom level is restricted to the
     * range [50, 200].
     * </p>
     *
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        if (zoomPercent < 50) {
            zoomPercent = 50;
        }
        if (zoomPercent > 200) {
            zoomPercent = 200;
        }
        scale = zoomPercent / 100;
    }

    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     *
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a
     * default size if no image is present.
     * </p>
     *
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            return new Dimension((int) Math.round(image.getCurrentImage().getWidth() * scale),
                    (int) Math.round(image.getCurrentImage().getHeight() * scale));
        } else {
            return new Dimension(450, 450);
        }
    }

    // Update rectangular area the user is highlighting as the mouse is dragged
    private void updateSelection() {

        int x1 = startPoint.x;
        int y1 = startPoint.y;
        int x2 = endPoint.x;
        int y2 = endPoint.y;

        int imgWidth = image.getCurrentImage().getWidth();
        int imgHeight = image.getCurrentImage().getHeight();

        x1 = Math.max(0, Math.min(x1, imgWidth));
        y1 = Math.max(0, Math.min(y1, imgHeight));
        x2 = Math.max(0, Math.min(x2, imgWidth));
        y2 = Math.max(0, Math.min(y2, imgHeight));

        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        selection = new Rectangle(x, y, width, height);
    }

    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     *
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image.hasImage()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scale, scale);
            g2.drawImage(image.getCurrentImage(), null, 0, 0);

            if (selection != null) {
                g2.setColor(new Color(0, 0, 255, 50));
                g2.fillRect(selection.x, selection.y, selection.width, selection.height);

                g2.setColor(Color.BLUE);
                g2.drawRect(selection.x, selection.y, selection.width, selection.height);
            }
            g2.dispose();
        }

    }

    // Get rectangle area that user is highlighting with proper coordinates
    private Rectangle getSelectionRectangle(Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int w = Math.abs(end.x - start.x);
        int h = Math.abs(end.y - start.y);
        return new Rectangle(x, y, w, h);
    }

    /**
     * <p>
     * Gets the preferred options for the object to be drawn
     * </p>
     *
     * <p>
     * A panel is launched so the user can select the color, and 'solidness' of the
     * object to be drawn.
     * 
     * </p>
     *
     * @return Instance of the ShapeOptions class that collects users preferences
     *         and uses those for
     *         operation to be carried out
     */
    private ShapeOptions getShapeOptions() {
        JColorChooser colorChooser = new JColorChooser();
        String[] styles = { I18nManager.get("outline"), I18nManager.get("solid") };
        JComboBox<String> styleBox = new JComboBox<>(styles);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(styleBox, BorderLayout.NORTH);
        panel.add(colorChooser, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this, // child of Image Panel
                panel,
                I18nManager.get("shape_options"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Color chosenColor = colorChooser.getColor();
            boolean isFilled = styleBox.getSelectedItem().equals("Solid");
            return new ShapeOptions(chosenColor, isFilled);
        }

        return null; // cancelled
    }

}

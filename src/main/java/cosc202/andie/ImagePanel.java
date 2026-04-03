
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
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener{

    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;
    private Point startPoint;
    private Point endPoint;
    private Rectangle selection;

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
            if (!image.hasImage()) return;
            startPoint = e.getPoint();
            endPoint = startPoint;
            selection = null;
        }

        public void mouseReleased(MouseEvent e) {
            if (!image.hasImage()) return;
            endPoint = e.getPoint();
            updateSelection();
            repaint();

            if (selection != null) {
                int result = JOptionPane.showConfirmDialog(
                        ImagePanel.this,
                        "Crop selected region as new image?",
                        "Crop",
                        JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    cropImage();
                } else {
                    selection = null;
                    repaint();
                }
            }
        }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
            if (!image.hasImage()) return;
            endPoint = e.getPoint();
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

    private void cropImage() {
        if (selection.width <= 0 || selection.height <= 0) return;

        BufferedImage cropped = image.getCurrentImage().getSubimage(
                selection.x,
                selection.y,
                selection.width,
                selection.height
        );

        image.setCurrentImage(cropped);
        image.setOriginal(cropped);
        // refresh variables
        startPoint = null;
        endPoint = null;
        selection = null;
        repaint();
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

    private void updateSelection() {
        int x1 = (int)(startPoint.x / scale);
        int y1 = (int)(startPoint.y / scale);
        int x2 = (int)(endPoint.x / scale);
        int y2 = (int)(endPoint.y / scale);

        if (image.getCurrentImage().getWidth() - startPoint.x < 0 || image.getCurrentImage().getHeight() - startPoint.y < 0 ){
            return;
        } 

        if (image.getCurrentImage().getWidth() - endPoint.x < 0 || image.getCurrentImage().getHeight() - endPoint.y < 0 ){
            return;
        } 

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
                int x = (int)(selection.x * scale);
                int y = (int)(selection.y * scale);
                int w = (int)(selection.width * scale);
                int h = (int)(selection.height * scale);

                g2.setColor(new Color(0, 0, 255, 50)); // transparent blue
                g2.fillRect(x, y, w, h);

                g2.setColor(Color.BLUE); // solid border
                g2.drawRect(x, y, w, h);
            }
            g2.dispose();
        }

        
    }

    // Unused functions
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

}

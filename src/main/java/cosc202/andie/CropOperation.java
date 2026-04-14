package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/* Class for cropping a rectangle, which is considerd the default operation when a user selects a area to manipulate given a image */
public class CropOperation implements ImageOperation, java.io.Serializable {

    private Rectangle selection;

    public CropOperation(Rectangle selection) {
        this.selection = selection;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        int x = Math.max(0, selection.x);
        int y = Math.max(0, selection.y);

        // Make sure selected rectangle doesn't go out of bounds to prevent raster error.
        int w = Math.min(selection.width, input.getWidth() - x);
        int h = Math.min(selection.height, input.getHeight() - y);

        if (w <= 0 || h <= 0) {
            return input; // invalid selection!
        }

        BufferedImage sub = input.getSubimage(x, y, w, h);
        BufferedImage copy = new BufferedImage(
            sub.getWidth(),
            sub.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(sub, 0, 0, null);
        g2.dispose();

        return copy; 
    }
}

package cosc202.andie;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

/* Crop ellipse out of selected area in the image loaded */
public class CropEllipse implements ImageOperation {

    private Rectangle selection;

    public CropEllipse(Rectangle selection) {
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
            return input; // invalid selection, do nothing
        }

        // Create a new image with alpha (transparent background)
        BufferedImage newImage = new BufferedImage(
            w,
            h,
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = newImage.createGraphics();

        // Clear with transparent
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, w, h);

        // Draw the original image only inside the ellipse
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setClip(new Ellipse2D.Float(0, 0, w, h));
        g2.drawImage(input, -x, -y, null);

        g2.dispose();
        return newImage;
    }
}

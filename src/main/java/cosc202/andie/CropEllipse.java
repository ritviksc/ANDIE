package cosc202.andie;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

public class CropEllipse implements ImageOperation {

    private Rectangle selection;

    public CropEllipse(Rectangle selection) {
        this.selection = selection;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        // Create a new image with alpha (transparent background)
        BufferedImage newImage = new BufferedImage(
            selection.width,
            selection.height,
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = newImage.createGraphics();

        // Clear with transparent
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, selection.width, selection.height);

        // Draw the original image only inside the ellipse
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setClip(new Ellipse2D.Float(0, 0, selection.width, selection.height));
        g2.drawImage(input, -selection.x, -selection.y, null);

        g2.dispose();
        return newImage;
    }
}

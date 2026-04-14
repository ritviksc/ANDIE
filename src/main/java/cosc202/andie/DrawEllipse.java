package cosc202.andie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/* Class for drawing a ellipse on a given image. */
public class DrawEllipse implements ImageOperation,java.io.Serializable {

    private Rectangle rect; // Area of image to be used to draw the ellipse
    private Color color; // Color user wants
    private boolean filled; // filled or solid ellipse

    public DrawEllipse(Rectangle rect, Color color, boolean filled) {
        this.rect = rect;
        this.color = color;
        this.filled = filled;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        BufferedImage copy = new BufferedImage(
            input.getWidth(),
            input.getHeight(),
            input.getType()
        );

        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(input, 0, 0, null);

        g2.setColor(color);

        Ellipse2D ellipse = new Ellipse2D.Float(
            rect.x, rect.y, rect.width, rect.height
        );

        if (filled) {
            g2.fill(ellipse);
        } else {
            g2.draw(ellipse);
        }

        g2.dispose();
        return copy;
    }
}

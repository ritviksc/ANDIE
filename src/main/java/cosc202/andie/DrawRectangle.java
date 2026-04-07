package cosc202.andie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/* Class for drawing a rectangle (filled or solid) depending on what the user wants. */
public class DrawRectangle implements ImageOperation {

    private Rectangle rect; // Portion of image to draw rectangle on
    private Color color; // get color user wants 
    private boolean filled; // filled or not

    public DrawRectangle(Rectangle rect, Color color, boolean filled) {
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

        if (filled) {
            g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        } else {
            g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        }

        g2.dispose();
        return copy;
    }
}

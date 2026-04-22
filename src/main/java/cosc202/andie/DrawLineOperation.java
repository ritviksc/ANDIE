package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;

/* Class to draw a Line on the image. It is a image operation that can be undone.*/
public class DrawLineOperation implements ImageOperation, java.io.Serializable {
    private Point start; // Top point of the line
    private Point end;  // Bottom point of the line
    private Color col; // Color user wants

    public DrawLineOperation(Color col,Point start, Point end) {
        this.start = start;
        this.end = end;
        this.col = col;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        BufferedImage copy = new BufferedImage(
            input.getWidth(),
            input.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(input, 0, 0, null);

        g2.setColor(col);
        g2.drawLine(start.x, start.y, end.x, end.y);

        g2.dispose();
        return copy;
    }
}

package cosc202.andie;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;

public class DrawLineOperation implements ImageOperation {
    private Point start;
    private Point end;

    public DrawLineOperation(Point start, Point end) {
        this.start = start;
        this.end = end;
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

        g2.setColor(Color.RED);
        g2.drawLine(start.x, start.y, end.x, end.y);

        g2.dispose();
        return copy;
    }
}

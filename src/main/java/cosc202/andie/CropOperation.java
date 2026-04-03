package cosc202.andie;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CropOperation implements ImageOperation {

    private Rectangle selection;

    public CropOperation(Rectangle selection) {
        this.selection = selection;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        return input.getSubimage(
            selection.x,
            selection.y,
            selection.width,
            selection.height
        );
    }
}

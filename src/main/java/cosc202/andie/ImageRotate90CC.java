package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to rotate a image by 90 degrees counter clockwise.
 * </p>
 *
 * 
 * @author Ritvik Sharma
 * @version 1.0
 */
public class ImageRotate90CC implements ImageOperation,java.io.Serializable {

    @Override
    public BufferedImage apply(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();

        BufferedImage output = new BufferedImage(height, width, input.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int newX = y;
                int newY = width - 1 - x;

                output.setRGB(newX, newY, input.getRGB(x, y));
            }
        }

        return output;
    }
    
}

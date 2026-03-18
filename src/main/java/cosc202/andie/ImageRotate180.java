package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to rotate a image by 180 degrees.
 * </p>
 *
 * 
 * @author Ritvik Sharma
 * @version 1.0
 */
public class ImageRotate180 implements ImageOperation,java.io.Serializable {
    
    @Override
    public BufferedImage apply(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();

        BufferedImage output = new BufferedImage(width, height, input.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int newX = width - 1 - x;
                int newY = height - 1 - y;

                output.setRGB(newX, newY, input.getRGB(x, y));
            }
        }

        return output;
    }
}

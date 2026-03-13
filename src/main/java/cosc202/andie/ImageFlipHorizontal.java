package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to flip a image by horizontally.
 * </p>
 *
 * <p>
 *  Flip image in place horizontally by swapping pixels in each row
 * </p>
 * 
 * @author Ritvik Sharma
 * @version 1.0
 */
public class ImageFlipHorizontal implements ImageOperation, java.io.Serializable {

    public ImageFlipHorizontal(){}

    // Default apply will flip image vertically in place
    @Override
    public BufferedImage apply(BufferedImage input) {
        int imageWidth = input.getWidth(); // Get width of image
        int imageHeight = input.getHeight(); // Get height of image

        // Horizontal Flip (each row is flipped)
        for (int j = 0; j < imageHeight; j++){
            for (int i = 0; i < imageWidth /2; i++){
                int oppositeXPixel = (imageWidth -1) - i;
                int temp = input.getRGB(i, j); // save current pixel state 
                input.setRGB(i, j, input.getRGB(oppositeXPixel, j));
                input.setRGB(oppositeXPixel, j, temp); // swap

            }
        }

        return input;

    }

    
}

package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to flip a image by vertically.
 * </p>
 *
 * <p>
 *  Flip image vertically by flipping each column.
 * </p>
 * 
 * @author Ritvik Sharma
 * @version 1.0
 */
public class ImageFlipVertically implements ImageOperation, java.io.Serializable {

    public ImageFlipVertically(){}

    // Flip image vertically in place
    @Override
    public BufferedImage apply(BufferedImage input){
        int imageWidth = input.getWidth(); // Get width of image
        int imageHeight = input.getHeight(); // Get height of image

        // Vertical Flip
        for (int j = 0; j < imageHeight/2; j++){
            int oppositeYPixel = (imageHeight -1) - j;
            for (int i = 0; i < imageWidth; i++){
                int temp = input.getRGB(i, j); // save current pixel state 
                input.setRGB(i, j, input.getRGB(i, oppositeYPixel));
                input.setRGB(i, oppositeYPixel, temp); // swap

            }
        }

        return input;
    }
    
}

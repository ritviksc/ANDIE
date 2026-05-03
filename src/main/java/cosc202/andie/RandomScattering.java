package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>
 * ImageOperation to apply a random scattering effect to an image
 * </p>
 * 
 * <P>
 * For each pixel in the output image, a random pixel is chosen from within
 * a square radius around the original pixel location. The chosen pixel colour
 * is copied into the output image.
 * </p>
 * @author malee
 * @version 1.0
 */
public class RandomScattering implements ImageOperation, java.io.Serializable {
    // Radius around each pixel to randomly sample from
    private final int radius;
    
    // Random Number generator used to choose nearby pixels
    private final Random random;
    
    // COnstruct a random scattering operation with given radius
    public RandomScattering(int radius) {
        this.radius = radius;
        this.random = new Random();
    }
    
    //Apply the random scattering image to an image
    public BufferedImage apply(BufferedImage input){
        int width = input.getWidth();
        int height = input.getHeight();
        
        BufferedImage output = new BufferedImage( width, height, input.getType());
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int randomX = x + random.nextInt(radius * 2 + 1) - radius;
                int randomY = y + random.nextInt(radius * 2 + 1) - radius;

                randomX = Math.max(0, Math.min(randomX, width - 1));
                randomY = Math.max(0, Math.min(randomY, height - 1));

                int rgb = input.getRGB(randomX, randomY);
                output.setRGB(x, y, rgb);
            }
        }

        return output;
    }
}

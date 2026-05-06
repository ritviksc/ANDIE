package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>
 * ImageOperation to apply a random scattering effect to an image
 * </p>
 *
 * <p>
 * For each pixel in the output image, a random pixel is chosen from within a
 * square radius around the original pixel location. The colour of the chosen 
 * pixel is copied into the output image.
 * </p>
 *
 * @author malee
 * @version 1.0
 */
public class RandomScattering implements ImageOperation, java.io.Serializable {

    /**
     * Radius around each pixel used for random sampling.
     */
    private final int radius;

    /**
     * Random number generator used to choose nearby pixels.
     */
    private final Random random;

    /**
     * Creates a random scattering effect with the specified radius.
     *
     * @param radius The maximum distance from each pixel to randomly sample
     * from.
     */
    public RandomScattering(int radius) {
        this.radius = radius;
        this.random = new Random();
    }

    /**
     * Applies the random scattering effect to an image.
     *
     * @param input The image to process.
     * @return A new image with randomly scattered pixels.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            return null;
        }
        int width = input.getWidth();
        int height = input.getHeight();

        BufferedImage output = new BufferedImage(width, height, input.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Generate a random nearby coordinate within the radius.
                int randomX = x + random.nextInt(radius * 2 + 1) - radius;
                int randomY = y + random.nextInt(radius * 2 + 1) - radius;

                // Clamp coordinates so they remain inside image bounds.
                randomX = Math.max(0, Math.min(randomX, width - 1));
                randomY = Math.max(0, Math.min(randomY, height - 1));

                // Copy the randomly selected pixel colour into the output image.
                int rgb = input.getRGB(randomX, randomY);
                output.setRGB(x, y, rgb);
            }
        }

        return output;
    }
}

package cosc202.andie;

/**
 * <p>
 * ImageOperation to invert the colors of an image.
 * </p>
 *
 * <p>
 * The inversion filter converts each pixel in a colour image to its
 * complementary color. Each RGB component of a pixel is subtracted from 255,
 * effectively producing a negative of the original image. The original alpha
 * (transparency) of the pixel is preserved.
 * </p>
 *
 * @author Maleena Taia
 * @version 1.0
 */
import java.awt.image.BufferedImage;
import java.awt.Color;

import javax.swing.JOptionPane;

public class ImageInversion implements ImageOperation, java.io.Serializable {

    @Override
    public BufferedImage apply(BufferedImage input) {

        if (input == null) {
        // Return null or throw a warning dialog
        JOptionPane.showMessageDialog(null, I18nManager.get("inversion_no_image"), I18nManager.get("error_title"), JOptionPane.ERROR_MESSAGE);
        return input; 
    }
        /**
         * <p>
         * Apply the color inversion filter to an input image.
         * </p>
         *
         * <p>
         * Iterates through each pixel of the input image, calculates the
         * inverted RGB components (255 - original value), and sets the output
         * pixel to this new color.
         * </p>
         *
         * @param input The image to invert.
         * @return A new BufferedImage where all pixels are color-inverted.
         */

        // Get dimensions of the input image
        int width = input.getWidth();
        int height = input.getHeight();

        // Create a new output image with same width, height, and type
        BufferedImage output = new BufferedImage(width, height, input.getType());

        // Loop through every row of the image
        for (int y = 0; y < height; y++) {
            // Loop through every column of the image
            for (int x = 0; x < width; x++) {

                // Get the current pixel color
                Color color = new Color(input.getRGB(x, y), true);

                // Extract RGB components
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();

                // Compute inverted RGB values
                int newR = 255 - r;
                int newG = 255 - g;
                int newB = 255 - b;

                // Create a new color with inverted values and original alpha
                Color newColor = new Color(newR, newG, newB, a);

                // Set the inverted color in the output image
                output.setRGB(x, y, newColor.getRGB());
            }
        }

        // Return the color-inverted image
        return output;
    }
}

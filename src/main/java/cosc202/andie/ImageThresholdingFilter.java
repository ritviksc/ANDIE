package cosc202.andie;

import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 * <p>
 * ImageOperation to apply a thresholding filter to an image.
 * </p>
 *
 * <p>
 * The thresholding filter converts a colour image into black and white image
 * based on a specified intensity threshold. Pixels with brightness above the
 * threshold become white, and those below become black. The original alpha
 * (transparency) of the pixel is preserved.
 * </p>
 *
 * @author Maleena Taia
 * @version 1.0
 */
public class ImageThresholdingFilter implements ImageOperation {

    /**
     * <p>
     * The intensity threshold for black and white conversion. Pixels with
     * average brightness above this value become white, others black
     * </p>
     */
    private final int threshold;

    /**
     * <p>
     * Construct a threshold filter with the given threshold.
     * </p>
     *
     * @param threshold The intensity threshold (0-255) used for black-and-white
     * conversion.
     */
    public ImageThresholdingFilter(int threshold) {
        // Validate threshold (error prevention)
        if (threshold < 0 || threshold > 255) {
            this.threshold = 128; // default safe value
        } else {
            this.threshold = threshold;
        }
    }

    /**
     * <p>
     * Apply the thresholding filter to an input image.
     * </p>
     *
     * <p>
     * Iterates through each pixel of the input image, calculates its average
     * brightness, and sets the output pixel to black or white based on the
     * threshold.
     * </p>
     *
     * @param input The image to apply the threshold filter to.
     * @return A new BufferedImage where all pixels are black or white based on
     * the threshold.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {

        // Exception handling: null check
        if (input == null) {
            JOptionPane.showMessageDialog(
                    null,
                    I18nManager.get("threshold_no_image"),
                    I18nManager.get("error_title"),
                    JOptionPane.WARNING_MESSAGE
            );
            return input;
        }

        int width = input.getWidth();
        int height = input.getHeight();
        // Creates a new image with the same width, height, and type as input
        BufferedImage output = new BufferedImage(width, height, input.getType());

        // Loop through every row of the image
        for (int y = 0; y < input.getHeight(); y++) {
            // Loop through every column of the image
            for (int x = 0; x < input.getWidth(); x++) {

                // Get pixel value at (x, y) as a 32-bit integer ARGB
                int rgb = input.getRGB(x, y);

                // Extract ARGB components, all masked with 0xFF
                int a = (rgb >> 24) & 0xFF; // shift 24 bits to right
                int r = (rgb >> 16) & 0xFF; // shift 16 bits to right
                int g = (rgb >> 8) & 0xFF; // shift 8 bits to right
                int b = rgb & 0xFF; // masks last 8 bits

                // Compute average brightness of pixels
                int intensity = (r + g + b) / 3;

                // Compare intensity with threshold
                if (intensity > threshold) {
                    // Set pixel to white RGB = 255
                    int white = (a << 24) | (255 << 16) | (255 << 8) | 255;
                    output.setRGB(x, y, white); // sets pixels in output image white
                } else {
                    // Set pixel to black RGB = 0
                    int black = (a << 24);
                    output.setRGB(x, y, black); // sets pixels in output image black
                }
            }
        }

        return output;
    }
}

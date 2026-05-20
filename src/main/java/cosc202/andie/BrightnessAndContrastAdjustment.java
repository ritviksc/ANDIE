package cosc202.andie;

import java.awt.image.*;
import javax.swing.JOptionPane;

/**
 * Applies brightness and contrast adjustments to an image.
 * <p>
 * This operation modifies the RGB colour channel of every pixel
 * while preserving the alpha (transparency) channel
 * Brightness and contrast values are provided as percentages
 * Brightness increases or decreases overall lightness
 * Contrast increases or decreases the difference between light and dark areas
 * </p>
 * @author Maleena Taia
 * Version 1.0
 */
public class BrightnessAndContrastAdjustment implements ImageOperation, java.io.Serializable {

    /** 
     * Percentage value used to adjust image brightness
     */
    private double brightness;
    
    /** 
     * Percentage value used to adjust image contrast
     */
    private double contrast;

    /** 
     * Creates a new brightness and contrast adjustment operation.
     * @param brightness - The brightness percentage adjustment,
     *                     positive values brighten image
     *                     negative values darken it.
     * @param contrast - The contrast percentage adjustment.
     *                   Positive values increase contrast, 
     *                   negative values decrease it.
     */
    public BrightnessAndContrastAdjustment(double brightness, double contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    /**
     * Applies the brightness and contrast adjustment to the input image.
     *
     * @param input The image to modify.
     * @return A new BufferedImage containing the adjusted image.
     *         Returns the original image if the input is null.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
         
        // Check that an image has been loaded before processing.
        if (input == null) {
            JOptionPane.showMessageDialog(
                    null,
                    I18nManager.get("brightness_no_image"),
                    I18nManager.get("error_title"),
                    JOptionPane.ERROR_MESSAGE
            );
            return input;
        }

        // Store image dimensions.
        int width = input.getWidth();
        int height = input.getHeight();

        // Create a new image to store the adjusted result.
        BufferedImage output = new BufferedImage(width, height, input.getType());

        // Loop through every pixel in the image.
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                
                // Get the pixel colour as a single ARGB integer.
                int argb = input.getRGB(x, y);

                // Extract alpha, red, green, and blue values using bit shifting.
                int a = (argb & 0xFF000000) >>> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);

                // Apply brightness and contrast adjustments to each colour channel.
                int newR = adjustChannel(r);
                int newG = adjustChannel(g);
                int newB = adjustChannel(b);

                // Recombine the adjusted channels back into a single ARGB value.
                int newArgb = (a << 24) | (newR << 16) | (newG << 8) | newB;
                output.setRGB(x, y, newArgb);
            }
        }

        // Returns modified image.
        return output;
    }

    /**
     * Adjusts a single colour channel using brightness and contrast values.
     *
     * @param value The original channel value (0-255).
     * @return The adjusted and clamped channel value.
     */
    private int adjustChannel(int value) {
        
        // Apply contrast around midpoint 127.5 and brightness scaling.
        double newValue = (1 + contrast / 100.0) * (value - 127.5)
                + 127.5 * (1 + brightness / 100.0);

        // Round the calculated value to the nearest integer.
        int result = (int) Math.round(newValue);

        // Clamps the result to the valid RGB range 0 - 255
        // Used to make sure values cant go out of bounds.
        if (result < 0) {
            result = 0;
        }
        if (result > 255) {
            result = 255;
        }

        return result;
    }
}

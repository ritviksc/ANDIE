package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * <p>
 * ImageOperation to swap the colour channels of an image (R, G, B).
 * </p>
 *
 * <p>
 * This operation allows the user to specify a new ordering of the red, green,
 * and blue channels. For example, an input of "GBR" will map Red → Green, Green
 * → Blue, and Blue → Red. The alpha (transparency) channel of each pixel is
 * preserved.
 * </p>
 *
 * <p>
 * Includes exception handling and multilingual support via I18nManager. The
 * user prompt can be used via the no-argument constructor, or a predefined
 * order can be passed to the other constructor.
 * </p>
 *
 * @author Maleena Taia
 * @version 2.0
 */
public class ColourChannelSwapping implements ImageOperation, java.io.Serializable {

    private final String order; // the channel order, e.g., "GBR"

    /**
     * No-argument constructor: prompts user for channel order.
     * Uses default "RGB" if user cancels.
     */
    public ColourChannelSwapping() {
        // Prompt user for channel order
        String input = JOptionPane.showInputDialog(I18nManager.get("channel_prompt"));
        if (input == null) {
            // User cancelled → use default order
            this.order = "RGB";
        } else {
            this.order = input.toUpperCase().trim();
        }
    }

    /**
     * Constructor that accepts a predefined channel order.
     *
     * @param order The new channel order (e.g., "GBR")
     */
    public ColourChannelSwapping(String order) {
        // If null, use default safe order
        if (order == null) {
            this.order = "RGB";
        } else {
            this.order = order.toUpperCase().trim();
        }
    }

    /**
     * Apply the colour channel swap operation to an input image.
     *
     * @param input The image to apply the channel swap to.
     * @return A new BufferedImage with channels swapped according to the
     *         specified order, or the original input if null or invalid.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {

        // Exception handling: null check
        if (input == null) {
            JOptionPane.showMessageDialog(
                null,
                I18nManager.get("channel_no_image"),
                I18nManager.get("error_title"),
                JOptionPane.ERROR_MESSAGE
            );
            return input;
        }

        // Validate order
        if (order.length() != 3 || !order.matches("[RGB]{3}") ||
            !(order.contains("R") && order.contains("G") && order.contains("B"))) {

            JOptionPane.showMessageDialog(
                null,
                I18nManager.get("channel_invalid"),
                I18nManager.get("error_title"),
                JOptionPane.WARNING_MESSAGE
            );
            return input;
        }

        // Original RGB indices for mapping
        char[] original = {'R', 'G', 'B'};
        int[] map = new int[3]; // map[i] tells which original channel goes to position i

        // Create output image copy
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        // Build the mapping array based on the provided order
        for (int i = 0; i < 3; i++) {
            char target = order.charAt(i);
            for (int j = 0; j < 3; j++) {
                if (original[j] == target) {
                    map[i] = j;
                    break;
                }
            }
        }

        // Debug: check mapping output
        System.out.println(Arrays.toString(map));

        // Get image dimensions
        int width = input.getWidth();
        int height = input.getHeight();

        // Loop through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int pixel = input.getRGB(x, y); // get ARGB value of pixel

                // Extract alpha, red, green, and blue components
                int a = (pixel & 0xFF000000) >>> 24; // alpha channel
                int r = (pixel & 0x00FF0000) >> 16; // red channel
                int g = (pixel & 0x0000FF00) >> 8;  // green channel
                int b = (pixel & 0x000000FF);       // blue channel

                int[] rgb = {r, g, b}; // store original RGB

                // Apply channel mapping based on user input
                int newR = rgb[map[0]];
                int newG = rgb[map[1]];
                int newB = rgb[map[2]];

                // Reconstruct the pixel with the new RGB values and original alpha
                int newPixel = (a << 24) | (newR << 16) | (newG << 8) | newB;

                // Update the pixel in the image
                output.setRGB(x, y, newPixel);
            }
        }

        return output;
    }
}

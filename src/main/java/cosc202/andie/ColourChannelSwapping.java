package cosc202.andie;

import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 * <p>
 * ImageOperation to swap the colour channels of an image (R, G, B).
 * </p>
 * 
 * <p>
 * This operation allows the user to specify a new ordering of the
 * red, green, and blue channels. For example, an input of "GBR"
 * will map Red → Green, Green → Blue, and Blue → Red.
 * The alpha (transparency) channel of each pixel is preserved.
 * </p>
 * 
 * @author Maleena Taia
 * @version 1.0
 */
public class ColourChannelSwapping implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Apply the colour channel swap operation to an input image.
     * </p>
     * 
     * <p>
     * Prompts the user for a new channel order (permutation of R, G, B),
     * calculates the mapping of each channel, and applies it to every pixel.
     * </p>
     * 
     * @param input The image to apply the channel swap to.
     * @return A new BufferedImage with channels swapped according to user input.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {

        // Prompt the user to enter a new channel order
        String order = JOptionPane.showInputDialog(
                "Enter colour channel order (permutation of R, G, B, e.g., GBR):");

        // Validate input: must be 3 characters and only contain R, G, B
        if (order == null || order.length() != 3 || !order.matches("[RGB]{3}")) {
            JOptionPane.showMessageDialog(null, "Invalid input. Must be a permutation of R, G, B.");
            return input; // return original image if input is invalid
        }

        // Original RGB indices for mapping
        char[] original = {'R', 'G', 'B'};
        int[] map = new int[3]; // map[i] tells which original channel goes to position i

        // Build the mapping array based on user input
        for (int i = 0; i < 3; i++) {
            char target = order.charAt(i);
            for (int j = 0; j < 3; j++) {
                if (original[j] == target) {
                    map[i] = j;
                    break;
                }
            }
        }

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
                input.setRGB(x, y, newPixel);
            }
        }

        // Return the modified image
        return input;
    }
}

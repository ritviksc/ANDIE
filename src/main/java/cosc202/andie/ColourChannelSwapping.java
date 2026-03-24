package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class ColourChannelSwapping implements ImageOperation, java.io.Serializable {

    private final String order;

    public ColourChannelSwapping() {
        // Prompt user for channel order
        String input = JOptionPane.showInputDialog(I18nManager.get("channel_prompt"));
        if (input == null) {
            this.order = "RGB"; // user cancelled → default
        } else {
            input = input.toUpperCase().trim();
            // Validate input
            if (input.length() != 3 || !input.matches("[RGB]{3}")
                    || !(input.contains("R") && input.contains("G") && input.contains("B"))) {
                JOptionPane.showMessageDialog(
                        null,
                        I18nManager.get("channel_invalid"),
                        I18nManager.get("error_title"),
                        JOptionPane.ERROR_MESSAGE
                );
                this.order = "RGB"; // fallback
            } else {
                this.order = input;
            }
        }
    }

    // Constructor with predefined order
    public ColourChannelSwapping(String order) {
        if (order == null) {
            this.order = "RGB"; // null → default
        } else {
            String temp = order.toUpperCase().trim();
            // Validate input silently
            if (temp.length() != 3 || !temp.matches("[RGB]{3}")
                    || !(temp.contains("R") && temp.contains("G") && temp.contains("B"))) {
                this.order = "RGB"; // fallback silently
            } else {
                this.order = temp;
            }
        }
    }

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
            return null; // exit if no image
        }

        // Safe order fallback
        String safeOrder = order;
        if (safeOrder == null || safeOrder.length() != 3
                || !safeOrder.matches("[RGB]{3}")
                || !(safeOrder.contains("R") && safeOrder.contains("G") && safeOrder.contains("B"))) {
            safeOrder = "RGB"; // fallback silently
        }

        // Build channel mapping
        char[] original = {'R', 'G', 'B'};
        int[] map = new int[3]; // map[i] tells which original channel goes to position i
        for (int i = 0; i < 3; i++) {
            char target = safeOrder.charAt(i);
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

        // Create output image of the same size and type
        BufferedImage output = new BufferedImage(width, height, input.getType());

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

        return output; // return new swapped image
    }
}

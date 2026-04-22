package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to swap the colour channels of an image (R, G, B).
 * Alpha (transparency) is preserved.
 * </p>
 *
 * <p>
 * Applies one of six predefined RGB channel permutations based on a
 * selected index.
 * </p>
 *
 * @author Leena Taia
 * @version 1.0
 */
public class ColourChannelSwapping implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * The selected swap option index: 0 = no change, 1–5 = specific RGB channel
     * permutations.
     * </p>
     */
    private final int selected;

    /**
     * <p>
     * Construct a ColourChannelSwapping operation with the given swap option.
     * </p>
     *
     * @param selected the swap option index (0–5). If an invalid index is
     * provided, no change will be applied (index 0).
     */
    public ColourChannelSwapping(int selected) {
        if (selected < 0 || selected > 5) {
            this.selected = 0; // no change
        } else {
            this.selected = selected;
        }
    }

    /**
     * <p>
     * Apply the colour channel swap to an input image.
     * </p>
     *
     * <p>
     * Iterates through every pixel in the input image, swaps the red, green,
     * and blue channels according to the selected permutation, and returns a
     * new BufferedImage with the modified pixels. Alpha is preserved. If the
     * input image is null, an error dialog is shown and null is returned.
     * </p>
     *
     * @param input The BufferedImage to apply the channel swap to.
     * @return A new BufferedImage with swapped colour channels, or null if the
     * input image is null.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {

        // Check if input is null
        if (input == null) {
            return null;
        }

        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int argb = input.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                int newR = r, newG = g, newB = b;

                // Map selected index (0–5) to RGB channel permutations
                switch (selected) {
                    case 0 -> {
                        /* no change */ }
                    case 1 -> {
                        newG = b;
                        newB = g;
                    } // RBG
                    case 2 -> {
                        newR = g;
                        newG = r;
                    } // GRB
                    case 3 -> {
                        newR = b;
                        newG = r;
                        newB = g;
                    } // BRG
                    case 4 -> {
                        newR = g;
                        newG = b;
                        newB = r;
                    } // GBR
                    case 5 -> {
                        newR = b;
                        newB = r;
                    } // BGR
                }

                int newArgb = (a << 24) | (newR << 16) | (newG << 8) | newB;
                output.setRGB(x, y, newArgb);
            }
        }

        return output;
    }
}

package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Applies a contrast mask effect to an image.
 * <p>
 * The effect is created by:
 * <ol>
 * <li>Creating a copy of the original image</li>
 * <li>Converting the copy to greyscale</li>
 * <li>Inverting the image colours</li>
 * <li>Applying Gaussian blur</li>
 * <li>Blending the mask with the original image using soft light blending</li>
 * </ol>
 * This increases local contrast and can create a sharpening-style effect.
 * </p>
 *
 * @author Maleena Taia
 * @version 1.0
 */
public class ContrastMask implements ImageOperation, java.io.Serializable {

    /**
     * Radius used for the Gaussian blur effect.
     */
    protected int radius;

    /**
     * Strength of the contrast mask effect from 0 to 100.
     */
    protected int strength;

    /**
     * Creates a new contrast mask operation.
     *
     * @param radius Radius used for Gaussian blur.
     * @param strength Strength of the effect from 0 to 100.
     */
    public ContrastMask(int radius, int strength) {
        this.radius = radius;
        this.strength = strength;
    }

    /**
     * Applies the contrast mask effect to an image.
     *
     * @param input The image to process.
     * @return A new image with the contrast mask effect applied.
     */
    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            return null;
        }

        // Deep copy the original image
        BufferedImage mask = deepCopy(input);

        // Convert copy to greyscale
        ConvertToGrey gs = new ConvertToGrey();
        mask = gs.apply(mask);

        // Invert greyscale image
        ImageInversion invert = new ImageInversion();
        mask = invert.apply(mask);

        // Blur the negative image
        GaussianFilter blur = new GaussianFilter(radius);
        mask = blur.apply(mask);

        BufferedImage output = new BufferedImage(
                input.getWidth(),
                input.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        // Convert strength percentage into alpha weight
        int maskAlpha = (int) (255 * (strength / 100.0));

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int originalPixel = input.getRGB(x, y);
                int maskPixel = mask.getRGB(x, y);

                // Extract alpha channel from original pixel
                int a = (originalPixel >> 24) & 0xFF;

                int rOrig = (originalPixel >> 16) & 0xFF;
                int gOrig = (originalPixel >> 8) & 0xFF;
                int bOrig = originalPixel & 0xFF;

                int rMask = (maskPixel >> 16) & 0xFF;
                int gMask = (maskPixel >> 8) & 0xFF;
                int bMask = maskPixel & 0xFF;
                
                double weight = maskAlpha / 255.0;

                // Blend original and mask channels using soft light blending.
                int r = blendSoftLight(rOrig, rMask, weight);
                int g = blendSoftLight(gOrig, gMask, weight);
                int b = blendSoftLight(bOrig, bMask, weight);

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;
                output.setRGB(x, y, newPixel);
            }
        }

        // Returns modified image.
        return output;
    }

    /**
     * Creates a deep copy of an image.
     *
     * @param image The image to copy.
     * @return A deep copy of the image.
     */
    private BufferedImage deepCopy(BufferedImage image) {
        
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Blends one colour channel using a soft light blend and applies the mask
     * strength as a weight.
     *
     * @param original The original channel value.
     * @param mask The mask channel value.
     * @param weight The blend strength from 0.0 to 1.0.
     * @return The blended channel value.
     */
    private int blendSoftLight(int original, int mask, double weight) {
        
        // Convert channel values into ratios between 0 and 1
        double base = original / 255.0;
        double blend = mask / 255.0;

        // Soft light blend formula
        double blended = (1.0 - 2.0 * blend) * base * base + 2.0 * blend * base;
        
        // Apply weighted blend
        double result = blended * weight + base * (1.0 - weight);

        // Convert back into RGB range
        return clamp((int) Math.round(result * 255.0));
    }

    /**
     * Clamps a colour value so it stays within the valid range 0 to 255.
     *
     * @param value The colour value to clamp.
     * @return The clamped colour value.
     */
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}

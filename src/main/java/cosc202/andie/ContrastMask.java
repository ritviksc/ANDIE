package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author malee
 */
public class ContrastMask implements ImageOperation, java.io.Serializable {

    // Blur radius for Gaussian blur
    protected int radius;

    //Strength of effect from 0 - 100
    protected int strength;

    // Create contrastMask operation
    public ContrastMask(int radius, int strength) {
        this.radius = radius;
        this.strength = strength;
    }

    // Apply contrast mask effect to an image
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
        double maskAlpha = strength / 100.0;

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int originalPixel = input.getRGB(x, y);
                int maskPixel = mask.getRGB(x, y);

                int a = (originalPixel >> 24) & 0xFF;

                int rOrig = (originalPixel >> 16) & 0xFF;
                int gOrig = (originalPixel >> 8) & 0xFF;
                int bOrig = originalPixel & 0xFF;

                int rMask = (maskPixel >> 16) & 0xFF;
                int gMask = (maskPixel >> 8) & 0xFF;
                int bMask = maskPixel & 0xFF;

                int r = blendSoftLight(rOrig, rMask, maskAlpha);
                int g = blendSoftLight(gOrig, gMask, maskAlpha);
                int b = blendSoftLight(bOrig, bMask, maskAlpha);

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;
                output.setRGB(x, y, newPixel);
            }
        }

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
        double base = original / 255.0;
        double blend = mask / 255.0;

        double blended = (1.0 - 2.0 * blend) * base * base + 2.0 * blend * base;
        double result = blended * weight + base * (1.0 - weight);

        return clamp((int) Math.round(result * 255.0));
    }
    
    /**
     * Clamps a colour value so it stays within the valid range 0 to 255.
     * 
     */
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}

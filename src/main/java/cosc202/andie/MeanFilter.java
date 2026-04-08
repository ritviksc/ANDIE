package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Mean (simple blur) filter.
 * </p>
 *
 * <p>
 * A Mean filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and can be implemented by a
 * convolution.
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @see java.awt.image.ConvolveOp
 * @author Steven Mills
 * @version 1.0
 */
public class MeanFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2
     * a 5x5 filter, and so forth.
     */
    private int radius;
    private static final int ALPHA = 0;
    private static final int RED = 1;
    private static final int GREEN = 2;
    private static final int BLUE = 3; // For clarity

    /**
     * <p>
     * Construct a Mean filter with the given size.
     * </p>
     *
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used. A
     * size of 1 is a 3x3 filter, 2 is 5x5, and so on. Larger filters give a
     * stronger blurring effect.
     * </p>
     *
     * @param radius The radius of the newly constructed MeanFilter
     */
    MeanFilter(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Mean filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Mean filter has radius 1.
     * </p>
     *
     * @see MeanFilter(int)
     */
    @SuppressWarnings("unused")
    MeanFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Mean filter to an image.
     * </p>
     *
     * <p>
     * As with many filters, the Mean filter is implemented via convolution. The
     * size of the convolution kernel is specified by the {@link radius}. Larger
     * radii lead to stronger blurring.
     * </p>
     *
     * @param input The image to apply the Mean filter to.
     * @return The resulting (blurred)) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        int size = (2 * radius + 1) * (2 * radius + 1);
        float[] kernel = new float[size];
        int kWdith = (2 * radius + 1);
        int height = input.getHeight();
        int width = input.getWidth();

        Arrays.fill(kernel, 1.0f / size);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        
        int kernelSum = 0;
        
        for(float value: kernel){
            kernelSum += value;
        }
        
        if(kernelSum == 0) kernelSum = 1; // Prevent divide by zero errors
        
        int colourChannels = 4;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int[] sums = new int[colourChannels]; // get running sum

                for (int ky = -radius; ky <= radius; ky++) { // iterate over kernel area

                    for (int kx = -radius; kx <= radius; kx++) {
                        int yInitial = y + ky;
                        int xInitial = x + kx;

                        int yCorrected = Math.max(0, Math.min(yInitial, height - 1));
                        int xCorrected = Math.max(0, Math.min(xInitial, width - 1));

                        int argb = input.getRGB(xCorrected, yCorrected);

                        int a = (argb >>> 24) & 0xFF;
                        int r = (argb >>> 16) & 0xFF;
                        int g = (argb >>> 8) & 0xFF;
                        int b = argb & 0xFF;

                        float filterWeight = kernel[(ky + radius) * kWdith + (kx + radius)]; // get the value at the specified kernel co-ordinate
                        
                        sums[ALPHA] += (a * filterWeight);
                        sums[RED] += (r * filterWeight);
                        sums[GREEN] += (g * filterWeight);
                        sums[BLUE] += (b * filterWeight);
                        
                    }
                }
                
                int newA = Math.min(255, Math.max(0, sums[ALPHA]));
                int newR = Math.min(255, Math.max(0, sums[RED]));
                int newG = Math.min(255, Math.max(0, sums[GREEN]));
                int newB = Math.min(255, Math.max(0, sums[BLUE]));
                
                int argb = (newA << 24) | (newR << 16) | (newG << 8) | newB;
                
                output.setRGB(x, y, argb);
                
            }
        }
        return output;
    }

}

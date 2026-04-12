package cosc202.andie;

import static cosc202.andie.ConvolutionFilter.Mode.EMBOSS;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Sobel (simple blur) filter.
 * </p>
 *
 * <p>
 * An Sobel filter
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @see ConvolutionFilter
 * @author Steven Mills
 * @version 1.0
 */
public class SobelFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2
     * a 5x5 filter, and so forth.
     */
    private int direction;
    private static final int ALPHA = 0;
    private static final int RED = 1;
    private static final int GREEN = 2;
    private static final int BLUE = 3; // For clarity

    /**
     * <p>
     * Construct a Sobel filter with the given size.
     * </p>
     *
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used. A
     * size of 1 is a 3x3 filter, 2 is 5x5, and so on. Larger filters give a
     * stronger blurring effect.
     * </p>
     *
     * @param radius The radius of the newly constructed SobelFilter
     */
    SobelFilter(int direction) {
        this.direction = direction;
    }

    /**
     * <p>
     * Construct a Sobel filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Sobel filter has direction 1.
     * </p>
     *
     * @see SobelFilter(int)
     */
    @SuppressWarnings("unused")
    SobelFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Sobel filter to an image.
     * </p>
     *
     * <p>
     * As with many filters, the Sobel filter is implemented via convolution.
     *
     * </p>
     *
     * @param input The image to apply the Sobel filter to.
     * @return The resulting (embossed) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {

        float[] kernel = new float[9];
        ConvolutionFilter convOp = new ConvolutionFilter();

        BufferedImage output = input;
        BufferedImage horizontal = input;
        BufferedImage vertical = input;

        if (direction == 0 || direction == 2) {
            kernel = new float[]{-0.5f, 0.0f, 0.5f,
                -1.0f, 0.0f, 1.0f,
                -0.5f, 0.0f, 0.5f
            };

            horizontal = convOp.applyConvolution(input, kernel, EMBOSS);
            if (direction == 0) {
                output = horizontal;
            }
        }

        if (direction == 1 || direction == 2) {

            kernel = new float[]{-0.5f, -1.0f, -0.5f,
                0.0f, 0.0f, 0.0f,
                0.5f, 1.0f, 0.5f
            };

            vertical = convOp.applyConvolution(input, kernel, EMBOSS);
            if (direction == 1) {
                output = vertical;
            }
        }

        if (direction == 2) {
            for (int y = 0; y < input.getHeight() - 1; y++) {
                for (int x = 0; x < input.getWidth() - 1; x++) {

                    int argbHor = horizontal.getRGB(x, y);

                    int argbVer = vertical.getRGB(x, y);

                    int aHor = (argbHor >>> 24) & 0xFF;
                    int rHor = (argbHor >>> 16) & 0xFF;
                    int gHor = (argbHor >>> 8) & 0xFF;
                    int bHor = argbHor & 0xFF;

                    int aVer = (argbVer >>> 24) & 0xFF;
                    int rVer = (argbVer >>> 16) & 0xFF;
                    int gVer = (argbVer >>> 8) & 0xFF;
                    int bVer = argbVer & 0xFF;

                    int aNew = aHor;
                    int rNew = (int) Math.sqrt(Math.pow(rHor - 128, 2) + Math.pow(rVer - 128, 2));
                    int gNew = (int) Math.sqrt(Math.pow(gHor - 128, 2) + Math.pow(gVer - 128, 2));
                    int bNew = (int) Math.sqrt(Math.pow(bHor - 128, 2) + Math.pow(bVer - 128, 2));

                    rNew = Math.min(255, Math.max(0, rNew));
                    gNew = Math.min(255, Math.max(0, gNew));
                    bNew = Math.min(255, Math.max(0, bNew));

                    int argb = (aNew << 24) | (rNew << 16) | (gNew << 8) | bNew;

                    output.setRGB(x, y, argb);

                }

            }
        }
        return output;
    }

}

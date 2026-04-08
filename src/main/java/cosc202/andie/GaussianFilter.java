package cosc202.andie;

import static cosc202.andie.ConvolutionFilter.Mode.BLUR;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Gaussian blur filter.
 * </p>
 *
 * <p>
 * A Gaussian filter blurs an image by using a Gaussian bell curve and can be
 * implemented by a convolution.
 * </p>
 *
 * @author Robert Hannaford
 * @version 1.0
 */
public class GaussianFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2
     * a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Gaussian filter with the given size.
     * </p>
     *
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used. A
     * size of 1 is a 3x3 filter, 2 is 5x5, and so on. Larger filters give a
     * stronger blurring effect.
     * </p>
     *
     * @param radius The radius of the newly constructed GaussianFilter
     */
    GaussianFilter(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Gaussian filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Gaussian filter has radius 1 (3x3).
     * </p>
     *
     * @see GaussianFilter(int)
     */
    @SuppressWarnings("unused")
    GaussianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Gaussian filter to an image.
     * </p>
     *
     * <p>
     * As with many filters, the Gaussian filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.
     * Larger
     *
     * radii lead to stronger blurring with smaller values being much weaker.
     * </p>
     *
     * @param input The image to apply the Gaussian filter to.
     * @return The resulting (blurred)) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {

        int size = (2 * radius + 1) * (2 * radius + 1);

        float[] array = new float[size];
        float sigma = (float) (1 / 3.0 * radius); // Used in calculating Gaussian
        for (int y = -radius; y <= radius; y++) {

            for (int x = -radius; x <= radius; x++) {

                array[(y + radius) * (2 * radius + 1) + (x + radius)] = getGaussianValue(x, y, sigma); // Set the value at each co-ordinate

            }

        }

        ConvolutionFilter convOp = new ConvolutionFilter();
        BufferedImage output = convOp.applyConvolution(input, array, BLUR);

        return output;
    }

    /**
     * <p>
     * A function that returns the Gaussian value at a given point
     * </p>
     *
     * @param x the x co-ordinate
     * @param y the y co-ordinate
     * @param sigma the sigma value
     * @return The value of the Gaussian function at the point
     */
    private static float getGaussianValue(int x, int y, float sigma) {

        float firstHalf = 1f / (2 * (float) Math.PI * (sigma * sigma));
        float exponent;
        try {
            exponent = -((x * x + y * y) / (2f * sigma * sigma));
        } catch (ArithmeticException e) {
            exponent = 0;
        }

        float secondHalf = (float) Math.exp(exponent);

        float value = firstHalf * secondHalf;

        return value;
    }

}

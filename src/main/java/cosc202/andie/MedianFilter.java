package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median (simple blur) filter.
 * </p>
 *
 * <p>
 * todo: how it works
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @see java.awt.image.ConvolveOp
 * @author Robert Hannaford
 * @version 1.0
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of the neighbourhood to apply. A radius of 1 is a 3x3 neighbourhood, a radius of 2
     * a 5x5 neighbourhood, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Median filter with the given size.
     * </p>
     *
     * <p>
     * todo: explanation
     * </p>
     *
     * @param radius The radius of the newly constructed MedianFilter
     */
    MedianFilter(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Median filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Median filter has radius 1.
     * </p>
     *
     * @see MedianFilter(int)
     */
    @SuppressWarnings("unused")
    MedianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Median filter to an image.
     * </p>
     *
     * <p>
     * todo: explanation
     * </p>
     *
     * @param input The image to apply the Median filter to.
     * @return The resulting (blurred)) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        int size = (2 * radius + 1) * (2 * radius + 1);
        float[] array = new float[size];
        Arrays.fill(array, 1.0f / size);

        Kernel kernel = new Kernel(2 * radius + 1, 2 * radius + 1, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        
        
        
        
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(input, output);

        return output;
    }

}

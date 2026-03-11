/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;

/**
 *
 * @author Robert Hannaford
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2
     * a 5x5 filter, and so forth.
     */
    private int radius;

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
    @SuppressWarnings("unused")
    SharpenFilter() {
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
        
        float[] array = {0, -1/2.0f, 0
                        -1/2.0f, 3.0f, -1/2.0f,
                        0, -1/2.0f, 0
        };

        Kernel kernel = new Kernel(3, 3, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(input, output);

        return output;
    }

}

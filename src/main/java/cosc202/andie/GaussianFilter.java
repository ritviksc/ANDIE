package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Gaussian blur filter.
 * </p>
 *
 * <p>
 * A Gaussian filter blurs an image by using a Gaussian bell curve
 * and can be implemented by a convolution.
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
     * @param radius The radius of the newly constructed MeanFilter
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
     * By default, a Gaussian filter has radius 1.
     * </p>
     *
     * @see MeanFilter(int)
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

        float[] array = new float[size];
        float rho = (float) (1/3.0*radius); // Used in calculating Gaussian
        float sum = 0f; // Running total for normalisation
        int pos = 0; // For tracking where we are in the array
        for(int y = -radius; y <= radius; y++){
            
            for(int x = -radius; x <= radius; x++){
                
                System.out.println(x + " " + y);
                array[pos] = getGaussianValue(x, y, rho);
                sum += array[pos];
                pos++;
                
            }
            
        }
        
        pos = 0; // Go back to the start of the list
        
        for(int y = -radius; y <= radius; y++){
            
            for(int x = -radius; x <= radius; x++){

                array[pos] /= sum;  // Normalise each value
                System.out.print(array[pos] + "   ");
                pos++;
                
            }
            
            System.out.println();
            
        }

        Kernel kernel = new Kernel(2 * radius + 1, 2 * radius + 1, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(input, output);

        return output;
    }
    /**
     * 
     * @param x
     * @param y
     * @param rho 
     * @return The value of the Gaussian function at the point
     */
    private static float getGaussianValue(int x, int y, float rho){
        
        float firstHalf = 1f/(2*(float) Math.PI*(rho*rho));
        float exponent = -((x * x + y * y) / (2f * rho * rho));
        float secondHalf = (float) Math.exp(exponent);
        
        float value = firstHalf*secondHalf;
        
        return value;
    }

}

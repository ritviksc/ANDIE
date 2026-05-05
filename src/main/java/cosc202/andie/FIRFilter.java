package cosc202.andie;

import static cosc202.andie.ConvolutionFilter.Mode.EMBOSS;
import java.awt.image.BufferedImage;

/**
 * <p>
 * An edge detection filter that works 'vertically'
 * applied via convolution
 * </p>
 * @author rober
 */
public class FIRFilter implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Construct a FIR filter.
     * </p>
     *
     * <p>
     * A filter that enhances the difference between pixels.
     * </p>
     *
     */
    @SuppressWarnings("unused")
    FIRFilter() {
    }
    /**
     * <p>
     * Apply a FIR filter to an image.
     * </p>
     *
     * <p>
     * As with many filters, the FIR filter is implemented via convolution.
     * The kernel used is actually a simplified laplacian approximation for a 3x3 kernel.
     * </p>
     *
     * @param input The image to apply the FIR filter to.
     * @return The resulting (sharpened) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        
        
        // Note this is a simplified laplacian kernel for a 3x3 kernel
        float[] array = {0, -1.0f, 0,
                        -1.0f, 4.0f, -1.0f,
                        0, -1.0f, 0
        };
        ConvolutionFilter convOp = new ConvolutionFilter();
        
        BufferedImage output = convOp.applyConvolution(input, array, EMBOSS);

        return output;
    }
    
}
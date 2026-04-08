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
 * <p>
 * ImageOperation to apply a Sharpen (simple clearing) filter.
 * </p>
 *
 * <p>
 * A sharpen filter accentuates differences between pixels to 'sharpen'
 * an image to produce a clearer picture than the original.
 * This is implemented via a convolution operation.
 * </p>
 *
 * @author Robert Hannaford
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Construct a Sharpen filter.
     * </p>
     *
     * <p>
     * A filter that enhances the difference between pixels.
     * </p>
     *
     */
    @SuppressWarnings("unused")
    SharpenFilter() {
    }
    /**
     * <p>
     * Apply a Sharpen filter to an image.
     * </p>
     *
     * <p>
     * As with many filters, the Sharpen filter is implemented via convolution.
     * </p>
     *
     * @param input The image to apply the Sharpen filter to.
     * @return The resulting (sharpened) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        
        float[] array = {0, -1/2.0f, 0,
                        -1/2.0f, 3.0f, -1/2.0f,
                        0, -1/2.0f, 0
        };
        ConvolutionFilter convOp = new ConvolutionFilter();
        BufferedImage output = convOp.applyConvolution(input, array, true);

        return output;
    }

}

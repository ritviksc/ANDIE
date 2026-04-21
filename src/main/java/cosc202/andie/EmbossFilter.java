package cosc202.andie;

import static cosc202.andie.ConvolutionFilter.Mode.EMBOSS;
import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a Emboss (simple blur) filter.
 * </p>
 *
 * <p>
 * An Emboss filter 
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @see ConvolutionFilter
 * @author Robert Hannaford
 * @version 1.0
 */
public class EmbossFilter implements ImageOperation, java.io.Serializable {

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
     * Construct a Emboss filter with the given size.
     * </p>
     *
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used. A
     * size of 1 is a 3x3 filter, 2 is 5x5, and so on. Larger filters give a
     * stronger blurring effect.
     * </p>
     *
     * @param radius The radius of the newly constructed EmbossFilter
     */
    EmbossFilter(int direction) {
        this.direction = direction;
    }

    /**
     * <p>
     * Construct an Emboss filter with the default size.
     * </p
     * >
     * <p>
     * By default, an Emboss filter has direction 1.
     * </p>
     *
     * @see EmbossFilter(int)
     */
    @SuppressWarnings("unused")
    EmbossFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply an Emboss filter to an image.
     * </p>
     *
     * <p>
     * As with many filters, the Emboss filter is implemented via convolution.
     * 
     * </p>
     *
     * @param input The image to apply the Emboss filter to.
     * @return The resulting (embossed) image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        
        float[] kernel = new float[9];
        
        switch(direction){
            
            case 0: // Right
                    kernel = new float[] {0.0f, 0.0f, 0.0f,
                                        -1.0f, 0.0f, 1.0f,
                                        0.0f, 0.0f, 0.0f
                    };
                break;
                
            case 1: // Down right
                kernel = new float[] {-1.0f, 0.0f, 0.0f,
                                      0.0f, 0.0f, 0.0f,
                                      0.0f, 0.0f, 1.0f
                };
                break;
                
            case 2: // Down
                kernel = new float[] {0.0f, -1.0f, 0.0f,
                                      0.0f, 0.0f, 0.0f,
                                      0.0f, 1.0f, 0.0f
                };
                break;
            case 3: // Down left
                kernel = new float[] {0.0f, 0.0f, -1.0f,
                                      0.0f, 0.0f, 0.0f,
                                      1.0f, 0.0f, 0.0f
                };
                break;
                
            case 4: // Left
                kernel = new float[] {0.0f, 0.0f, 0.0f,
                                      1.0f, 0.0f, -1.0f,
                                      0.0f, 0.0f, 0.0f
                };
                break;
                
            case 5: // Up left
                kernel = new float[] {1.0f, 0.0f, 0.0f,
                                      0.0f, 0.0f, 0.0f,
                                      0.0f, 0.0f, -1.0f
                };
            case 6: // Up
                kernel = new float[] {0.0f, 1.0f, 0.0f,
                                      0.0f, 0.0f, 0.0f,
                                      0.0f, -1.0f, 0.0f
                };
            case 7: // Up right
                kernel = new float[] {0.0f, 0.0f, 1.0f,
                                      0.0f, 0.0f, 0.0f,
                                      -1.0f, 0.0f, 0.0f
                };
        }
        
        ConvolutionFilter convOp = new ConvolutionFilter();
        BufferedImage output = convOp.applyConvolution(input, kernel, EMBOSS);
        return output;
    }

}

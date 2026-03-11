package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median (complex blur) filter.
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
        int size = (2 * radius + 1);
        
        int horizontalClusters = input.getWidth()/size;
        int verticalClusters = input.getHeight()/size;
        int[][] colours = new int[4][size*size]; // 1 alpha channel and 3 colour channels and size*size number of pixels
                                                 // 0 => alpha
                                                 // 1 => red
                                                 // 2 => green
                                                 // 3 => blue
        int ALPHA = 0;
        int RED = 1;
        int GREEN = 2;
        int BLUE = 3;
        
//        int argb = input.getRGB(1, 1);
//        /* >> sets shifted-in bits to match the sign (high order) bit
//        * >>> sets shifted-in bits to zero always
//         */
//        int a = (argb & 0xFF000000) >>> 24;
//        int r = (argb & 0x00FF0000) >> 16;
//        int g = (argb & 0x0000FF00) >> 8;
//        int b = (argb & 0x000000FF);
        int argb, a, r, g, b;

        for (int cy = 0; cy < verticalClusters; cy++) {
            for (int cx = 0; cx < horizontalClusters; cx++) {
                
                int pos = 0; // The position in the current array
                
                for(int y = 0; y < size; y++){
                    for(int x = 0; x < size; x++){
                        argb = input.getRGB(x, y);

                        a = (argb & 0xFF000000) >>> 24;
                        r = (argb & 0x00FF0000) >> 16;
                        g = (argb & 0x0000FF00) >> 8;
                        b = (argb & 0x000000FF);
                        
                        colours[ALPHA][pos] = a;
                        colours[RED][pos] = r;
                        colours[GREEN][pos] = g;
                        colours[BLUE][pos] = b;
                        
                        
                    }
                } // Gets the rgb values of the cluster and stores them in a 2d array
                
                int aAve;
                int rAve;
                int gAve;
                int bAve;
                
                for(int colour = 0; colour < 4; colour++){
                    
                    int currentAverage = 0;
                    
                    for(int i = 0; i < size*size; i++){
                    
                        currentAverage += colours[colour][i];
                        
                    }
                    
                    currentAverage /= (size*size);
                    
                    switch(colour){ // Assign each colour channel its new average
                        
                        case 0:
                            
                            aAve = currentAverage;
                        
                        case 1:
                            
                            rAve = currentAverage;
                        
                        case 2:
                            
                            gAve = currentAverage;
                        
                        case 3:
                            
                            bAve = currentAverage;
                        
                    }
                    
                }

            }
        }
        
        
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        return output;
    }

}

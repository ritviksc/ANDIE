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
     * The size of the cluster to apply. A radius of 1 is a 3x3 cluster, a radius of 2
     * a 5x5 cluster, and so forth.
     */
    private int radius;
    private static final int ALPHA = 0;
    private static final int RED = 1;
    private static final int GREEN = 2;
    private static final int BLUE = 3;

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
     * @return The resulting (blurred) image.
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

        for (int cy = 0; cy < verticalClusters; cy++) {
            for (int cx = 0; cx < horizontalClusters; cx++) {
                
                int pos = 0; // The position in the current array
                                
                for(int y = 0; y < size; y++){
                    for(int x = 0; x < size; x++){
                        int argb = input.getRGB(x, y);

                        int a = (argb & 0xFF000000) >>> 24;
                        int r = (argb & 0x00FF0000) >> 16;
                        int g = (argb & 0x0000FF00) >> 8;
                        int b = (argb & 0x000000FF);
                        
                        
                        colours[ALPHA][pos] = a;
                        colours[RED][pos] = r;
                        colours[GREEN][pos] = g;
                        colours[BLUE][pos] = b;
                        pos++;
                        
                    }
                } // Gets the rgb values of the cluster and stores them in a 2d array
                
                
                for (int[] colour : colours) { // Sort each array
                    Arrays.sort(colour);
                }
                
                int mean = (size*size/2) + 1;
                
                int aAve = colours[ALPHA][mean];
                int rAve = colours[RED][mean];
                int gAve = colours[GREEN][mean];
                int bAve = colours[BLUE][mean]; // Get the mean for each colour
                
                for(int y = 0; y < size; y++){
                    for(int x = 0; x < size; x++){
                        
                        int argb = (aAve << 24) | (rAve << 16) | (gAve << 8) | bAve; // Creates new argb value by assigning the new bit medians
                        
                        input.setRGB(x, y, argb);
                        
                    }
                } 
                
            }
        }
        
        return input;
    }

}

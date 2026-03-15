package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median (complex blur) filter.
 * </p>
 *
 * <p>
 * A median filter works by taking the average of each colour channel
 * in each pixel surrounding the current working pixel. It then sets the current
 * pixel to be the median of each colour channel.
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
    private static final int BLUE = 3; // For clarity

    /**
     * <p>
     * Construct a Median filter with the given size.
     * </p>
     *
     * <p>
     * By default, give it a radius of 1.
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
     * This filter is implemented as a O(n^4) so be wary when using large n.
     * This method is used to apply the filter to a given image.
     * </p>
     *
     * @param input The image to apply the Median filter to (recommended less than 10).
     * @return The resulting (blurred) image.
     */
    @Override
    
    public BufferedImage apply(BufferedImage input) {
        int size = ((2 * radius) + 1);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        int horizontalClusters = input.getWidth();
        int verticalClusters = input.getHeight();
        
        int[][] colours = new int[4][size*size]; // 1 alpha channel and 3 colour channels and size*size number of pixels
                                                 // 0 => alpha
                                                 // 1 => red
                                                 // 2 => green
                                                 // 3 => blue
        int median = (size*size) / 2;
        int lastValidPixel = input.getRGB(0, 0);
        for (int cy = 0; cy < verticalClusters; cy++) {
            for (int cx = 0; cx < horizontalClusters; cx++) {
                
                int pos = 0; // The position in the current array
                for(int y = -radius; y <= radius; y++){
                    for(int x = -radius; x <= radius; x++){
                        
                        try{
                            int xCorrected;
                            int yCorrected;
                            
                            if(cx + x > 0 && cx + x < horizontalClusters) xCorrected = cx + x;
                            else if(cx + x < 0) xCorrected = 0;
                            else xCorrected = horizontalClusters - 1;
                            
                            if(cy + y > 0 && cy + y < verticalClusters) yCorrected = cy + y;
                            else if(cy + y < 0) yCorrected = 0;
                            else yCorrected = verticalClusters - 1;
                            
                            int argb = input.getRGB(xCorrected, yCorrected);
                            lastValidPixel = argb;
                            int a = (argb >>> 24) & 0xFF;
                            int r = (argb >>> 16) & 0xFF;
                            int g = (argb >>> 8) & 0xFF;
                            int b = argb & 0xFF;

                            colours[ALPHA][pos] = a;
                            colours[RED][pos] = r;
                            colours[GREEN][pos] = g;
                            colours[BLUE][pos] = b;
                            pos++;

                        } catch(ArrayIndexOutOfBoundsException e){
                        
                            int argb = lastValidPixel;
                            int a = (argb >>> 24) & 0xFF;
                            int r = (argb >>> 16) & 0xFF;
                            int g = (argb >>> 8) & 0xFF;
                            int b = argb & 0xFF;

                            colours[ALPHA][pos] = a;
                            colours[RED][pos] = r;
                            colours[GREEN][pos] = g;
                            colours[BLUE][pos] = b;
                            pos++;
                        
                        }
                        
                        
                    }
                } // Gets the rgb values of the cluster and stores them in a 2d array
                
                
                for (int[] colour : colours) { // Sort each array
                    Arrays.sort(colour);
                    // System.out.println(Arrays.toString(colour));
                }
                
                
                
                
                
                int aAve = colours[ALPHA][median];
                int rAve = colours[RED][median];
                int gAve = colours[GREEN][median];
                int bAve = colours[BLUE][median]; // Get the median for each colour
                int argb = (aAve << 24) | (rAve << 16) | (gAve << 8) | bAve; // Creates new argb value by assigning the new bits
                // System.out.println(Integer.toBinaryString(argb));
                
                output.setRGB(cx, cy, argb); // Manipulate the OUTPUT image to ensure it can be undone.
                
            }
        }
        
        return output;
    }

}

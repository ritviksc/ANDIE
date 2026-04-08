package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author rober
 */
public class ConvolutionFilter {

    private static int[] kernel;
    
    private static final int ALPHA = 0;
    private static final int RED = 1;
    private static final int GREEN = 2;
    private static final int BLUE = 3; // For clarity
    
    public ConvolutionFilter(){}
    
    public BufferedImage applyConvolution(BufferedImage input, float[] kernel, boolean normalise){
        
        int radius = (int) Math.sqrt(kernel.length)/2;
        
        int size = (2 * radius + 1) * (2 * radius + 1);
        int kWdith = (2 * radius + 1);
        int height = input.getHeight();
        int width = input.getWidth();

        Arrays.fill(kernel, 1.0f / size);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        int kernelSum = 0;

        for (float value : kernel) {
            kernelSum += value;
        }

        if (kernelSum != 0 && normalise == true) { // normalise
            for(int i = 0; i < kernel.length; i++){
                kernel[i] /= kernel[i]/kernelSum;
            }
        }
        int colourChannels = 4;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                float[] sums = new float[colourChannels]; // get running sum
                float sumWeight = 0;
                int newA = (input.getRGB(x, y) >>> 24) & 0xff;

                for (int ky = -radius; ky <= radius; ky++) { // iterate over kernel area

                    for (int kx = -radius; kx <= radius; kx++) {
                        int yInitial = y + ky;
                        int xInitial = x + kx;

                        int yCorrected = Math.max(0, Math.min(yInitial, height - 1));
                        int xCorrected = Math.max(0, Math.min(xInitial, width - 1));

                        int argb = input.getRGB(xCorrected, yCorrected);

                        int a = (argb >>> 24) & 0xFF;
                        int r = (argb >>> 16) & 0xFF;
                        int g = (argb >>> 8) & 0xFF;
                        int b = argb & 0xFF;

                        float filterWeight = kernel[(ky + radius) * kWdith + (kx + radius)]; // get the value at the specified kernel co-ordinate

                        sums[ALPHA] += a * filterWeight;
                        sums[RED] += (r * a * filterWeight);
                        sums[GREEN] += (g * a * filterWeight);
                        sums[BLUE] += (b * a * filterWeight);
                        sumWeight += a * filterWeight;
                    }
                }

                int outA = sumWeight == 0 ? 0 : (int) Math.min(255, Math.max(0, Math.round(sums[ALPHA])));
                int newR = sumWeight == 0 ? 0 : (int) Math.min(255, (int) Math.max(0, Math.round(sums[RED]) / sumWeight));
                int newG = sumWeight == 0 ? 0 : (int) Math.min(255, (int) Math.max(0, Math.round(sums[GREEN]) / sumWeight));
                int newB = sumWeight == 0 ? 0 : (int) Math.min(255, (int) Math.max(0, Math.round(sums[BLUE]) / sumWeight));

                int argb = (outA << 24) | (newR << 16) | (newG << 8) | newB;

                output.setRGB(x, y, argb);

            }
        }
        return output;
    }
    
    
}

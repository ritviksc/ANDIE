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

    public ConvolutionFilter() {
    }

    public enum Mode {
        BLUR,
        EMBOSS,
        EDGE
    }

    public BufferedImage applyConvolution(BufferedImage input, float[] kernel, Mode mode) {

        boolean normalise = false;
        boolean useAlphaWeighting = false;
        boolean applyBias = false;

        switch (mode) {
            case BLUR:
                normalise = true;
                useAlphaWeighting = true;
                break;

            case EMBOSS:
                applyBias = true;
                break;

            case EDGE: // edge cases
                break;
        }

        int radius = (int) Math.sqrt(kernel.length) / 2;

        int size = (2 * radius + 1) * (2 * radius + 1);
        int kWdith = (2 * radius + 1);
        int height = input.getHeight();
        int width = input.getWidth();

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        float kernelSum = 0;
        for (float value : kernel) {
            kernelSum += value;
        }
        if (kernelSum != 0 && normalise == true) { // normalise
            for (int i = 0; i < kernel.length; i++) {
                kernel[i] /= kernelSum;
            }
        }

        int colourChannels = 4;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                float[] sums = new float[colourChannels]; // get running sum
                float sumWeight = 0;

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

                        if (useAlphaWeighting) {

                            sums[RED] += (r * a * filterWeight);
                            sums[GREEN] += (g * a * filterWeight);
                            sums[BLUE] += (b * a * filterWeight);
                            sumWeight += a * filterWeight;

                        } else {

                            

                            sums[RED] += (r * filterWeight);
                            sums[GREEN] += (g * filterWeight);
                            sums[BLUE] += (b * filterWeight);

                        }
                    }
                }

                int newR, newG, newB;
                if (useAlphaWeighting) {
                    newR = sumWeight == 0 ? 0 : (int) (sums[RED] / sumWeight);
                    newG = sumWeight == 0 ? 0 : (int) (sums[GREEN] / sumWeight);
                    newB = sumWeight == 0 ? 0 : (int) (sums[BLUE] / sumWeight);
                } else {
                    newR = (int) sums[RED];
                    newG = (int) sums[GREEN];
                    newB = (int) sums[BLUE];
                }

                if (applyBias) {
                    newR += 128;
                    newG += 128;
                    newB += 128;
                }

                int newA = (input.getRGB(x, y) >>> 24) & 0xff;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                int argb = (newA << 24) | (newR << 16) | (newG << 8) | newB;

                output.setRGB(x, y, argb);

            }
        }
        return output;
    }

}

package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * An image operation that adjusts the saturation of an image
 * 
 * Saturation controls the intensity of the colours:
 * values below 100 reduce colour intensity, 
 * 100 keeps the original image unchanged
 * and values above 100 increase colour intensity
 * 
 * @author Maleena Taia
 * @version 1.0
 */
public class SaturationFilter implements ImageOperation, java.io.Serializable {
    /** 
     * Saturation amount as a percentage
     */
    private final int saturation;
    
    /**
     * Create new saturation filter
     * @param saturation Saturation percentage to apply
     */
    public SaturationFilter(int saturation){
        this.saturation = saturation;
    }
    
    /**
     * Applies the saturation adjustment to an image.
     * 
     * Each pixel is converted toward or away from its grey value
     * depending on the saturation factor.
     * 
     * @param input The input image to process.
     * @return A new image with adjusted saturation.
     */
    @Override
    public BufferedImage apply(BufferedImage input){
        
        int width = input.getWidth();
        int height = input.getHeight();
        
        BufferedImage output = new BufferedImage(width, height, input.TYPE_INT_ARGB);
        
        // Convert percentage into a multiplier value
        float saturationFactor = saturation / 100.0f;
        
        // Process every pixel in the image.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int argb = input.getRGB(x, y);

                // Extract alpha, red, green, and blue components.
                int alpha = (argb >> 24) & 0xFF;
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;

                // Calculate the grey value of the pixel.
                int grey = (red + green + blue) / 3;

                // Adjust each colour channel based on saturation
                int newRed = clamp((int) (grey + saturationFactor * (red - grey)));
                int newGreen = clamp((int) (grey + saturationFactor * (green - grey)));
                int newBlue = clamp((int) (grey + saturationFactor * (blue - grey)));

                // Combine colour channels back into one ARGB value.
                int newARGB = (alpha << 24)
                        | (newRed << 16)
                        | (newGreen << 8)
                        | newBlue;

                output.setRGB(x, y, newARGB);
        }
    }

    return output;
}
    
    /**
     * Restricts a value to the valid colour range of 0–255.
     * 
     * @param value The value to clamp.
     * @return The clamped value.
     */
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
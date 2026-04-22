package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 *
 * @author malee
 */
public class SaturationFilter implements ImageOperation, java.io.Serializable {
    // Saturation amount as a percentage
    private final int saturation;
    
    //Create new saturation filter
    public SaturationFilter(int saturation){
        this.saturation = saturation;
    }
    
    // Apply saturation to an image
    @Override
    public BufferedImage apply(BufferedImage input){
        
        int width = input.getWidth();
        int height = input.getHeight();
        
        BufferedImage output = new BufferedImage(width, height, input.TYPE_INT_ARGB);
        
        float saturationFactor = saturation / 100.0f;
        
         for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {

            int argb = input.getRGB(x, y);

            int alpha = (argb >> 24) & 0xFF;
            int red = (argb >> 16) & 0xFF;
            int green = (argb >> 8) & 0xFF;
            int blue = argb & 0xFF;

            int grey = (red + green + blue) / 3;

            int newRed = clamp((int) (grey + saturationFactor * (red - grey)));
            int newGreen = clamp((int) (grey + saturationFactor * (green - grey)));
            int newBlue = clamp((int) (grey + saturationFactor * (blue - grey)));

            int newARGB = (alpha << 24)
                    | (newRed << 16)
                    | (newGreen << 8)
                    | newBlue;

            output.setRGB(x, y, newARGB);
        }
    }

    return output;
}
    
    //Keep colour values between 0 and 255.
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
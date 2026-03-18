package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to resize a image by a scale factor.
 * </p>
 *
 * <p>
 *  The image will be scaled up or down depending on the factor provided (a percentage). 
 *  If it is less than 100% the image will be scaled down, else the image will be scaled up accordingly.
 * </p>
 * 
 * @author Ritvik Sharma
 * @version 1.0
 */
public class ImageResize implements ImageOperation, java.io.Serializable {

    static final double CONVERT_TO_FLOAT = (float) 100.0;
    private double scaleFactor;

    public ImageResize(double factor){
        this.scaleFactor = factor/CONVERT_TO_FLOAT;
    }

    // default constructor
    public ImageResize(){
        this.scaleFactor = 1;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        int imageWidth = input.getWidth(); // Get width of image
        int imageHeight = input.getHeight(); // Get height of image

        // Get new dimensions w.r.t to scaling factor, making sure if factor is to low we take width and height to be 1 avoiding any crashes.
        int newWidth = Math.max(1,(int) ( imageWidth * this.scaleFactor));
        int newHeight = Math.max(1,(int) ( imageHeight * this.scaleFactor));

        // Create copy of input and and draw new image
        Image resultingImage = input.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, input.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose(); // avoid memory leaks if ANDIE runs for a longer amount of time
        return outputImage;

    }
    
}

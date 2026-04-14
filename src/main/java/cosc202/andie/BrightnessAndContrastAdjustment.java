package cosc202.andie;

import java.awt.image.*;
import javax.swing.JOptionPane;

/**
 *
 * @author malee
 */
public class BrightnessAndContrastAdjustment implements ImageOperation, java.io.Serializable {

    private double brightness;
    private double contrast;

    public BrightnessAndContrastAdjustment(double brightness, double contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    @Override
    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            JOptionPane.showMessageDialog(
                    null,
                    I18nManager.get("brightness_no_image"),
                    I18nManager.get("error_title"),
                    JOptionPane.ERROR_MESSAGE
            );
            return input;
        }

        int width = input.getWidth();
        int height = input.getHeight();

        BufferedImage output = new BufferedImage(width, height, input.getType());

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int argb = input.getRGB(x, y);

                int a = (argb & 0xFF000000) >>> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);

                int newR = adjustChannel(r);
                int newG = adjustChannel(g);
                int newB = adjustChannel(b);

                int newArgb = (a << 24) | (newR << 16) | (newG << 8) | newB;
                output.setRGB(x, y, newArgb);
            }
        }

        return output;
    }

    private int adjustChannel(int value) {
        double newValue = (1 + contrast / 100.0) * (value - 127.5)
                + 127.5 * (1 + brightness / 100.0);

        int result = (int) Math.round(newValue);

        if (result < 0) {
            result = 0;
        }
        if (result > 255) {
            result = 255;
        }

        return result;
    }
}

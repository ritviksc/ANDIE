package cosc202.andie;
import java.awt.Color;

/**
 * Helper class to collect user options for drawing options, and these are used to apply a Image Operation
 * @author shari838
 */
class ShapeOptions {
    Color color; // color user wants
    boolean filled; // filled or solid object

    ShapeOptions(Color color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }
}
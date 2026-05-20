package cosc202.andie;

import java.awt.*;

/**
 * A mascot character displayed in the application interface.
 *
 * The mascot can display temporary speech bubble messages
 * and draw itself onto a panel.
 *
 * @author Maleena Taia
 * @version 1.0
 */
public class Mascot {

    /**
     * Current speech bubble message.
     */
    private String message = null;

    /**
     * Time when the current message expires
     */
    private long messageExpiry = 0;

    /**
     * Image used for the mascot sprite
     */
    private Image sprite;

    /**
     * Creates a mascot using the given sprite image
     *
     * @param sprite Image used to draw the mascot
     */
    public Mascot(Image sprite) {
        this.sprite = sprite;
    }

    /**
     * Displays a temporary message above the mascot.
     *
     * @param msg Message to display
     * @param durationMs Duration in milliseconds
     */
    public void showMessage(String msg, int durationMs) {
        this.message = msg;
        this.messageExpiry = Math.max(messageExpiry, System.currentTimeMillis() + durationMs);
    }

    /**
     * Updates the mascot state and removes expired messages.
     */
    public void update() {
        if (message != null && System.currentTimeMillis() > messageExpiry) {
            message = null;
        }
    }

    /**
     * Draws the mascot and its speech bubble
     *
     * @param g Graphics context used for drawing.
     * @param panelWidth  Width of the panel.
     * @param panelHeight Height of the panel.
     */
    public void draw(Graphics2D g, int panelWidth, int panelHeight) {
        int x = panelWidth - 80;
        int y = panelHeight - 80;

        g.drawImage(sprite, x, y, 80, 80, null);

        if (message != null) {
            FontMetrics fm = g.getFontMetrics();

            int padding = 8;
            int textWidth = fm.stringWidth(message);
            int textHeight = fm.getHeight();

            int bubbleWidth = textWidth + padding * 2;
            int bubbleHeight = textHeight + padding * 2;

            // Default position (above mascot)
            int bubbleX = x - bubbleWidth - 10;
            int bubbleY = y - bubbleHeight;

            // Keeps bubble within the edges of the panel.
            // Clamp LEFT edge
            if (bubbleX < 0) {
                bubbleX = 10;
            }

            // Clamp TOP edge
            if (bubbleY < 0) {
                bubbleY = y + 10;
            }
            // Clamp RIGHT edge
            if (bubbleX + bubbleWidth > panelWidth) {
                bubbleX = panelWidth - bubbleWidth - 10;
            }

            // Clamp BOTTOM edge
            if (bubbleY + bubbleHeight > panelHeight) {
                bubbleY = panelHeight - bubbleHeight - 10;
            }

            drawBubble(g, message, bubbleX, bubbleY);
        }
    }

    /**
     * Draws a speech bubble containing text.
     *
     * @param g Graphics context used for drawing.
     * @param text Text displayed in the bubble.
     * @param x X-coordinate of the bubble.
     * @param y Y-coordinate of the bubble.
     */
    private void drawBubble(Graphics2D g, String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();

        int padding = 8;
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int width = textWidth + padding * 2;
        int height = textHeight + padding * 2;

        g.setColor(new Color(255, 255, 255, 220));
        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 10, 10);

        g.drawString(text, x + padding, y + padding + fm.getAscent());
    }
}

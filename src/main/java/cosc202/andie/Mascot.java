package cosc202.andie;

import java.awt.*;

/**
 *
 * @author malee
 */
public class Mascot {

    private String message = null;
    private long messageExpiry = 0;

    private Image sprite;

    public Mascot(Image sprite) {
        this.sprite = sprite;
    }

    public void showMessage(String msg, int durationMs) {
        this.message = msg;
        this.messageExpiry = Math.max(messageExpiry, System.currentTimeMillis() + durationMs);
    }

    public void update() {
        if (message != null && System.currentTimeMillis() > messageExpiry) {
            message = null;
        }
    }

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

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
        if (message != null) {
            return;
        }
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

        g.drawImage(sprite, x, y, 64, 64, null);

        if (message != null) {
            drawBubble(g, message, x - 60, y - 20);
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

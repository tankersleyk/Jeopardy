package jeopardy;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public final class Utils {

    /**
     * Draw a string that is centered in some given rectangle
     * @param g the graphics to draw the string on
     * @param text the text value to draw
     * @param rect the rectangle to center the string in
     * @param color the color to draw the text in
     */
    public static void drawCenteredString(Graphics2D g, String text, Rectangle2D rect, Color color) {
        Color oldColor = g.getColor();
        g.setColor(color);
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = (int) (rect.getX() + (rect.getWidth() - metrics.stringWidth(text)) / 2);
        int y = (int) (rect.getY() + (rect.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
        g.setColor(oldColor);
    }
}

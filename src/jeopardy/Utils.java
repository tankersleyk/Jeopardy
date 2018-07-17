package jeopardy;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class Utils {

    public final static Color BLUE = Color.getHSBColor(232f/360f, .97f, .45f);
    public final static Color ORANGE = Color.getHSBColor(.1f, .64f, .83f);
    public final static Color PURPLE = Color.getHSBColor(252/360f, .87f, .7f);

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

    /**
     * Resizes the image to the given width and height
     * @param image the image to resize
     * @param width the width of the desired resized image
     * @param height the height of the desired resized image
     */
    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image tmpImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Convert Image to BufferedImage by drawing the image onto bufferedimage's graphics object

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(tmpImage, 0, 0, null);
        graphics.dispose();

        return newImage;
    }
}

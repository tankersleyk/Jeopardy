package jeopardy;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

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
     * @param fontSize the size of the font to use for the text
     */
    public static void drawCenteredString(Graphics2D g, String text, Rectangle2D rect, Color color, int fontSize) {
        Color oldColor = g.getColor();
        g.setColor(color);

        Font oldFont = g.getFont();
        Font font = new Font("Serif", Font.PLAIN, fontSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        List<String> toWrite = Arrays.asList(splitString(metrics, text, rect.getWidth()).split("\n"));
        int n = toWrite.size();

        for (int i = 0; i < n; i++) {
            String s = toWrite.get(i);
            int x = (int) (rect.getX() + (rect.getWidth() - metrics.stringWidth(s)) / 2);
            int y = (int) (rect.getY() + (rect.getHeight() - metrics.getHeight()) * (i+1)/(n+1)) + metrics.getAscent();

            g.drawString(s, x, y);
        }

        g.setColor(oldColor);
        g.setFont(oldFont);
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

    /**
     * Split a string such that each section (split by '\n') can be drawn in the width supplied
     * @param metrics the FontMetrics that describe the font being used to display the text
     * @param text the text to write for this object
     * @param w the width (in pixels) of the area in which to draw the string
     * @return the string with new lines inserted so that each section can be drawn
     */
    private static String splitString(FontMetrics metrics, String text, double w) {
        final double MAX_LINE_PERC = 1.0; // Don't use more than this amount of the width
        StringBuilder s = new StringBuilder();
        StringBuilder line = new StringBuilder();

        List<String> words = Arrays.asList(text.split(" "));

        int i = 0;
        while (i < words.size()) {
            while (i < words.size() && metrics.stringWidth(line.toString() + words.get(i)) <= MAX_LINE_PERC*w) {
                line.append(words.get(i));
                line.append(" ");
                i+=1;
            }
            if (i < words.size() && metrics.stringWidth(words.get(i)) > MAX_LINE_PERC* w) {
                System.out.println(words.get(i));
                s.append(words.get(i));
                s.append('\n');
                line = new StringBuilder();
                i+=1;
            }
            else {
                s.append(line);
                s.append('\n');
                line = new StringBuilder();
            }
        }

        s.deleteCharAt(s.length()-1); // remove last newline char

        return s.toString();
    }
}

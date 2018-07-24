package jeopardy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jeopardy.States.MainMenuState;

/**
 * The main Jeopardy file for game entry!
 *
 */
public class Jeopardy {

    private static Map<String, Boolean> settings;
    public static final BufferedImage BACKGROUND;

    static {
        try {
            BACKGROUND = ImageIO.read(new File("data/background.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in background image");
        }
    }

    public static int WIN_WIDTH = 1280;
    public static int WIN_HEIGHT = 720;

    /**
     * Handle the game meta information, including player's settings and server connection
     */
    public static void main(String[] args) {

        // Set up the window
        final JFrame window = new JFrame("Jeopardy");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the graphics
        final JPanel drawingArea = new JPanel();
        drawingArea.setLayout(null);
        drawingArea.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);

        StateStack.changePanel(drawingArea);
        StateStack.push(MainMenuState.getInstance());
    }
}

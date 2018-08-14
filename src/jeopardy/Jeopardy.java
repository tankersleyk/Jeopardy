package jeopardy;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jeopardy.States.MainMenuState;

/**
 * The main Jeopardy file for game entry!
 *
 */
public class Jeopardy {

    //    private static Map<String, Boolean> settings;
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

    private static JPanel drawingArea;
    private static JFrame window;

    /**
     * Handle the game meta information, including player's settings and server connection
     */
    @SuppressWarnings("serial")
    public static void main(String[] args) {

        // Set up the window
        window = new JFrame("Jeopardy") {
            @Override
            public void repaint() {
                super.repaint();
                StateStack.repaint();
            }
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                StateStack.render();
            }
        };
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the graphics
        drawingArea = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Utils.drawBackground((Graphics2D) g);
            }
            @Override
            public Point getToolTipLocation(MouseEvent e) {
                Point p = e.getPoint();
                p.y+=15;
                return p;
            }
        };
        drawingArea.setLayout(null);
        drawingArea.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));

        window.add(drawingArea);
        window.pack();
        window.setVisible(true);

        StateStack.changePanel(drawingArea);
        StateStack.push(MainMenuState.getInstance());
    }

    public static void changeResolution(int width, int height) {
        WIN_WIDTH = width;
        WIN_HEIGHT = height;
        drawingArea.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        window.pack();
    }
}

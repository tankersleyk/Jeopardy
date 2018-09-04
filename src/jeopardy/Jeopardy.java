package jeopardy;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.scenario.Settings;

import jeopardy.States.MainMenuState;

/**
 * The main Jeopardy file for game entry!
 *
 */
public class Jeopardy {

    public static final Properties SETTINGS;
    public static final BufferedImage BACKGROUND;
    //    private static final File SETTINGS_FILE;

    static {
        try {
            BACKGROUND = ImageIO.read(new File("data/background.png"));
            SETTINGS = new Properties();

            try {
                InputStream input = new FileInputStream("data/config.properties");
                SETTINGS.load(input);
            }
            catch (IOException e) {
                SETTINGS.setProperty("noGuess", "true");
                SETTINGS.setProperty("Resolution", "1280x720");
                OutputStream output;
                try {
                    output = new FileOutputStream("data/config.properties");
                    SETTINGS.store(output, null);
                    output.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
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

        String[] splitResolution = SETTINGS.getProperty("Resolution").split("x");
        int width = Integer.parseInt(splitResolution[0]);
        int height = Integer.parseInt(splitResolution[1]);

        WIN_WIDTH = width;
        WIN_HEIGHT = height;

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
        changeSettings("Resolution", width +"x" + height);
    }

    public static void changeSettings(String key, String value) {
        SETTINGS.setProperty(key, value);
        OutputStream output;
        try {
            output = new FileOutputStream("data/config.properties");
            SETTINGS.store(output, null);
            output.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

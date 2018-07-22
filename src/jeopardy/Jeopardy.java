package jeopardy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jeopardy.States.MainMenuState;

/**
 * The main Jeopardy file for game entry!
 *
 */
public class Jeopardy {

    private static Map<String, Boolean> settings;

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
        //        window.setLayout(new BorderLayout());
        final JPanel drawingArea = new JPanel();
        drawingArea.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);
        final Graphics2D graphics = (Graphics2D) drawingArea.getGraphics();

        StateStack.changeGraphics(graphics);
        StateStack.push(MainMenuState.getInstance());

        // Set up the mouse listener to handle a player's clicks
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                StateStack.handleClick(me);
            }
        });

        // Set up the mouse motion listener to handle hover effects
        drawingArea.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent me) {
                super.mouseMoved(me);
                StateStack.handleMouse(me.getPoint());
            }
        });

        long lastTime = System.currentTimeMillis();
        // Game Loop
        while (true) {
            float dt = (System.currentTimeMillis() - lastTime) * 1000; // Probably won't need it, but will keep for now
            Point absoluteLocation = MouseInfo.getPointerInfo().getLocation();
            int x = absoluteLocation.x;
            int y = absoluteLocation.y;
            Point location = new Point(x - (int) drawingArea.getLocationOnScreen().getX(), y - (int) drawingArea.getLocationOnScreen().getY());
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
    }
}

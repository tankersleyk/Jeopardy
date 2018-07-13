package jeopardy;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * Handle the game meta information, including player's settings and server connection
     */
    public static void main(String[] args) {

        // Set up the window
        final JFrame window = new JFrame("Jeopardy");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the graphics
        final JPanel drawingArea = new JPanel();
        drawingArea.setPreferredSize(new Dimension(800, 800));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);
        final Graphics2D graphics = (Graphics2D) drawingArea.getGraphics();

        StateStack.push(MainMenuState.getInstance());

        // Set up the mouse listener to handle a player's clicks
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                StateStack.handleClick(me);
            }
        });

        long lastTime = System.currentTimeMillis();
        // Game Loop
        while (true) {
            float dt = (System.currentTimeMillis() - lastTime) * 1000;
            StateStack.render(graphics);
            lastTime = System.currentTimeMillis();
        }
    }
}

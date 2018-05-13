package jeopardy;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
        final Graphics2D g = (Graphics2D) drawingArea.getGraphics();
        
        final Game game = new Game(g);
        
        // Set up the mouse listener to handle a player's clicks
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                game.handleClick(me);
            }
        });
    }
}

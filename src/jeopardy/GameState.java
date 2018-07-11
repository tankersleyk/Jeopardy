package jeopardy;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * One of the game states of the Jeopardy game
 *
 */
public interface GameState {

    /**
     * Draw the appropriate graphics based on this game state
     * 
     * @param g The graphics to draw this game state onto
     */
    public void drawGraphics(Graphics2D g);

    /**
     * Handle a player's click
     * @param me the information about the mouse click
     */
    public void handleClick(MouseEvent me);
}

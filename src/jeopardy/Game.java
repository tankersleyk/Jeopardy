package jeopardy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Game {
    public static enum State {
        MAIN_MENU, FIRST_ROUND;
    }
    
    // Base for drawing new images
    private final static BufferedImage base = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
    
    private State state;
    private final GameState mainMenu;
    private final GameState firstRound;
    private final Map<State, GameState> stateMap;
    
    private final Graphics2D g;
    
    public Game(Graphics2D g) {
        // Initialize game states
        this.mainMenu = new MainMenu(this);
        this.firstRound = new GameRound(Round.JEOPARDY);
        
        // Create mapping from state to GameState objects
        this.stateMap = new HashMap<>();
        this.stateMap.put(State.MAIN_MENU, mainMenu);
        this.stateMap.put(State.FIRST_ROUND, firstRound);
        
        this.g = g;

        // Initial state should be the main menu
        changeState(State.MAIN_MENU);
        
    }
    
    /**
     * Change the game state
     * @param newState the new state of the game (e.g. the main menu)
     */
    protected void changeState(State newState) {
        this.state = newState;
        
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawImage(base, 0, 0, null);
        g.fillRect(0, 0, 800, 800);
        g.setColor(oldColor);
        getActiveState().drawGraphics(g);
    }
    
    /**
     * Process a click event
     * @param me the mouse event corresponding to the click
     */
    protected void handleClick(MouseEvent me) {
        stateMap.get(state).handleClick(me);
    }
    
    /**
     * Get the GameState object for the current state
     * @return the GameState that is currently active
     */
    public GameState getActiveState() {
        return stateMap.get(state); // GameStates are immutable
    }
}

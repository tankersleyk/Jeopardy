package jeopardy;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import jeopardy.States.BaseState;
import jeopardy.States.State;

/**
 * A FSM which manages the various states of the game and transitions between them.
 *
 */
public class StateMachine {

    private static List<State> states = new ArrayList<>();
    private static State current;

    /**
     * Resets the state machine to only have the states provided
     * @param states the states the machine can transition between
     */
    public static void instantiateStates(List<State> states) {
        StateMachine.states = new ArrayList<>();
        for (State state : states) {
            StateMachine.states.add(state);
        }

        current = new BaseState();
    }

    /**
     * Exits out of the current state and enters the state specified
     * @param state the state to change to - must be in the state machine
     * @param params the parameters to pass into the enter function
     */
    public static void change(State state, StateParams params) {
        for (State s : states) {
            if (s.equals(state)) {
                current.exit();
                s.enter(params);
                current = s;
            }
        }
    }

    /**
     * Calls the update function on the current state
     * @param dt the time (in seconds) since the last frame
     */
    public static void update(float dt) {
        current.update(dt);
    }

    /**
     * Draws the relevant graphics for the topmost state onto the Graphics2D object
     * @param graphics the Graphics2D object to draw onto
     */
    public static void render(Graphics2D graphics) {
        current.render(graphics);
    }
}

package jeopardy;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Stack;

import jeopardy.States.State;

public class StateStack {

    private static Stack<State> states = new Stack<State>();

    /**
     * Push a new state onto this stack
     * @param state the state to push onto the stack
     */
    public static void push(State state) {
        states.push(state);
    }

    /**
     * Update the topmost state in the stack
     * @param dt the time (in seconds) since the last frame
     */
    public static void update(float dt) {
        states.peek().update(dt);;
    }

    /**
     * Draws the relevant graphics for all states onto the graphics2D object, drawing 
     *  from the bottom-most item in the stack to the topmost
     * @param graphics the Graphics2D object to draw onto
     */
    public static void render(Graphics2D graphics) {
        for (State state : states) {
            state.render(graphics);
        }
    }

    /**
     * Exits the current state and pops it off the stack
     */
    public static void pop() {
        states.peek().exit();
        states.pop();
    }

    /**
     * Clears the stack WITHOUT triggering the exit functions of the states
     */
    public static void clear() {
        while (!states.empty()) {
            states.pop();
        }
    }
}

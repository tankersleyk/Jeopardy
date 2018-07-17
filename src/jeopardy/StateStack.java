package jeopardy;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Stack;

import jeopardy.States.State;

public class StateStack {

    private static Stack<State> states = new Stack<State>();

    /**
     * Push a new state onto this stack
     * @param state the state to push onto the stack
     */
    public synchronized static void push(State state) {
        states.push(state);
    }

    /**
     * Push a new state onto this stack
     * @param state the state to push onto the stack
     * @param params the parameters relevant to this state
     */
    public synchronized static void push(State state, StateParams params) {
        states.push(state);
        state.enter(params);
    }

    /**
     * Update the topmost state in the stack
     * @param dt the time (in seconds) since the last frame
     */
    public synchronized static void update(float dt) {
        states.peek().update(dt);;
    }

    /**
     * Draws the relevant graphics for all states onto the graphics2D object, drawing 
     *  from the bottom-most item in the stack to the topmost
     * @param graphics the Graphics2D object to draw onto
     */
    public synchronized static void render(Graphics2D graphics) {
        for (State state : states) { // TODO: Have graphics return images to draw all at once to prevent flickering
            state.render(graphics);
        }
    }

    /**
     * Exits the current state and pops it off the stack
     */
    public synchronized static void pop() {
        states.peek().exit();
        states.pop();
    }

    /**
     * Clears the stack WITHOUT triggering the exit functions of the states
     */
    public synchronized static void clear() {
        while (!states.empty()) {
            states.pop();
        }
    }

    /**
     * Has the top state handle the click event of a mouse
     * @param me the mouse event with the details of the mouse click
     */
    public synchronized static void handleClick(MouseEvent me) {
        states.peek().handleClick(me);
    }

    /**
     * Has the top state handle function for mouse location
     * @param location the Point location of the mouse
     */
    public synchronized static void handleMouse(Point location) {
        states.peek().handleMouse(location);
    }
}

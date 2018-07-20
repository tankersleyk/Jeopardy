package jeopardy;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Stack;

import jeopardy.States.State;

public class StateStack {

    private static Stack<State> states = new Stack<State>();
    private static Graphics2D graphics;

    /**
     * Push a new state onto this stack - StateStack MUST have a graphics object
     * @param state the state to push onto the stack
     */
    public synchronized static void push(State state) {
        states.push(state);
        StateStack.render();
    }

    /**
     * Push a new state onto this stack - StateStack MUST have a graphics object
     * @param state the state to push onto the stack
     * @param params the parameters relevant to this state
     */
    public synchronized static void push(State state, StateParams params) {
        states.push(state);
        state.enter(params);
        StateStack.render();
    }

    /**
     * Update the topmost state in the stack
     * @param dt the time (in seconds) since the last frame
     */
    public synchronized static void update(float dt) {
        states.peek().update(dt);;
    }

    /**
     * Draws the relevant graphics for all states onto the graphics object 
     *  this state stack is using, drawing from the bottom-most
     *  item in the stack to the topmost - StateStack MUST have a graphics object
     */
    public synchronized static void render() {
        for (State state : states) { // TODO: Have graphics return images to draw all at once to prevent flickering
            synchronized (graphics) { // States don't acquire lock, statestack does - change?
                state.render(graphics);
            }
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
        if (states.peek().handleMouse(location)) {
            StateStack.render();
        }
    }

    /**
     * Set the graphics for this state stack to draw onto
     * @param graphics the graphics to draw onto
     */
    public static void changeGraphics(Graphics2D graphics) {
        StateStack.graphics = graphics;
    }
}

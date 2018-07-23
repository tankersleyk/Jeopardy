package jeopardy;

import java.awt.Component;
import java.util.Stack;

import javax.swing.JPanel;

import jeopardy.States.State;

public class StateStack {

    private static Stack<State> states = new Stack<State>();
    private static JPanel panel;

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
            synchronized (panel) { // States don't acquire lock, statestack does - change?
                state.render(panel);
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
     * Set the panel for this state stack to add components onto
     * @param panel the panel to use for rendering
     */
    public static void changePanel(JPanel panel) {
        StateStack.panel = panel;
    }

    /**
     * Remove a component from the JPanel associated with the StateStack
     * @param component the component to remove
     */
    public static void removeComponent(Component component) {
        panel.remove(component);
    }
}

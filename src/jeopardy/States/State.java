package jeopardy.States;

import java.awt.Graphics2D;

import javax.swing.JPanel;

import jeopardy.StateParams;

/**
 * A state to be used with the StateMachine or StateStack only
 *
 */
public interface State {

    /**
     * Called when the state is entered
     * @param params An object that holds parameters to carry over between states
     */
    public void enter(StateParams params);

    /**
     * Called every frame
     * @param dt the amount of time (in seconds) that has passed since the last frame
     */
    public void update(float dt);

    /**
     * Draws the relevant graphics for this state onto the Graphics2D object
     * @param graphics the Graphics2D object to draw onto
     * @effects draws over the graphics object
     */
    public void render(Graphics2D graphics);

    /**
     * Draws the relevant graphics for this onto the Graphics object of the panel
     *  and adds any necessary components such as JTextFields or JButtons
     * @param panel the panel to use for rendering
     * @effects draws onto the graphics object of the panel and adds components
     */
    public void render(JPanel panel);

    /**
     * Called when the state is exited
     */
    public void exit();

    /**
     * Called when the state is returned to
     */
    public void returnToState();

    /**
     * Redraw the graphics associated with this state
     * @param graphcis the graphics object to draw onto
     */
    public void repaint(Graphics2D graphics);

}

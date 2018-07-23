package jeopardy.States;

import java.awt.Graphics2D;

import javax.swing.JPanel;

import jeopardy.StateParams;

/**
 * An empty state for other states to extend so that they may have empty methods
 *
 */
public class BaseState implements State{

    @Override
    public void enter(StateParams params) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(Graphics2D graphics) {

    }

    @Override
    public void render(JPanel panel) {

    }

    @Override
    public void exit() {

    }

}

package jeopardy.States;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

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
    public void exit() {

    }

    @Override
    public void handleClick(MouseEvent me) {

    }

    @Override
    public void handleMouse(Point location) {

    }

}

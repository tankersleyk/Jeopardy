package jeopardy;

import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public abstract class MenuButton extends Rectangle2D.Double {

    public MenuButton(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    /**
     * Performs the appropriate action that should occur when this menu button is clicked
     */
    public void isClicked() {

    }
}

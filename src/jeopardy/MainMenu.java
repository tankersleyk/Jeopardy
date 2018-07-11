package jeopardy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * The main menu of the jeopardy game
 *
 */
public class MainMenu implements GameState {

    private final List<MenuButton> buttons;
    private final Game parentGame;

    /**
     * Create a main menu for the Jeopardy game
     * @param game the Game object for this Jeopardy game
     */
    public MainMenu(Game game) {
        buttons = new ArrayList<>();
        parentGame = game;
        MenuButton singleRect = new MenuButton(250, 200, 300, 100) {
            @Override
            public void isClicked() {
                parentGame.changeState(Game.State.FIRST_ROUND);
            }
        };
        MenuButton multiRect = new MenuButton(250, 500, 300, 100) {
            @Override
            public void isClicked() {
                parentGame.changeState(Game.State.FIRST_ROUND);
            }
        };
        buttons.add(singleRect);
        buttons.add(multiRect);
    }

    @Override
    public void drawGraphics(Graphics2D g) {
        g.setColor(Color.BLUE);

        Rectangle2D singleRect = new Rectangle2D.Double(250, 200, 300, 100);
        g.fill(singleRect);
        Utils.drawCenteredString(g, "Single Player", singleRect, Color.BLACK);

        Rectangle2D multiRect = new Rectangle2D.Double(250, 500, 300, 100);
        g.fill(multiRect);
        Utils.drawCenteredString(g, "Multi Player", multiRect, Color.BLACK);
    }

    @Override
    public void handleClick(MouseEvent me) {
        for (MenuButton button : buttons) {
            if (button.contains(me.getPoint())) {
                button.isClicked();
            }
        }

    }
}

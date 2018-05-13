package jeopardy;

import java.awt.Color;
import java.awt.FontMetrics;
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
    
    /**
     * Draw a string that is centered in some given rectangle
     * @param g the graphics to draw the string on
     * @param text the text value to draw
     * @param rect the rectangle to center the string in
     * @param color the color to draw the text in
     */
    private void drawCenteredString(Graphics2D g, String text, Rectangle2D rect, Color color) { // TODO: Move to utils file
        Color oldColor = g.getColor();
        g.setColor(color);
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = (int) (rect.getX() + (rect.getWidth() - metrics.stringWidth(text)) / 2);
        int y = (int) (rect.getY() + (rect.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
        g.setColor(oldColor);
    }

    @Override
    public void drawGraphics(Graphics2D g) {
        g.setColor(Color.BLUE);
        
        Rectangle2D singleRect = new Rectangle2D.Double(250, 200, 300, 100);
        g.fill(singleRect);
        drawCenteredString(g, "Single Player", singleRect, Color.BLACK);
        
        Rectangle2D multiRect = new Rectangle2D.Double(250, 500, 300, 100);
        g.fill(multiRect);
        drawCenteredString(g, "Multi Player", multiRect, Color.BLACK);
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

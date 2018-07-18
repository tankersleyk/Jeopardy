package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import jeopardy.MenuButton;
import jeopardy.StateStack;
import jeopardy.Utils;

public class MainMenuState extends BaseState{

    private static MainMenuState instance = null;
    private final List<MenuButton> buttons;
    private final Map<MenuButton, String> buttonLabels = new HashMap<>();
    private static final BufferedImage background;
    private static final BufferedImage logo;

    // TODO: probably split these try's
    static {
        try {
            background = ImageIO.read(new File("data/mmbackground.png"));
            logo = ImageIO.read(new File("data/logo.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in images");
        }
    }

    /**
     * Create a main menu for the Jeopardy game
     */
    @SuppressWarnings("serial")
    private MainMenuState() {
        buttons = new ArrayList<>();
        MenuButton singleRect = new MenuButton(250, 200, 300, 100) {
            @Override
            public void isClicked() {
                StateStack.pop(); // exit main menu state
                StateStack.push(FirstRoundState.getInstance());
            }
        };
        MenuButton multiRect = new MenuButton(250, 500, 300, 100) {
            @Override
            public void isClicked() {
                StateStack.pop(); // exit main menu state
                StateStack.push(MainMenuState.getInstance());; // TODO: REPLACE WITH MULTIPLAYER STATE
            }
        };

        buttons.add(singleRect);
        buttons.add(multiRect);

        buttonLabels.put(singleRect, "Single Player");
        buttonLabels.put(multiRect, "Multiplayer");
    }

    /**
     * get the instance of this main menu
     * @return the singleton MainMenuState object
     */
    public static MainMenuState getInstance() {
        if (instance == null) {
            instance = new MainMenuState();
        }
        return instance;
    }

    @Override
    public void render(Graphics2D graphics) {
        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(800, 800, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.drawImage(Utils.resizeImage(background, 800, 800), null, 0, 0);

        tmpGraphics.drawImage(Utils.resizeImage(logo, 400, 100), null, 200, 50);

        for (MenuButton button : buttons) {
            tmpGraphics.setColor(button.highlighted ? Utils.PURPLE : Utils.BLUE);
            Rectangle2D rect = (Rectangle2D) button;
            tmpGraphics.fill(rect);
            Utils.drawCenteredString(tmpGraphics, buttonLabels.get(button), rect, Color.WHITE, 20);
        }

        graphics.drawImage(tmpImage, null, 0, 0);
    }

    @Override
    public void handleClick(MouseEvent me) {
        for (MenuButton button : buttons) {
            if (button.contains(me.getPoint())) {
                button.isClicked();
            }
        }
    }

    @Override
    public void handleMouse(Point location) {
        for (MenuButton button : buttons) {
            if (button.contains(location)) {
                button.highlighted = true;
            }
            else
            {
                button.highlighted = false;
            }
        }
    }
}

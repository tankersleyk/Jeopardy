package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import jeopardy.MenuButton;
import jeopardy.StateStack;
import jeopardy.Utils;

public class MainMenuState extends BaseState{

    private static MainMenuState instance = null;
    private final List<MenuButton> buttons;
    private static final BufferedImage background; // TODO: draw background + implement resizeImage in utils

    static {
        try {
            background = ImageIO.read(new File("data/mmbackground.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in background image");
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

        tmpGraphics.setColor(Color.BLUE);

        Rectangle2D singleRect = new Rectangle2D.Double(250, 200, 300, 100);
        tmpGraphics.fill(singleRect);
        Utils.drawCenteredString(tmpGraphics, "Single Player", singleRect, Color.BLACK);

        Rectangle2D multiRect = new Rectangle2D.Double(250, 500, 300, 100);
        tmpGraphics.fill(multiRect);
        Utils.drawCenteredString(tmpGraphics, "Multi Player", multiRect, Color.BLACK);

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
}

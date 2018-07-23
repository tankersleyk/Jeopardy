package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import jeopardy.GameButton;
import jeopardy.Jeopardy;
import jeopardy.StateStack;
import jeopardy.Utils;

public class MainMenuState extends BaseState{

    private static MainMenuState instance = null;
    private final List<GameButton> buttons;
    private static final BufferedImage background;
    private static final BufferedImage logo;

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 100;

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
        int x = (Jeopardy.WIN_WIDTH-BUTTON_WIDTH)/2;

        int y = (Jeopardy.WIN_HEIGHT - BUTTON_HEIGHT * 2) / 3;
        GameButton singleRect = new GameButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Utils.BLUE, Color.WHITE, "Single Player", 20) {
            @Override
            public void isClicked() {
                StateStack.pop(); // exit main menu state
                StateStack.push(PlayerCreationState.getInstance());
            }
        };

        y = 2 * (Jeopardy.WIN_HEIGHT - BUTTON_HEIGHT * 2) / 3 + BUTTON_HEIGHT;
        GameButton multiRect = new GameButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT,  Utils.BLUE, Color.WHITE, "Multiplayer", 20) {
            @Override
            public void isClicked() {
                StateStack.pop(); // exit main menu state
                StateStack.push(MainMenuState.getInstance());; // TODO: REPLACE WITH MULTIPLAYER STATE
            }
        };

        buttons.add(singleRect);
        buttons.add(multiRect);

        // Set up button highlighting and clicking
        for (GameButton button : buttons) {
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(Utils.PURPLE);
                }

                public void mouseExited(MouseEvent e) {
                    button.setBackground(Utils.BLUE);
                }

                public void mouseClicked(MouseEvent e) {
                    for (GameButton button : buttons) {
                        StateStack.removeComponent(button);
                    }
                    button.isClicked();
                }
            });
        }
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
    public void render(JPanel panel) {

        Graphics2D graphics = (Graphics2D) panel.getGraphics();
        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.drawImage(Utils.resizeImage(background, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        tmpGraphics.drawImage(Utils.resizeImage(logo, Jeopardy.WIN_WIDTH / 2, Jeopardy.WIN_HEIGHT / 8), null, Jeopardy.WIN_WIDTH / 4, Jeopardy.WIN_HEIGHT / 16);

        for (GameButton button : buttons) {
            panel.add(button);
            button.repaint();
        }

        graphics.drawImage(tmpImage, null, 0, 0);
    }
}

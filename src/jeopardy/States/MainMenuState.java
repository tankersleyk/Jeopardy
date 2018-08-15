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
    private static final BufferedImage logo;

    private final int BUTTON_SPACING = 35;

    static {
        try {
            logo = ImageIO.read(new File("data/logo.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in logo");
        }
    }

    /**
     * Create a main menu for the Jeopardy game
     */
    @SuppressWarnings("serial")
    private MainMenuState() {
        final int LOGO_HEIGHT = Jeopardy.WIN_HEIGHT / 8 + Jeopardy.WIN_HEIGHT / 16;
        final int LEFTOVER_HEIGHT = Jeopardy.WIN_HEIGHT - LOGO_HEIGHT;

        final int BUTTON_WIDTH = Jeopardy.WIN_WIDTH / 4;
        final int BUTTON_HEIGHT = (LEFTOVER_HEIGHT - BUTTON_SPACING * 5) / 4;

        buttons = new ArrayList<>();
        int x = (Jeopardy.WIN_WIDTH-BUTTON_WIDTH)/2;

        int y = LOGO_HEIGHT + BUTTON_SPACING;
        GameButton singleButton = new GameButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Utils.BLUE, Color.WHITE, "Single Player", 20) {
            @Override
            public void isClicked() {
                StateStack.pop(); // exit main menu state
                StateStack.push(PlayerCreationState.getInstance());
            }
        };

        y = LOGO_HEIGHT + BUTTON_SPACING * 2 + BUTTON_HEIGHT;
        GameButton settingsButton = new GameButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Utils.BLUE, Color.WHITE, "Settings", 20) {
            @Override
            public void isClicked() {
                StateStack.push(SettingsState.getInstance());
            }
        };

        y = LOGO_HEIGHT + BUTTON_SPACING * 3 + BUTTON_HEIGHT * 2;
        GameButton multiButton = new GameButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT,  Utils.BLUE, Color.WHITE, "Multiplayer", 20) {
            @Override
            public void isClicked() {
                StateStack.pop(); // exit main menu state
                StateStack.push(MainMenuState.getInstance());; // TODO: REPLACE WITH MULTIPLAYER STATE
            }
        };

        y = LOGO_HEIGHT + BUTTON_SPACING * 4 + BUTTON_HEIGHT * 3;
        GameButton quitButton = new GameButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Utils.BLUE, Color.WHITE, "Quit", 20) {
            @Override
            public void isClicked() {
                System.exit(0);
            }
        };

        buttons.add(singleButton);
        buttons.add(settingsButton);
        buttons.add(multiButton);
        buttons.add(quitButton);

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
        final int LOGO_HEIGHT = Jeopardy.WIN_HEIGHT / 8 + Jeopardy.WIN_HEIGHT / 16;
        final int LEFTOVER_HEIGHT = Jeopardy.WIN_HEIGHT - LOGO_HEIGHT;

        final int BUTTON_WIDTH = Jeopardy.WIN_WIDTH / 4;
        final int BUTTON_HEIGHT = (LEFTOVER_HEIGHT - BUTTON_SPACING * 5) / 4;

        int x = (Jeopardy.WIN_WIDTH-BUTTON_WIDTH)/2;
        int i = 1;
        for (GameButton button : buttons) {
            int y = LOGO_HEIGHT + BUTTON_SPACING * i + BUTTON_HEIGHT * (i-1);
            button.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
            i+=1;
        }
        Graphics2D graphics = (Graphics2D) panel.getGraphics();
        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        tmpGraphics.drawImage(Utils.resizeImage(logo, Jeopardy.WIN_WIDTH / 2, Jeopardy.WIN_HEIGHT / 8), null, Jeopardy.WIN_WIDTH / 4, Jeopardy.WIN_HEIGHT / 16);

        for (GameButton button : buttons) {
            panel.add(button);
            button.repaint();
        }

        graphics.drawImage(tmpImage, null, 0, 0);
    }

    @Override
    public void exit() {
        instance = null;
    }
}

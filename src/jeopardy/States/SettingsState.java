package jeopardy.States;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.plaf.basic.BasicComboBoxUI;

import jeopardy.GameButton;
import jeopardy.Jeopardy;
import jeopardy.StateStack;
import jeopardy.Utils;

public class SettingsState extends BaseState{

    private static SettingsState instance = null;
    private Map<GameButton, List<Component>> options;
    private List<GameButton> settings;
    private GameButton activeSetting;
    private final GameButton backButton;
    private static final BufferedImage displayIcon;
    private static final BufferedImage gameplayIcon;

    private static final String[] RESOLUTIONS = {
            "1280x720", "1024x768", "1366x768", "1920x1080"
    };

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 100;
    private final int SETTINGS_PADDING = 20;

    static {
        try {
            displayIcon = ImageIO.read(new File("data/display.png"));
            gameplayIcon = ImageIO.read(new File("data/gameplay.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in logo");
        }
    }

    @SuppressWarnings({ "serial", "unchecked", "rawtypes" })
    private SettingsState() {
        options = new HashMap<>();
        settings = new ArrayList<>();

        GameButton displayButton = new GameButton(Jeopardy.WIN_WIDTH / 4, Jeopardy.WIN_HEIGHT / 4, 150, 50, Utils.BLUE, Color.WHITE, "\t Display", 20) {
            @Override
            public void isClicked() {
            }
        };

        displayButton.setIcon(new ImageIcon(displayIcon));

        JComboBox resolutionBox = new JComboBox(RESOLUTIONS);
        resolutionBox.setBounds(new Rectangle(
                displayButton.getX() + displayButton.getWidth() + SETTINGS_PADDING, displayButton.getY(), 300, 50
                ));

        resolutionBox.setUI(new BasicComboBoxUI());
        resolutionBox.setBackground(Utils.BLUE);
        resolutionBox.setForeground(Color.WHITE);
        resolutionBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        resolutionBox.setFocusable(false);

        settings.add(displayButton);
        options.put(displayButton, Arrays.asList(resolutionBox));

        GameButton gameplayButton = new GameButton(Jeopardy.WIN_WIDTH / 4, Jeopardy.WIN_HEIGHT / 4 + displayButton.getHeight() + SETTINGS_PADDING, 150, 50, Utils.BLUE, Color.WHITE, "\t Game", 20) {
            @Override
            public void isClicked() {
            }
        };

        gameplayButton.setIcon(new ImageIcon(gameplayIcon));

        List<Component> gameplaySettings = new ArrayList<>();

        ToolTipManager.sharedInstance().setInitialDelay(0);

        JCheckBox noGuess = new JCheckBox("Wrong answers lose money (singleplayer only)", Boolean.parseBoolean(Jeopardy.SETTINGS.getProperty("noGuess")));
        noGuess.setBounds(new Rectangle(Jeopardy.WIN_WIDTH / 4 + gameplayButton.getWidth() + SETTINGS_PADDING,
                Jeopardy.WIN_HEIGHT / 4, 300, 50));
        noGuess.setToolTipText("When enabled, you may forgo guessing by entering a blank answer");
        noGuess.setForeground(Color.WHITE);
        noGuess.setBackground(Utils.BLUE);
        noGuess.setFocusable(false);

        noGuess.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Jeopardy.SETTINGS.setProperty("noGuess", Boolean.toString(noGuess.isSelected()));
                System.out.println(Jeopardy.SETTINGS.getProperty("noGuess"));
            }

        });

        gameplaySettings.add(noGuess);

        settings.add(gameplayButton);
        options.put(gameplayButton,gameplaySettings);

        for (GameButton button : settings) {
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(Utils.PURPLE);
                }

                public void mouseExited(MouseEvent e) {
                    button.setBackground(Utils.BLUE);
                }

                public void mouseClicked(MouseEvent e) {
                    if (activeSetting != null) {
                        for (Component component : options.get(activeSetting)) {
                            StateStack.removeComponent(component);
                        }
                    }
                    activeSetting = button;
                    for (Component component : options.get(button)) {
                        JPanel panel = StateStack.getPanel();
                        panel.add(component);
                        component.repaint();
                    }
                }
            });
        }

        backButton = new GameButton(Jeopardy.WIN_WIDTH / 2 - BUTTON_WIDTH / 2, 8 * Jeopardy.WIN_HEIGHT / 10, BUTTON_WIDTH, BUTTON_HEIGHT, Utils.BLUE, Color.WHITE, "Back", 20) {
            @Override
            public void isClicked() {
                String resolution = (String) resolutionBox.getSelectedItem();
                changeResolution(resolution);
                StateStack.pop();
            }
        };

        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Utils.PURPLE);
            }

            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Utils.BLUE);
            }

            public void mouseClicked(MouseEvent e) {
                StateStack.removeComponent(backButton);
                for (GameButton button : settings) {
                    StateStack.removeComponent(button);
                    for (Component component : options.get(button)) {
                        StateStack.removeComponent(component);
                    }
                }
                backButton.isClicked();
            }
        });
    }

    public static SettingsState getInstance() {
        if (instance == null) {
            instance = new SettingsState();
        }

        return instance;
    }

    @Override
    public void render(JPanel panel) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        panel.add(backButton);
        backButton.repaint();

        for (GameButton button : settings) {
            panel.add(button);
            button.repaint();
        }

        graphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);
    }

    private void changeResolution(String resolution) {
        String[] splitResolution = resolution.split("x");
        int width = Integer.parseInt(splitResolution[0]);
        int height = Integer.parseInt(splitResolution[1]);
        Jeopardy.changeResolution(width, height);
    }

    @Override
    public void exit() {
        instance = null;
    }
}

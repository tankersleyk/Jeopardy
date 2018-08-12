package jeopardy.States;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;

import jeopardy.GameButton;
import jeopardy.Jeopardy;
import jeopardy.StateStack;
import jeopardy.Utils;

public class SettingsState extends BaseState{

    private static SettingsState instance = null;
    private Map<GameButton, List<Component>> options;
    private List<GameButton> settings;
    private final GameButton backButton;
    private static final BufferedImage displayIcon;

    private static final String[] RESOLUTIONS = {
            "1280x720", "1024x768", "1366x768", "1920x1080"
    };

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 100;
    private final int SETTINGS_PADDING = 20;

    static {
        try {
            displayIcon = ImageIO.read(new File("data/display.png"));
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
        displayButton.setHorizontalAlignment(SwingConstants.LEFT);

        // TODO: figure out how to fix graphical glitch
        JComboBox resolutionBox = new JComboBox(RESOLUTIONS);
        resolutionBox.setBounds(new Rectangle(
                displayButton.getX() + displayButton.getWidth() + SETTINGS_PADDING, displayButton.getY(), 300, 50
                ));

        resolutionBox.setUI(new BasicComboBoxUI());
        resolutionBox.setBackground(Utils.BLUE);
        resolutionBox.setForeground(Color.WHITE);

        settings.add(displayButton);
        options.put(displayButton, Arrays.asList(resolutionBox));

        for (GameButton button : settings) {
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    backButton.setBackground(Utils.PURPLE);
                }

                public void mouseExited(MouseEvent e) {
                    backButton.setBackground(Utils.BLUE);
                }

                public void mouseClicked(MouseEvent e) {
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

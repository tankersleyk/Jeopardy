package jeopardy.States;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import jeopardy.GameButton;
import jeopardy.Jeopardy;
import jeopardy.StateStack;
import jeopardy.Utils;

public class SettingsState extends BaseState{

    private static SettingsState instance = null;
    private List<Component> options;
    private final GameButton backButton;

    private static final String[] RESOLUTIONS = {
            "800x600", "1280x720", "1024x768", "1366x768", "1920x1080"
    };

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 100;

    @SuppressWarnings("serial")
    private SettingsState() {
        options = new ArrayList<>();

        JComboBox resolutionBox = new JComboBox(RESOLUTIONS);

        resolutionBox.setBounds(new Rectangle(
                Jeopardy.WIN_WIDTH / 4, Jeopardy.WIN_HEIGHT / 4, 300, 50
                ));

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
                for (Component component : options) {
                    StateStack.removeComponent(component);
                }
                backButton.isClicked();
            }
        });

        options.add(resolutionBox);
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

        for (Component component : options) {
            panel.add(component);
            component.repaint();
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

package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jeopardy.Jeopardy;
import jeopardy.Player;
import jeopardy.StateParams;
import jeopardy.StateStack;
import jeopardy.Utils;

public class PlayerCreationState extends BaseState{

    private static PlayerCreationState instance = null;
    private JTextField textfield = new JTextField("Enter name...", 0);
    private static final int TEXT_WIDTH = Jeopardy.WIN_WIDTH / 3;
    private static final int TEXT_HEIGHT = 20;

    private boolean hasEntered = false;

    /**
     * Create a way for the player to enter a name
     */
    private PlayerCreationState() {
        textfield.setBounds(
                Jeopardy.WIN_WIDTH / 2 - TEXT_WIDTH / 2,
                Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2 + Jeopardy.WIN_HEIGHT / (TEXT_HEIGHT / 2),
                TEXT_WIDTH, TEXT_HEIGHT);
        textfield.setForeground(Color.GRAY);
        textfield.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textfield.setText("");
                textfield.setForeground(Color.BLACK);
                hasEntered = true;
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                // do nothing
            }
        });

        // TODO: separate this into own class?
        textfield.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {

            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                int keyCode = arg0.getKeyCode();
                if (keyCode == Utils.ENTER_KEY && hasEntered) {
                    String name = textfield.getText();
                    if (name.length() > 0) {
                        StateStack.pop(); // Exit player creation state
                        StateStack.removeComponent(textfield);
                        StateStack.push(FirstRoundState.getInstance(), new StateParams(new Player(name)));
                        textfield.setText("Enter name...");
                        hasEntered = false;
                    }
                    else {
                        // TODO: add error message
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {

            }

        });
    }

    /**
     * get the instance of this player creation
     * @return the singleton PlayerCreationState object
     */
    public static PlayerCreationState getInstance() {
        if (instance == null) {
            instance = new PlayerCreationState();
        }
        return instance;
    }

    @Override
    public void render(JPanel panel) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        textfield.setBorder(null);
        panel.add(textfield);
        textfield.repaint();

        graphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);
        Rectangle2D textLocation = new Rectangle2D.Double(
                Jeopardy.WIN_WIDTH / 2 - TEXT_WIDTH / 2,
                Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2,
                TEXT_WIDTH, TEXT_HEIGHT);

        Utils.drawCenteredString(graphics, "Enter your name", textLocation, Color.WHITE, 50);
    }
}

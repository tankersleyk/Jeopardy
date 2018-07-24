package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.JTextField;

import jeopardy.Jeopardy;
import jeopardy.Player;
import jeopardy.Question;
import jeopardy.StateParams;
import jeopardy.StateStack;
import jeopardy.Utils;

public class QuestionAnsweringState extends BaseState{

    private static QuestionAnsweringState instance = null;
    private Question question;
    private Player player;

    private JTextField answerField = new JTextField("Enter answer...", 0);
    private static final int TEXT_WIDTH = Jeopardy.WIN_WIDTH / 3;
    private static final int TEXT_HEIGHT = 20;
    private boolean hasEntered = false;

    private QuestionAnsweringState() {
        answerField.setBounds(
                Jeopardy.WIN_WIDTH / 2 - TEXT_WIDTH / 2,
                Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2 + Jeopardy.WIN_HEIGHT / (TEXT_HEIGHT / 2),
                TEXT_WIDTH, TEXT_HEIGHT);
        answerField.setForeground(Color.GRAY);
        answerField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                answerField.setText("");
                answerField.setForeground(Color.BLACK);
                hasEntered = true;
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                // do nothing
            }
        });

        answerField.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {

            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                int keyCode = arg0.getKeyCode();
                if (keyCode == Utils.ENTER_KEY && hasEntered) {
                    String guess = answerField.getText();
                    if (guess.length() > 0) {
                        if (question.acceptAnswer(guess)) {
                            player.addMoney(question.getPoints());
                        }
                        else {

                        }
                        StateStack.pop(); // Exit player creation state
                        StateStack.removeComponent(answerField);
                        answerField.setText("Enter answer...");
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

    public static QuestionAnsweringState getInstance() {
        if (instance == null) {
            instance = new QuestionAnsweringState();
        }
        return instance;
    }

    @Override
    public void enter(StateParams params) {
        this.question = params.QUESTION;
        this.player = params.PLAYER;
    }

    @Override
    public void render(JPanel panel) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        answerField.setBorder(null);
        panel.add(answerField);
        answerField.repaint();

        graphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);
        Rectangle2D textLocation = new Rectangle2D.Double(
                Jeopardy.WIN_WIDTH / 2 - TEXT_WIDTH / 2,
                0,
                TEXT_WIDTH, Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2 + Jeopardy.WIN_HEIGHT / (TEXT_HEIGHT / 2));

        Utils.drawCenteredString(graphics, question.getQuestion(), textLocation, Color.WHITE, 50);
    }
}
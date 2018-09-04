package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import jeopardy.Jeopardy;
import jeopardy.Player;
import jeopardy.Question;
import jeopardy.Round;
import jeopardy.StateParams;
import jeopardy.StateStack;
import jeopardy.Utils;

public class QuestionAnsweringState extends BaseState{

    private static QuestionAnsweringState instance = null;
    private Question question;
    private Player player;
    private int value;

    private JTextField answerField = new JTextField("Enter answer...", 0);
    private static final int TEXT_WIDTH = Jeopardy.WIN_WIDTH / 3;
    private static final int TEXT_HEIGHT = 20;
    private static final int TEXT_X = Jeopardy.WIN_WIDTH / 2 - TEXT_WIDTH / 2;
    private static final int TEXT_Y = Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2 + Jeopardy.WIN_HEIGHT / (TEXT_HEIGHT / 2);
    private boolean hasEntered = false;

    private QuestionAnsweringState() {
        answerField.setBounds(TEXT_X, TEXT_Y, TEXT_WIDTH, TEXT_HEIGHT);
        answerField.setForeground(Color.GRAY);
        answerField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!hasEntered) {
                    answerField.setText("");
                    answerField.setForeground(Color.BLACK);
                    hasEntered = true;
                }
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
                    // in final jeopardy, must submit an answer no matter what
                    if (guess.length() > 0 || (Boolean.parseBoolean(Jeopardy.SETTINGS.getProperty("noGuess")) && !question.getRound().equals(Round.FINAL_JEOPARDY))) 
                        answerSubmit(guess);
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
        this.value = params.QUESTION_VALUE;
    }

    @Override
    public void render(JPanel panel) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        answerField.setBorder(null);
        panel.add(answerField);
        answerField.repaint();

        graphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        Rectangle2D categoryLocation = new Rectangle2D.Double(
                25,
                0,
                Jeopardy.WIN_WIDTH - 25,
                Jeopardy.WIN_HEIGHT / 8
                );
        Rectangle2D questionLocation = new Rectangle2D.Double(
                50,
                Jeopardy.WIN_HEIGHT / 8,
                Jeopardy.WIN_WIDTH - 50,
                Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2 - Jeopardy.WIN_HEIGHT / 8 + Jeopardy.WIN_HEIGHT / (TEXT_HEIGHT / 2));

        for (BufferedImage image : question.getImages()) {
            graphics.drawImage(Utils.resizeImage(image, TEXT_WIDTH, Jeopardy.WIN_HEIGHT - (TEXT_Y + TEXT_HEIGHT))
                    , null, TEXT_X, TEXT_Y + TEXT_HEIGHT);
        }
        Utils.drawCenteredString(graphics, question.getCategory(), categoryLocation, Color.WHITE, 30);
        Utils.drawCenteredString(graphics, question.getQuestion(), questionLocation, Color.WHITE, 50);
    }

    private void answerSubmit(String guess) {
        StateStack.removeComponent(answerField);
        JPanel panel = StateStack.getPanel();
        Graphics2D graphics = (Graphics2D) panel.getGraphics();
        BufferedImage tmpImage = new BufferedImage(Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        String correctnessString;
        Color correctnessColor;

        if (guess.length() == 0 && Boolean.parseBoolean(Jeopardy.SETTINGS.getProperty("noGuess"))) {
            correctnessString = "Did not guess";
            correctnessColor = Color.YELLOW;
        }
        else {
            if (question.acceptAnswer(guess)) {
                player.addMoney(value);
                correctnessString = "Correct";
                correctnessColor = Color.GREEN;
            }
            else {
                if (Boolean.parseBoolean(Jeopardy.SETTINGS.getProperty("noGuess")) || question.getRound() == Round.FINAL_JEOPARDY) {
                    player.subtractMoney(value);
                }
                correctnessString = "Incorrect";
                correctnessColor = Color.RED;
            }
        }

        Utils.drawCenteredString(tmpGraphics, correctnessString, new Rectangle2D.Double(
                0, 0, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT / 3
                ), correctnessColor, 50);

        Utils.drawCenteredString(tmpGraphics, "Your answer: " + guess, new Rectangle2D.Double(
                0, Jeopardy.WIN_HEIGHT / 3, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT / 3
                ), Color.WHITE, 30);

        Utils.drawCenteredString(tmpGraphics, "Correct answer: " + question.getAnswer(), new Rectangle2D.Double(
                0, 2 * Jeopardy.WIN_HEIGHT / 3, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT / 3
                ), Color.WHITE, 30);

        Utils.drawCenteredString(tmpGraphics, "Press Enter to continue...", new Rectangle2D.Double(
                0, 9 * Jeopardy.WIN_HEIGHT / 10, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT / 10
                ), Color.WHITE, 20);

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "exitQuestion");
        panel.getActionMap().put("exitQuestion", new exitQuestionState(panel));

        graphics.drawImage(tmpImage, null, 0, 0);
    }

    @SuppressWarnings("serial")
    private class exitQuestionState extends AbstractAction {

        private JPanel panel;

        exitQuestionState(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StateStack.pop(); // Exit player creation state
            StateStack.removeComponent(answerField);
            answerField.setText("Enter answer...");
            hasEntered = false;
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "none");
        }
    }
}

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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
                    if (guess.length() > 0 || !question.getRound().equals(Round.FINAL_JEOPARDY)) // in final jeopardy, must submit an answer
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
        Rectangle2D textLocation = new Rectangle2D.Double(
                0,
                0,
                Jeopardy.WIN_WIDTH,
                Jeopardy.WIN_HEIGHT / 2 - TEXT_HEIGHT / 2 + Jeopardy.WIN_HEIGHT / (TEXT_HEIGHT / 2));

        renderQuestion(question.getQuestion(), panel, textLocation);
        //        Utils.drawCenteredString(graphics, question.getQuestion(), textLocation, Color.WHITE, 50);
    }

    private void renderQuestion(String question, JPanel panel, Rectangle2D textLocation) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        StringBuilder drawnQuestion = new StringBuilder();
        List<URL> URLs = new ArrayList<>();

        for (int i = 0; i < question.length(); i++) {
            if (question.charAt(i) == '<') {
                if (question.charAt(i+1) == '/') {
                    drawnQuestion.append(" ");
                    i+=4;
                }
                else if (question.charAt(i+1) == 'i') {
                    i+=3;
                }
                else {
                    StringBuilder url = new StringBuilder();
                    while(question.charAt(i) != '"') {
                        i+=1;
                    }

                    i+=2;

                    while (question.charAt(i) != '"') {
                        url.append(question.charAt(i));
                        i+=1;
                    }

                    i+=3;
                    try {
                        URLs.add(new URL(url.toString()));
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            else {
                drawnQuestion.append(question.charAt(i));
            }
        }

        String extraInfo = "arget=\"\"_blank\"\">";

        if (drawnQuestion.toString().contains(extraInfo)) {
            drawnQuestion = new StringBuilder(drawnQuestion.toString().replaceAll(extraInfo, ""));
        }

        for (URL url : URLs) {
            // TODO: Handle more than one image
            if (url.getPath().contains("jpg")) { // Image
                try {
                    graphics.drawImage(Utils.resizeImage(ImageIO.read(url), TEXT_WIDTH, Jeopardy.WIN_HEIGHT - (TEXT_Y + TEXT_HEIGHT))
                            , null, TEXT_X, TEXT_Y + TEXT_HEIGHT);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            else if (url.getPath().contains("wmv")) { // audio

            }
        }

        Utils.drawCenteredString(graphics, Utils.stripDoubleQuotes(drawnQuestion.toString()), textLocation, Color.WHITE, 50);
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

        if (guess.length() == 0) {
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
                player.subtractMoney(value);
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

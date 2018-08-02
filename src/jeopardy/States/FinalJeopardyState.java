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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jeopardy.Jeopardy;
import jeopardy.Player;
import jeopardy.Question;
import jeopardy.QuestionParser;
import jeopardy.Round;
import jeopardy.StateParams;
import jeopardy.StateStack;
import jeopardy.Utils;

public class FinalJeopardyState extends BaseState{

    private static FinalJeopardyState instance = null;

    private static final BufferedImage PODIUM;
    private final File questionFile = new File("data/questions.csv");

    private Question question;
    private final String category;
    private Player player;
    private JTextField textfield = new JTextField("Enter bet..", 0);

    private static final int TEXT_WIDTH = Jeopardy.WIN_WIDTH / 3;
    private static final int TEXT_HEIGHT = 20;
    private boolean hasEntered = false;

    private int betAmount;

    static {
        try {
            PODIUM = ImageIO.read(new File("data/podium.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in podium");
        }
    }

    private FinalJeopardyState() {
        List<Question> allQuestions;
        try {
            allQuestions = QuestionParser.parse(questionFile, Round.FINAL_JEOPARDY);
        } catch (IOException e) {
            System.err.println("Could not read in questions");
            allQuestions = new ArrayList<>();
        }

        Random random = new Random();
        int i = 0;
        for (Question question : allQuestions) {
            i+=1;
            if (random.nextInt(i) == 0) {
                this.question = question;
            }
        }

        category = this.question.getCategory();

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
                    try {
                        Integer bet = Integer.parseInt(textfield.getText());
                        if (bet <= player.getMoney()) {
                            betAmount = bet;
                            StateStack.removeComponent(textfield);
                            StateStack.push(QuestionAnsweringState.getInstance(), new StateParams(player, question, betAmount));
                            textfield.setText("Enter bet..");
                            hasEntered = false;
                        }
                        else if (bet > player.getMoney()) {
                            wrongBet("You cannot bet more than you have");
                        }
                        else {
                            wrongBet("You cannot bet a negative amount");
                        }
                    }
                    catch (NumberFormatException e) {
                        wrongBet("Not a valid number");
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {

            }
        });
    }

    @Override
    public void enter(StateParams params) {
        this.player = params.PLAYER;
    }

    /**
     * get the instance of this Final Jeopardy State
     * @return the singleton PlayerCreationState object
     */
    public static FinalJeopardyState getInstance() {
        if (instance == null) {
            instance = new FinalJeopardyState();
        }
        return instance;
    }

    @Override
    public void render(JPanel panel) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        tmpGraphics.drawImage(Utils.resizeImage(PODIUM, PODIUM.getWidth(), (int) (Jeopardy.WIN_HEIGHT * 0.2)), null, Jeopardy.WIN_WIDTH / 2 - PODIUM.getWidth() / 2, (int) (Jeopardy.WIN_HEIGHT * 0.8));

        Utils.drawCenteredString(tmpGraphics, "$" + player.getMoney(), new Rectangle2D.Double(
                // TODO: calculate this more exactly
                Jeopardy.WIN_WIDTH / 2 - PODIUM.getWidth() / 2,
                Jeopardy.WIN_HEIGHT * 0.8 + PODIUM.getHeight() / 10,
                PODIUM.getWidth(),
                PODIUM.getHeight() / 4.75), Color.WHITE, 16);

        Utils.drawCenteredString(tmpGraphics, "Category: " + '\n' + category, new Rectangle2D.Double(
                0,
                0,
                Jeopardy.WIN_WIDTH,
                Jeopardy.WIN_HEIGHT * .8 / 2), Color.WHITE, 30);

        textfield.setBorder(null);
        panel.add(textfield);
        textfield.repaint();

        graphics.drawImage(tmpImage, null, 0, 0);
    }

    private void wrongBet(String errorMessage) {

    }

    @Override
    public void returnToState() {
        JPanel panel = StateStack.getPanel();
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        tmpGraphics.drawImage(Utils.resizeImage(PODIUM, PODIUM.getWidth(), (int) (Jeopardy.WIN_HEIGHT * 0.2)), null, Jeopardy.WIN_WIDTH / 2 - PODIUM.getWidth() / 2, (int) (Jeopardy.WIN_HEIGHT * 0.8));

        Utils.drawCenteredString(tmpGraphics, "$" + player.getMoney(), new Rectangle2D.Double(
                // TODO: calculate this more exactly
                Jeopardy.WIN_WIDTH / 2 - PODIUM.getWidth() / 2,
                Jeopardy.WIN_HEIGHT * 0.8 + PODIUM.getHeight() / 10,
                PODIUM.getWidth(),
                PODIUM.getHeight() / 4.75), Color.WHITE, 16);

        Utils.drawCenteredString(tmpGraphics, "Your final earnings: " + '\n' + player.getMoney(), new Rectangle2D.Double(
                0,
                0,
                Jeopardy.WIN_WIDTH,
                Jeopardy.WIN_HEIGHT * .8 / 2), Color.WHITE, 30);

        graphics.drawImage(tmpImage, null, 0, 0);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }

        StateStack.pop();
        instance = null;
        StateStack.push(MainMenuState.getInstance());
    }

}

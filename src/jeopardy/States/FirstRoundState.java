package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import jeopardy.GameButton;
import jeopardy.Jeopardy;
import jeopardy.Player;
import jeopardy.Question;
import jeopardy.QuestionParser;
import jeopardy.Round;
import jeopardy.StateParams;
import jeopardy.StateStack;
import jeopardy.Utils;

public class FirstRoundState extends BaseState{

    private static FirstRoundState instance = null;
    private static final BufferedImage PODIUM;
    private final Round round = Round.JEOPARDY;
    private Map<String, Map<Integer, Question>> questions;

    private List<GameButton> questionButtons;
    private List<String> categoryList;

    private Player player;

    private static final int SPACING = 20; // how many pixels to leave between boxes
    private static final int CATEGORIES = 5; // 5 categories in a round
    private static final int QUESTIONS_PER_CAT = 5; // 5 questions per category

    private final File questionFile = new File("data/questions.csv");

    static {
        try {
            PODIUM = ImageIO.read(new File("data/podium.png"));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read in podium");
        }
    }

    @SuppressWarnings("serial")
    private FirstRoundState() {
        List<Question> allQuestions;
        try {
            allQuestions = QuestionParser.parse(questionFile, round);
        } catch (IOException e) {
            System.err.println("Could not read in questions");
            allQuestions = new ArrayList<>();
        }
        Set<String> categories = new HashSet<>();
        for (Question q : allQuestions) {
            categories.add(q.getCategory());
        }

        Random random = new Random();
        int i = 0;
        List<String> chosenCategories = new ArrayList<>();
        for (String category : categories) { // pick 5 categories at random
            i+=1;
            if (i <= 5) { // TODO: Handle case where less than 5 categories, or require never happens
                chosenCategories.add(category);
            }
            else {
                int replaceIndex = random.nextInt(5);

                if (random.nextInt(i) < 5) { // swap with probability 5/i, 
                    chosenCategories.set(replaceIndex, category);
                }
            }
        }

        this.questions = new HashMap<>();

        for (String category : chosenCategories) {
            questions.put(category, pullQuestions(allQuestions, category));
        }

        for (String category : chosenCategories) {
            while (questions.get(category).size() < 5) { // Less than 5 questions in category
                // Leaving debug print statements in for now
                System.out.println("Replacing " + category + " with a new one because it only has " + questions.get(category).size() + " questions!");
                // Replace this category with a new one
                questions.remove(category);
                i = 0;
                for (String newCategory : categories) {
                    i+=1;
                    if (random.nextInt(i) == 0) {
                        category = newCategory;
                    }
                }
                System.out.println("New category is: " + category);
                questions.put(category, pullQuestions(allQuestions, category));
            }
        }

        categoryList = new ArrayList<>();
        for (String category : questions.keySet()) {
            categoryList.add(category);
        }

        questionButtons = new ArrayList<>();

        for (i = 0; i < categoryList.size(); i++) {
            // TODO: Scale font size with screen size
            String category = categoryList.get(i);
            for (int value = 1; value <= 5; value++) { // Question values
                int points = value * 200;
                GameButton question = new GameButton(
                        SPACING + i*((Jeopardy.WIN_WIDTH - SPACING * (CATEGORIES + 1))/CATEGORIES + SPACING),
                        SPACING + value*((int) ((0.8 * Jeopardy.WIN_HEIGHT) - SPACING * (QUESTIONS_PER_CAT + 2))/(QUESTIONS_PER_CAT + 1) + SPACING),
                        (Jeopardy.WIN_WIDTH - SPACING * (CATEGORIES + 1))/CATEGORIES,
                        ((int) (Jeopardy.WIN_HEIGHT * 0.8) - SPACING * (QUESTIONS_PER_CAT + 2))/(QUESTIONS_PER_CAT + 1),
                        Utils.BLUE, Utils.ORANGE, "$" + Integer.toString(points), 50) {
                    @Override
                    public void isClicked() {
                        for (GameButton button : questionButtons) {
                            StateStack.removeComponent(button);
                        }
                        Question question = questions.get(category).get(points);
                        if (question == null) {
                            System.out.println("err");
                            StateStack.render();
                        }
                        else {
                            StateStack.push(QuestionAnsweringState.getInstance(), new StateParams(player, question));
                        }
                    }
                };
                questionButtons.add(question);
            }
        }

        // Set up button highlighting
        for (GameButton button : questionButtons) {
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(Utils.PURPLE);
                }

                public void mouseExited(MouseEvent e) {
                    button.setBackground(Utils.BLUE);
                }

                public void mouseClicked(MouseEvent e) {
                    StateStack.removeComponent(button);
                    questionButtons.remove(button);
                    button.isClicked();
                }
            });
        }
    }

    /**
     * Get the instance of this first round
     * 
     * @return instance the singleton FirstRoundState object
     */
    public static FirstRoundState getInstance() {
        if (instance == null) {
            instance = new FirstRoundState();
        }

        return instance;
    }

    @Override
    public void enter(StateParams params) {
        player = params.PLAYER;
    }

    @Override
    public void render(JPanel panel) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();
        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();
        tmpGraphics.setColor(Utils.BLUE);

        tmpGraphics.drawImage(Utils.resizeImage(Jeopardy.BACKGROUND, Jeopardy.WIN_WIDTH, Jeopardy.WIN_HEIGHT), null, 0, 0);

        tmpGraphics.drawImage(Utils.resizeImage(PODIUM, PODIUM.getWidth(), (int) (Jeopardy.WIN_HEIGHT * 0.2)), null, Jeopardy.WIN_WIDTH / 2 - PODIUM.getWidth() / 2, (int) (Jeopardy.WIN_HEIGHT * 0.8));

        Utils.drawCenteredString(tmpGraphics, "$" + player.getMoney(), new Rectangle2D.Double(
                // TODO: calculate this more exactly
                Jeopardy.WIN_WIDTH / 2 - PODIUM.getWidth() / 2,
                Jeopardy.WIN_HEIGHT * 0.8 + PODIUM.getHeight() / 10,
                PODIUM.getWidth(),
                PODIUM.getHeight() / 4.75
                ), Color.WHITE, 16);

        // Draw manually instead of JButtons so that text can go to multiple lines - maybe change later by inserting new lines
        for (int i = 0; i < categoryList.size(); i++) { // Categories
            Rectangle2D categoryBox = new Rectangle2D.Double(
                    SPACING + i*((Jeopardy.WIN_WIDTH - SPACING * (CATEGORIES + 1))/CATEGORIES + SPACING),
                    SPACING,
                    (Jeopardy.WIN_WIDTH - SPACING * (CATEGORIES + 1))/CATEGORIES,
                    (Jeopardy.WIN_HEIGHT * .8 - SPACING * (QUESTIONS_PER_CAT + 2))/(QUESTIONS_PER_CAT + 1));
            tmpGraphics.fill(categoryBox);
            Utils.drawCenteredString(tmpGraphics, categoryList.get(i), categoryBox, Color.WHITE, 14);
        }

        for (GameButton question : questionButtons) {
            panel.add(question);
            question.repaint();
        }

        graphics.drawImage(tmpImage, null, 0, 0);
    }

    /**
     * Pulls 5 questions of increasing value from 5 distinct categories from a list of questions
     * @param questions the list of questions to pull from
     * @param category the category to pull questions from
     * @return the randomly selected questions
     */
    @SuppressWarnings("serial")
    private Map<Integer, Question> pullQuestions(List<Question> questions, String category) {
        Map<Integer, Question> pulledQuestions = new HashMap<>();
        Map<Integer, Integer> randomMap = new HashMap<Integer, Integer>() {{
            put(200, 0);
            put(400, 0);
            put(600, 0);
            put(800, 0);
            put(1000, 0);
        }};
        Random random = new Random();

        for (Question question : questions) {
            if (question.getCategory().equals(category) && randomMap.containsKey(question.getPoints())) {
                int points = question.getPoints();
                randomMap.put(points, randomMap.get(points) + 1);
                if (random.nextInt(randomMap.get(points)) == 0) { // swap with probability 1/i, 
                    pulledQuestions.put(points, question);
                }
            }
        }

        // Handle missing values due to daily double values
        int replaceValue = 0;
        for (int points : randomMap.keySet()) {
            if (randomMap.get(points) == 0) {
                replaceValue = points;
                break;
            }
        }

        if (replaceValue != 0) {
            for (Question question : questions) {
                // Not already present, and is matching category
                if (!pulledQuestions.values().contains(question) && question.getCategory().equals(category)) {
                    // Will add question assuming its missing value - will not be accurate 100% of the time, but close enough
                    pulledQuestions.put(replaceValue, question);
                    break;
                }
            }
        }
        return pulledQuestions;
    }

}

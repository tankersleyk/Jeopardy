package jeopardy.States;

import java.awt.Color;
import java.awt.Graphics2D;
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

import jeopardy.Question;
import jeopardy.QuestionParser;
import jeopardy.Round;
import jeopardy.Utils;

public class FirstRoundState extends BaseState{

    private static FirstRoundState instance = null;
    private final Round round = Round.JEOPARDY;
    private Map<String, Map<Integer, Question>> questions;

    private final File questionFile = new File("data/questions.csv");

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
                    chosenCategories.add(replaceIndex, category);
                }
            }
        }
        this.questions = new HashMap<>();

        for (String category : chosenCategories) {
            questions.put(category, pullQuestions(allQuestions, category));
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
    public void render(Graphics2D graphics) {
        // Use temporary image to draw everything all at once to graphics and prevent flickering
        BufferedImage tmpImage = new BufferedImage(800, 800, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D tmpGraphics = (Graphics2D) tmpImage.getGraphics();

        tmpGraphics.setColor(Utils.BLUE);
        List<String> categoryList = new ArrayList<>();
        for (String category : questions.keySet()) {
            categoryList.add(category);
        }
        for (int i = 0; i < categoryList.size(); i++) { // Categories
            Rectangle2D categoryBox = new Rectangle2D.Double(i*(800/5), 0, 800/5, 800/6);
            tmpGraphics.fill(categoryBox);
            Utils.drawCenteredString(tmpGraphics, categoryList.get(i), categoryBox, Color.WHITE, 20);

            for (int value = 1; value <= 5; value++) { // Question values
                Rectangle2D valueBox = new Rectangle2D.Double(i*(800/5), value*(800/6), 800/5, 800/6);
                tmpGraphics.fill(valueBox);
                Utils.drawCenteredString(tmpGraphics, "$" + Integer.toString(value*200), valueBox, Utils.ORANGE, 60);
            }
        }

        graphics.drawImage(tmpImage, null, 0, 0);
    }


    /**
     * Pulls 5 questions of increasing value from 5 distinct categories from a list of questions
     * @param questions the list of questions to pull from
     * @param category the category to pull questions from, must be in the categories set and must contain at least 
     *          one question of the corresponding category for each respective price TODO reword 
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
            if (question.getCategory().equals(category) && randomMap.containsKey(question.getPoints())) { // data uses values gained from daily doubles - remove most of those
                int points = question.getPoints();
                randomMap.put(points, randomMap.get(points) + 1);
                if (random.nextInt(randomMap.get(points)) == 0) { // swap with probability 1/i, 
                    pulledQuestions.put(points, question);
                }
            }
        }
        return pulledQuestions;
    }

}

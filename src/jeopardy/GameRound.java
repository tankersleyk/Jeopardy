package jeopardy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A round of the game (Jeopardy, Double Jeopardy)
 *
 */
public class GameRound implements GameState {
    
    private final Round round;
    private List<Question> questions;
    private final Random randomizer = new Random();
  
    private final File questionFile = new File("data/questions.csv");
    
    /**
     * Create a new game round
     * @param round the round to be implemented (Jeopardy, Double Jeopardy)
     */
    public GameRound(Round round) {
        this.round = round;
        try {
            this.questions = QuestionParser.parse(questionFile);
        } catch (IOException e) {
            System.err.println("Could not read in questions");
            this.questions = new ArrayList<>();
        }
    }

    @Override
    public void drawGraphics(Graphics2D g) {
        final Color oldColor = g.getColor();
        g.setColor(Color.WHITE);
        
        g.drawString("First round!", 400, 500);
        
        g.setColor(oldColor);
    }

    @Override
    public void handleClick(MouseEvent me) {
        Question randomQuestion = questions.get(randomizer.nextInt(questions.size()));
        System.out.println(randomQuestion.points + ": " + randomQuestion.question + " (" + randomQuestion.answer + ")");
    }
    
    
}

package jeopardy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A Jeopardy! question
 *
 */
public class Question {
    
    private final Round round;
    private final String category;
    private final Calendar date;
    private final String question;
    private final String answer;
    private final int points;
    private static final Calendar pointChange = new GregorianCalendar(2001, 10, 26);
    
    /**
     * Create a new Question
     * @param round the round (Jeopardy, Double Jeopardy, etc.) the question is from
     * @param category the category of the question
     * @param question the question itself
     * @param answer the answer to the question
     * @param points the value of the question in USD
     */
    public Question(Round round, String category, Calendar date, String question, String answer, int points) {
        this.round = round;
        this.category = category;
        this.date = date;
        this.question = question;
        this.answer = answer;
        if (date.before(pointChange.getTime())) { // On November 26, 2001 point values were doubled
            this.points = points * 2;
        }
        else {
            this.points = points;
        }
    }
    
    /**
     * TODO
     */
    public Round getRound() {
        return this.round;
    }
    
    /**
     * TODO
     */
    public String getCategory() {
        return this.category;
    }
    
    /**
     * TODO
     */
    public Date getDate() {
        return this.date.getTime();
    }
    
    /**
     * TODO
     */
    public String getQuestion() {
        return this.question;
    }
    
    /**
     * TODO
     */
    public String getAnswer() {
        return this.answer;
    }
    
    /**
     * TODO
     */
    public int getPoints() {
        return this.points;
    }
    
    /**
     * Decides if a guess is correct
     * @param guess the contestant's guess 
     * @return true iff the guess is an acceptable answer to this question
     */
    public Boolean acceptAnswer(String guess) { // numbers written out vs numerically, parentheses in answers, minor typos, etc.
        return guess.toLowerCase().equals(answer.toLowerCase()); //placeholder for now
    }
}

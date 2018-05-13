package jeopardy;

/**
 * A Jeopardy! question
 *
 */
public class Question {
    
    final Round round;
    final String category;
    final String question;
    final String answer;
    final int points;
    
    /**
     * Create a new Question
     * @param round the round (Jeopardy, Double Jeopardy, etc.) the question is from
     * @param category the category of the question
     * @param question the question itself
     * @param answer the answer to the question
     * @param points the value of the question in USD
     */
    public Question(Round round, String category, String question, String answer, int points) {
        this.round = round;
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.points = points;
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

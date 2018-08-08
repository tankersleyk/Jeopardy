package jeopardy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    private static final Calendar pointChange = new GregorianCalendar(2001, 11, 26);

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
        this.category = Utils.stripDoubleQuotes(category);
        this.date = date;
        this.question = question;
        this.answer = answer;
        if (date.before(pointChange)) { // On November 26, 2001 point values were doubled
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
    public Boolean acceptAnswer(String guess) {
        // TODO: numbers numerical vs written out, difference in how parentheses at start vs. end are handled in jeopardy answers?
        String strippedGuess = Utils.stripSymbols(Utils.stripArticles(guess.toLowerCase()));
        String noArticles = Utils.stripArticles(answer.toLowerCase());
        String strippedAnswer = Utils.stripSymbols(noArticles);

        try {
            String[] answers = parenthesesCheck(noArticles);
            String answer1 = Utils.stripSymbols(answers[0]);
            String answer2 = Utils.stripSymbols(answers[1]);

            if (Utils.editDistance(strippedGuess, answer1) <= answer1.length() / 5 ||
                    Utils.editDistance(strippedGuess, answer2) <= answer2.length() / 5) {
                return true;
            }
        }

        catch (IllegalArgumentException e) {

        }

        if (noArticles.contains(" or ")) {
            for (String altAnswer : noArticles.split(" or ")) {
                String answerToCheck = Utils.stripSymbols(altAnswer);
                if (Utils.editDistance(strippedGuess, answerToCheck) <= answerToCheck.length() / 5) {
                    return true;
                }
            }
        }

        for (String slashAnswer : slashCheck(noArticles)) {
            String answerToCheck = Utils.stripSymbols(slashAnswer);
            if (Utils.editDistance(strippedGuess, answerToCheck) <= answerToCheck.length() / 5) {
                return true;
            }
        }

        if (noArticles.contains(" &" )) {
            int i = 0;
            while (noArticles.charAt(i) != '&') {
                i+=1;
            }
            String answerToCheck = Utils.stripSymbols(noArticles.substring(i+1) + noArticles.substring(0, i));
            if (Utils.editDistance(strippedGuess, answerToCheck) <= answerToCheck.length() / 5) {
                return true;
            }
        }

        return Utils.editDistance(strippedGuess, strippedAnswer) <= strippedAnswer.length() / 5; // one typo per 5 characters
    }

    private static List<String> slashCheck(String answer) {
        List<String> answers = new ArrayList<>();

        if (answer.contains("/")) {
            for (int i = 0; i < answer.length(); i++) {
                if (answer.charAt(i) == '/') {
                    int startIndex = i;
                    while (startIndex > 0 && answer.charAt(startIndex) != ' ') {
                        startIndex-=1;
                    }
                    int endIndex = i;
                    while (endIndex < answer.length() && answer.charAt(endIndex) != ' ') {
                        endIndex+=1;
                    }
                    String s1 = answer.substring(0, i);
                    String s2 = answer.substring(0, startIndex) + answer.substring(i+1, endIndex);
                    for (String otherSplit : slashCheck(answer.substring(endIndex))) {
                        answers.add(s1 + otherSplit);
                        answers.add(s2 + otherSplit);
                    }
                    break;
                }
            }
        }
        else {
            answers.add(answer);
        }

        return answers;
    }

    /**
     * Splits an answer containing parentheses into two separate strings - those within the parentheses and those out of it
     * @param answer the answer to perform this check on, should contain <= 1 set of parentheses
     * @return an array of two strings - the string not in the parentheses and the string in the parentheses
     * @throws IllegalArgumentException if the answer does not contain both a closing and opening parentheses
     */
    private static String[] parenthesesCheck(String answer) throws IllegalArgumentException {
        // Will cause issues if parentheses aren't at beginning or end
        String[] answers = new String[2];

        int startIndex = Integer.MAX_VALUE;
        int endIndex = Integer.MAX_VALUE;

        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == '(') {
                startIndex = i;
            }

            else if (answer.charAt(i) == ')') {
                endIndex = i;
                break;
            }
        }

        if (startIndex == Integer.MAX_VALUE || endIndex == Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }

        answers[0] = answer.substring(startIndex + 1, endIndex);
        answers[1] = answer.substring(0, startIndex) + answer.substring(endIndex + 1);

        return answers;
    }
}

package jeopardy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;

import sun.audio.AudioStream;

/**
 * A Jeopardy! question
 *
 */
public class Question {

    private final Round round;
    private final String category;
    private final Calendar date;
    private String question;
    private final String answer;
    private final int points;
    private List<BufferedImage> images;
    private List<AudioStream> sounds;
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
     * Get the round (Jeopardy, Double, Final) that this question is from
     * @return the round this question is originally from
     */
    public Round getRound() {
        return this.round;
    }

    /**
     * Get the category that this question is from
     * @return the category this quesiton is associated with
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Get the date that this question originally aired
     * @return the date of the original air of the Jeopardy! show this question is from
     */
    public Date getDate() {
        return this.date.getTime();
    }

    /**
     * Get the question to be asked
     * @return the actual question to be asked
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * Get the answer to this question
     * @return the answer to the question returned by getQuestion
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * Get the value this question is worth
     * @return the amount (in dollars) of money this question was worth on its original air of Jeopardy
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Update this question object to scan for links in its question and separate them from the question
     * @return true iff the question's links (if there are any) can be properly read
     * @effects the question associated with this object will have its links and html tags removed
     */
    public boolean checkLinks() {
        images = new ArrayList<>();

        int linkStart = question.indexOf("href=\"\"");
        while (linkStart != -1) {
            linkStart+=7;
            int linkEnd;
            if (question.contains("target=")) {
                linkEnd = question.indexOf("\"\" target=");
            }
            else {
                linkEnd = question.indexOf("\"\">");
            }
            try {
                URL url = new URL(question.substring(linkStart, linkEnd));
                if (question.substring(linkStart, linkEnd).contains(".jpg")) { // j-archive stores all images as jpg's
                    try {
                        images.add(ImageIO.read(url));
                    } catch (IOException e) {
                        return false;
                    }
                }
                else if (question.substring(linkStart, linkEnd).contains(".mp3")) {
                    try {
                        AudioStream audio = new AudioStream(url.openStream());
                        sounds.add(audio);
                        System.out.println(category);
                    } catch (IOException e) {
                        return false;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            question = question.replaceFirst("<a href=\\\"\\\"[^>]+>", "");
            linkStart = question.indexOf("href=\"\"");
        }
        while (question.contains("<")) {
            question = question.replaceAll("<[^>]+>", "");
        }
        if (question.toLowerCase().contains("seen here") && images.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Get the images from the links associated with this question, MUST FIRST CALL checkLinks()
     * @return the images associated with this question
     */
    public List<BufferedImage> getImages() {
        if (this.images == null) {
            throw new RuntimeException("must call checkLinks() before getting images");
        }
        return this.images;
    }

    /**
     * Decides if a guess is correct
     * @param guess the contestant's guess 
     * @return true iff the guess is an acceptable answer to this question
     */
    public Boolean acceptAnswer(String guess) {
        // TODO: numbers numerical vs written out, and/&
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

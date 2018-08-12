package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import jeopardy.Question;
import jeopardy.Round;

class QuestionsTest {

    // checkLinks()

    /*
     * TODO partitions
     * 
     * 
     */

    // question has one valid image link
    @Test
    void ContainsOneImageLink() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "<a href=\"\"https://cdn.vox-cdn.com/thumbor/Pkmq1nm3skO0-j693JTMd7RL0Zk=/0x0:2012x1341/1200x800/filters:focal(0x0:2012x1341)/cdn.vox-cdn.com/uploads/chorus_image/image/47070706/google2.0.0.jpg\"\" target=\"\"_blank\"\">test question</a>", "test", 0);
        assertTrue(testQuestion.checkLinks());
        assertEquals(testQuestion.getQuestion(), "test question");
    }

    // guessAnswer()

    /*
     * Partition the inputs as follows:
     * 
     * guess: correct exactly
     *        correct, off by an edit distance of 20%
     *        incorrect
     *        edit distance by deletion
     *        edit distance by insertion
     *        edit distance by replacement
     *        
     * answer: contains no special symbols
     *         contains symbols such as spaces, hyphens
     *         contains parentheses to indicate multiple answers
     *         contains parentheses to indicate optional part of answer
     *         contains "/" to indicate multiple answers
     *         contains "or" to indicate multiple answers
     *         contains "&" to indicate two-part answer
     */

    // guess: correct exactly
    // answer: contains no special symbols
    @Test
    void CorrectGuess() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test", 0);
        String guess = "test";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: edit distance replacement
    // answer: contains no special symbols
    @Test
    void CorrectGuessEditDistanceReplacement() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "testtesting", 0);
        String guess = "Testzeszing";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: edit distance insertion
    // answer: contains no special symbols
    @Test
    void CorrectGuessEditDistanceInsertion() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "testtesting", 0);
        String guess = "Testzteszting";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: edit distance deletion
    // answer: contains no special symbols
    @Test
    void CorrectGuessEditDistanceDeletion() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "testtesting", 0);
        String guess = "Testesing";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: incorrect
    // answer: contains no special symbols
    @Test
    void IncorrectGuess() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test", 0);
        String guess = "";
        assertFalse(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess, edit distance
    // answer: contains special symbols
    @Test
    void CorrectGuessSpecialSymbols() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test testing-testz", 0);
        String guess = "testtesingestzz";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: incorrect guess
    // answer: contains special symbols
    @Test
    void IncorrectGuessSpecialSymbols() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test-", 0);
        String guess = "tes";
        assertFalse(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess, 
    // answer: contains parentheses for alternative answer
    @Test
    void CorrectGuessNotInParenthesesRight() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test (testing)", 0);
        String guess = "test";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess, 
    // answer: contains parentheses for alternative answer
    @Test
    void CorrectGuessInParenthesesRight() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test (testing)", 0);
        String guess = "testin";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess, 
    // answer: contains parentheses for alternative answer
    @Test
    void CorrectGuessNotInParenthesesLeft() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "(test) testing", 0);
        String guess = "testin";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess, 
    // answer: contains parentheses for alternative answer
    @Test
    void CorrectGuessInParenthesesLeft() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "(test) testing", 0);
        String guess = "test";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains parenthesis for optional part of answer
    @Test
    void CorrectGuessOptionalParentheses() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal (test) testing", 0);
        String guess = "portal testi";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains / to indicate multiple answers
    @Test
    void CorrectGuessBothSidesSlash() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test/testing", 0);
        String guess = "test/testi";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains / to indicate multiple answers
    @Test
    void CorrectGuessLeftSideSlash() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test/testing", 0);
        String guess = "test";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains / to indicate multiple answers
    @Test
    void CorrectGuessRightSideSlash() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test/testing", 0);
        String guess = "testing";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains / to indicate multiple answers
    @Test
    void CorrectGuessMultipleWordBothSidesSlash() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal test/testing here", 0);
        String guess = "portal test/testing here";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains / to indicate multiple answers
    @Test
    void CorrectGuessMultipleWordLeftSideSlash() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal test/testing here", 0);
        String guess = "portal test here";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains / to indicate multiple answers
    @Test
    void CorrectGuessMultipleWordRightSideSlash() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal test/testing here", 0);
        String guess = "portal testing here";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains or to indicate multiple answers
    @Test
    void CorrectGuessBothSidesOr() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal test or testing here", 0);
        String guess = "portal test or testing here";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains or to indicate multiple answers
    @Test
    void CorrectGuessLeftSideOr() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal test or testing here", 0);
        String guess = "portal test";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains or to indicate multiple answers
    @Test
    void CorrectGuessRightSideOr() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "portal test or testing here", 0);
        String guess = "testing here";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains or to indicate multiple answers
    @Test
    void CorrectGuessSameSideAmpersand() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test & testing", 0);
        String guess = "test & testing";
        assertTrue(testQuestion.acceptAnswer(guess));
    }

    // guess: correct guess
    // answer: contains or to indicate multiple answers
    @Test
    void CorrectGuessOppositeSideAmpersand() {
        Question testQuestion = new Question(Round.JEOPARDY, "Test Category", new GregorianCalendar(2018, 7, 7), "test question", "test & testing", 0);
        String guess = "testing & test";
        assertTrue(testQuestion.acceptAnswer(guess));
    }
}

package jeopardy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A parser that reads in csv files of Jeopardy questions of the form
 *          Show number, Air date (YYYY-MM-DD), Round (Jeopardy, Double Jeopardy, etc.), Category (in quotes),
 *           Value ("$xxx"), Question (in quotes), Answer (in quotes)
 *
 */
public class QuestionParser {

    /**
     * Parses a csv file of jeopardy questions
     * @param file the csv file to be parsed
     * @param round the game round to get questions from
     * @return the list of Questions in the file that much the given round
     * @throws IOException if the file could not be read
     */
    public static List<Question> parse(File file, Round gameRound) throws IOException {
        List<Question> questions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = br.readLine(); // don't care about first line

        while ((st = br.readLine()) != null) {
            List<String> info = Arrays.asList(st.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)")); // Split on commas, but not within quotes
            int showNumber = Integer.parseInt(info.get(0));
            String dateString = info.get(1);

            List<String> dateInfo = Arrays.asList(dateString.split("-"));
            int year = Integer.parseInt(dateInfo.get(0));
            int month = Integer.parseInt(dateInfo.get(1));
            int day = Integer.parseInt(dateInfo.get(2));
            Calendar date = new GregorianCalendar(year, month-1, day);

            String rounds = info.get(2);
            String category = info.get(3);
            category = category.substring(1, category.length() - 1);
            int value = 0;

            // TODO: handle this better
            if (!(rounds.equals("Final Jeopardy!") || rounds.equals("Tiebreaker"))) { // final jeopardy & tiebreaker questions have no value
                value = Integer.parseInt(info.get(4).substring(2, info.get(4).length()-1).replaceAll(",", "")); // remove quotes, $, and commas
            }
            String question = info.get(5);
            question = question.substring(1, question.length() - 1);
            String answer = info.get(6);
            answer = answer.substring(1, answer.length() - 1);

            Round round;
            switch (rounds) {
            case "Double Jeopardy!": round = Round.DOUBLE_JEOPARDY;
            break;
            case "Jeopardy!":        round = Round.JEOPARDY;
            break;
            case "Final Jeopardy!":  round = Round.FINAL_JEOPARDY;
            break;
            case "Tiebreaker":       round = Round.TIE_BREAKER;
            break;
            default:                 round = Round.JEOPARDY;
            break;
            }

            if (gameRound.equals(round)) {
                questions.add(new Question(round, category, date, question, answer, value));
            }
        }
        br.close();
        return questions;
    }
}

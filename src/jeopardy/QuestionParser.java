package jeopardy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A parser that reads in csv files of Jeopardy questions of the form
 *          Show number, Air date, Round (Jeopardy, Double Jeopardy, etc.), Category (in quotes),
 *           Value ("$xxx"), Question (in quotes), Answer (in quotes)
 *
 */
public class QuestionParser {
    
    /**
     * Parses a csv file of jeopardy questions
     * @param file the csv file to be parsed
     * @return the list of Questions in the file
     * @throws IOException if the file could not be read
     */
    public static List<Question> parse(File file) throws IOException {
        List<Question> questions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        String st = br.readLine(); // don't care about first line
        
        while ((st = br.readLine()) != null) {
            List<String> info = Arrays.asList(st.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)")); // Split on commas, but not within quotes
            int showNumber = Integer.parseInt(info.get(0));
            String date = info.get(1);
            String rounds = info.get(2);
            String category = info.get(3);
            int value = 0;
            
            // TODO: handle this better
            if (!(rounds.equals("Final Jeopardy!") || rounds.equals("Tiebreaker"))) { // final jeopardy & tiebreaker questions have no value
                value = Integer.parseInt(info.get(4).substring(2, info.get(4).length()-1).replaceAll(",", "")); // remove quotes, $, and commas
            }
            String question = info.get(5);
            String answer = info.get(6);
            
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
            
            questions.add(new Question(round, category, question, answer, value));
        }
        
        System.out.println("Done!");
        System.out.println(questions.size());
        br.close();
        return questions;
    }
}

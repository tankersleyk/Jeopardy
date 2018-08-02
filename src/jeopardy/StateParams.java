package jeopardy;

/**
 * Used for storing parameters relevant to states
 *
 */
public class StateParams {

    public final Player PLAYER;
    public final Question QUESTION;
    public final int QUESTION_VALUE;

    /**
     * Create a new StateParams with only a player as a relevant parameter
     * @param player the player object to pass to the state
     */
    public StateParams(Player player) {
        PLAYER = player;
        QUESTION = null;
        QUESTION_VALUE = 0;
    }

    /**
     * Create a new StateParams with both the player and question as a relevant parameter
     * @param player the player object to pass to the state
     * @param question the question object to pass to the state
     */
    public StateParams(Player player, Question question) {
        PLAYER = player;
        QUESTION = question;
        QUESTION_VALUE = question.getPoints();
    }

    /**
     * Create a new StateParams with the player, question, and a custom value for the question
     * @param player the player object to pass to the state
     * @param question the question object to pass to the state
     * @param value the value of the question
     */
    public StateParams(Player player, Question question, int value) {
        PLAYER = player;
        QUESTION = question;
        QUESTION_VALUE = value;
    }

}

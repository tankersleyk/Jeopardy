package jeopardy;

/**
 * Used for storing parameters relevant to states
 *
 */
public class StateParams {

    public final Player PLAYER;
    public final Question QUESTION;

    /**
     * Create a new StateParams with only a player as a relevant parameter
     * @param player the player object to pass to the state
     */
    public StateParams(Player player) {
        PLAYER = player;
        QUESTION = null;
    }

    /**
     * Create a new StateParams with both the player and question as a relevant parameter
     * @param player the player object to pass to the state
     * @param question the question object to pass to the state
     */
    public StateParams(Player player, Question question) {
        PLAYER = player;
        QUESTION = question;
    }

}

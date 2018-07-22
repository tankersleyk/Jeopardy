package jeopardy;

/**
 * Used for storing parameters relevant to states
 *
 */
public class StateParams {

    public final Player PLAYER;

    /**
     * Create a new StateParams with only a player as a relevant parameter
     * @param player the player object to pass to the state
     */
    public StateParams(Player player) {
        PLAYER = player;
    }

}

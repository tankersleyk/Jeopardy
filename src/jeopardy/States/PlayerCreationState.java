package jeopardy.States;

import javax.swing.JTextField;

public class PlayerCreationState extends BaseState{

    private static PlayerCreationState instance = null;
    private static JTextField textfield = new JTextField(20);

    /**
     * Create a way for the player to enter a name
     */
    @SuppressWarnings("serial")
    private PlayerCreationState() {

    }

    /**
     * get the instance of this player creation
     * @return the singleton PlayerCreationState object
     */
    public static PlayerCreationState getInstance() {
        if (instance == null) {
            instance = new PlayerCreationState();
        }
        return instance;
    }
}

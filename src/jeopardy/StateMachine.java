package jeopardy;

import java.util.List;

import jeopardy.States.State;

/**
 * A FSM which manages the various states of the game and transitions between them.
 *
 */
public class StateMachine {

    private static StateMachine stateMachine = null; // StateMachine should be a singleton

    private final List<State> states;
    private State current;

    /**
     * Create a new state machine
     * @param states the states this machine can transition between
     */
    private StateMachine(List<State> states) {
        this.states = states;
        this.current = states.get(states.size() - 1);
    }

    /**
     * Get the instance of the machine, or create a new state machine if one does not already exist
     * @param states the states the machine can transition between
     * @return the singleton state machine
     */
    public static StateMachine getInstance(List<State> states) {

        if (stateMachine == null) {
            stateMachine = new StateMachine(states);
        }

        return stateMachine;
    }

    /**
     * Exits out of the current state and enters the state specified
     * @param state the state to change to - must be in the state machine
     * @param params the parameters to pass into the enter function
     */
    public void change(State state, StateParams params) {
        for (State s : states) {
            if (s.equals(state)) {
                this.current.exit();
                s.enter(params);
                this.current = s;
            }
        }
    }

    /**
     * Calls the update function on the current state
     * @param dt the time (in seconds) since the last frame
     */
    public void update(float dt) {
        current.update(dt);
    }
}

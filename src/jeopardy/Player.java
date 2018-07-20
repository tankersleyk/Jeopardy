package jeopardy;

public class Player {
    
    private final String name;
    private int money;
    
    public Player(String name, int money) {
        this.name = name;
        this.money = money;
    }
    
    /**
     * Get the name of this player
     * @return the name of this player
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the current value of this player's earnings
     * @return how much money this player currently has
     */
    public int getMoney() {
        return money;
    }
}
package jeopardy;

public class Player {

    private final String name;
    private int money = 0;

    public Player(String name) {
        this.name = name;
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

    /**
     * Add to this player's count
     * @param value the amount of money to add
     */
    public void addMoney(int value) {
        this.money += value;
    }

    /**
     * Subtract money from the player's count
     * @param value the amount of money to subtract
     */
    public void subtractMoney(int value) {
        this.money -= value;
    }
}

package jeopardy;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public abstract class GameButton extends JButton {

    /**
     * Create a new menu button
     * @param x the x-location of the left side of the button relative to the JPanel
     * @param y the y-location of the top side of the button relative to the JPanel
     * @param w the width of the button
     * @param h the height of the button
     * @param buttonColor the resting color of the button
     * @param textColor the color of the text
     * @param text the text on this button
     * @param fontSize the size of the text to draw in
     */
    public GameButton(int x, int y, int w, int h, Color buttonColor, Color textColor, String text, int fontSize) {
        this.setBackground(buttonColor);

        this.setBorder(null);
        this.setBounds(x, y, w, h);

        this.setText(text);
        this.setForeground(textColor);
        this.setFont(new Font("Arial", Font.PLAIN, fontSize));
        this.setFocusPainted(false);
    }

    /**
     * Performs the appropriate action that should occur when this menu button is clicked
     */
    public void isClicked() {

    }
}

import javax.swing.*;

public class CardFactory {
    public static JComponent create(Card c) {
        return new CardPanel(c);
    }

    public static JComponent create() {
        return new CardBackImage(new ImageIcon("Images/cardback1.png"));

    }
}

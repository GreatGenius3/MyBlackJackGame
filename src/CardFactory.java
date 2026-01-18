import javax.swing.*;

public class CardFactory {
    public static JComponent create(Card c) {
        if (c.faceDown) {
            return new CardBackImage(new ImageIcon("images/cardback1.png"));

        }
    }
}

import javax.swing.*;
import java.awt.*;

public class CardBackImage extends JLabel {
    public CardBackImage(ImageIcon image) {
        super(image);
        setPreferredSize(new Dimension(150, 220));
    }
}

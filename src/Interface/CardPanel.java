package Interface;

import javax.swing.*;
import java.awt.*;


public class CardPanel extends JPanel
{
    JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel center = new JPanel();
    JLabel topLabel;
    JLabel bottomLabel;
    JLabel bigIcon;

    Color color;


    public CardPanel(Card c)
    {
        if (c.getCardSuit().equals(Suit.HEARTS) || c.getCardSuit().equals(Suit.DIAMONDS))
        {
            color = Color.red;
        }
        else
        {
            color = Color.black;
        }

        setPreferredSize(new Dimension(150, 210));
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BorderLayout());

        bigIcon = new JLabel(c.getIcon());
        topLabel = new JLabel(c.getCardName() + c.getIcon());
        bottomLabel = new JLabel(c.getCardName() + c.getIcon());

        bigIcon.setForeground(color);
        topLabel.setForeground(color);
        bottomLabel.setForeground(color);

        bigIcon.setFont(new Font("Arial", Font.BOLD, 70));
        topLabel.setFont(new Font("Arial", Font.BOLD, 30));
        bottomLabel.setFont(new Font("Arial", Font.BOLD, 30));

        add(top, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        center.add(bigIcon);
        bottom.add(bottomLabel);
        top.add(topLabel);
    }
}
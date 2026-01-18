import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck
{
    private List<Card> deck = new ArrayList<>();

    public Deck()
    {
        for (int i = 1; i <= 4; i++)
        {
            for (int j = 1; j <= 13; j++)
            {
                deck.add(new Card(j, i));
            }
        }
        for (Card c : deck)
        {
            System.out.println(c.getCardName() + " of " + c.getCardSuit() + " | value: " + c.cardValue);
        }
    }

    public List<Card> getDeck()
    {
        return deck;
    }

    public boolean isEmpty()
    {
        return deck.isEmpty();
    }

    public Card remove(int index)
    {
        return deck.remove(index);
    }
}
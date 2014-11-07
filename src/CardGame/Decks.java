package CardGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 3.11.2014.
 */
public class Decks {
    List<List<Card>> decks = new ArrayList<List<Card>>();

    public void addDeck(List<Card> deck) { decks.add(deck); }
}

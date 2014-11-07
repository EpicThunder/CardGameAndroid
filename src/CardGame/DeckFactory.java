package CardGame;

import com.example.CardGameAndroid.DatabaseAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 3.11.2014.
 */
public class DeckFactory {

    static CardFactory2 cardFactory;

    public static List<Card> getDeck1(DatabaseAdapter databaseAdapter) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        cardFactory = new CardFactory2(databaseAdapter);
        List<Card> deck = new ArrayList<Card>();
        for(int i=0; i<5; i++) deck.add(cardFactory.getUnitCard("Guard"));
        for(int i=0; i<5; i++) deck.add(cardFactory.getUnitCard("Skeleton"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Guard dog"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Archer"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUsableCard("Heal"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUnitCard("Mage apprentice"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Steel jaw trap"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Slow"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Mercenary"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Battle eagle"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Wooden bunker"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Tax depository"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Scout tower"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Armor"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Antidote"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Ground"));
        deck.add(cardFactory.getUnitCard("Soldier"));
        deck.add(cardFactory.getStructureCard("Mana well"));
        deck.add(cardFactory.getStructureCard("Outpost"));
        deck.add(cardFactory.getUsableCard("Burn down"));
        deck.add(cardFactory.getUsableCard("Fire arrow"));
        //cardFactory.closeFactory();
        return deck;
    }

    public static List<Card> getDeck2(DatabaseAdapter databaseAdapter) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        cardFactory = new CardFactory2(databaseAdapter);
        List<Card> deck = new ArrayList<Card>();
        for(int i=0; i<5; i++) deck.add(cardFactory.getUnitCard("Mercenary"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUsableCard("Fire arrow"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Battle eagle"));
        for(int i=0; i<4; i++) deck.add(cardFactory.getUnitCard("Archer"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUnitCard("Guard dog"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Armor"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUsableCard("Burn down"));
        for(int i=0; i<3; i++) deck.add(cardFactory.getUnitCard("Horseman soldier"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Skeleton"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUsableCard("Heal"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Outpost"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Tax depository"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getStructureCard("Scout tower"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Guerrilla fighter"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Raider"));
        for(int i=0; i<2; i++) deck.add(cardFactory.getUnitCard("Large desert scorpion"));
        deck.add(cardFactory.getUsableCard("Ground"));
        deck.add(cardFactory.getUsableCard("Antidote"));
        deck.add(cardFactory.getUnitCard("Soldier"));
        deck.add(cardFactory.getStructureCard("Mana well"));
        deck.add(cardFactory.getUnitCard("Guard"));
        deck.add(cardFactory.getStructureCard("Wooden bunker"));
        deck.add(cardFactory.getUnitCard("Mage apprentice"));
        //cardFactory.closeFactory();
        return deck;
    }
}

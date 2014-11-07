package CardGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    private static int playerCount = 0;
    private int ID, gold, maxMana, mana, hCode;
    private List<Card> deck, onHand, unitDeck, spellDeck, structureAndItemDeck;
    private String notEqual = "";

    public Player(List<Card> _deck) {
        hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue();
        onHand = new ArrayList<Card>(); unitDeck  = new ArrayList<Card>(); spellDeck  = new ArrayList<Card>();
        structureAndItemDeck  = new ArrayList<Card>();
        deck = _deck; ID = playerCount; playerCount++;
        if (deck.size()<1) ;
        for(Card card:deck) {
            if(card == null){
                card.canPay();
            }
            card.setOwner(this);
        }
    }
    Player(Player player) throws InstantiationException, IllegalAccessException {
        hCode = player.hashCode();
        ID = player.getID(); gold = player.getGold(); maxMana = player.getMana(); mana = player.getMana();
        deck = Support.copyCards(player.deck);
        onHand = Support.copyCards(player.getCards()); unitDeck = Support.copyCards(player.getUnitDeck());
        spellDeck = Support.copyCards(player.getSpellDeck());
        structureAndItemDeck = Support.copyCards(player.getStructureAndItemDeck());
        for(Card card:deck) card.setOwner(this); for(Card card:onHand) card.setOwner(this);
        for(Card card:unitDeck) card.setOwner(this); for(Card card:spellDeck) card.setOwner(this);
        for(Card card:structureAndItemDeck) card.setOwner(this);
    }

    public void printNotEqual() { if(notEqual != "") System.out.println(notEqual); }

    public int hashCode() { return hCode; }

    public boolean equals(Object o) {
        notEqual = "";
        if(o == null || !o.getClass().getSimpleName().equals("Player")) return false;
        Player p=(Player)o;
        /*return (ID == p.ID && gold == p.gold && maxMana == p.maxMana && mana == p.mana &&
                deck.equals(p.deck) && onHand.equals(p.onHand) && unitDeck.equals(p.unitDeck) &&
                spellDeck.equals(p.spellDeck) && structureAndItemDeck.equals(p.structureAndItemDeck));*/
        if(!(deck.containsAll(p.deck) && p.deck.containsAll(deck))) { notEqual = "Deck not equal"; return false; }
        if(!(onHand.containsAll(p.onHand) && p.onHand.containsAll(onHand))) { notEqual = "OnHand not equal"; return false; }
        if(!(unitDeck.containsAll(p.unitDeck) && p.unitDeck.containsAll(unitDeck))) { notEqual = "UnitDeck not equal"; return false; }
        if(!(spellDeck.containsAll(p.spellDeck) && p.spellDeck.containsAll(spellDeck))) { notEqual = "SpellDeck not equal"; return false; }
        if(!(structureAndItemDeck.containsAll(p.structureAndItemDeck) && p.structureAndItemDeck.containsAll(structureAndItemDeck)))
        { notEqual = "SAndIDeck not equal"; return false; }
        if(!(ID == p.ID && gold == p.gold && mana == p.mana)) { notEqual = "Other not equal"; return false; };
        return true;
    }

    public void readyGame() throws IllegalAccessException, InstantiationException {
        gold = 0; maxMana = mana = 100;
        String type;
        onHand.clear(); unitDeck.clear(); spellDeck.clear(); structureAndItemDeck.clear();
        Collections.shuffle(deck);
        for(Card card:deck) {
            type = card.getCardCategory();
            if(type == "Unit") unitDeck.add(new UnitCard((UnitCard)card));
            else if(type == "Spell") spellDeck.add(new UsableCard((UsableCard)card));
            else if(type == "Item") structureAndItemDeck.add(new UsableCard((UsableCard)card));
            else structureAndItemDeck.add(new StructureCard((StructureCard)card));
        }
    }
    public int getID() { return ID; }
    public int getGold() { return gold; }
    public void setGold(int _gold) { gold = _gold; }
    public void changeGold(int changeGold) { gold += changeGold; }
    public int getMana() { return mana; }
    public void setMana(int _mana) { if(_mana > maxMana) mana = maxMana; else mana = _mana; }
    public void changeMana(int changeMana) { if(mana+changeMana > maxMana) mana = maxMana; else mana += changeMana; }
    public int averageDeckCost() {
        int average=0; for(Card card:deck) average += card.getCost(); return average/deck.size();
    }
    public void drew(Card card) { System.out.println("You drew: " + card.getName()+", cost:"+card.getCost()+", category:"+card.getCardCategory()); }
    public boolean drawUnit() {
        if(unitDeck.size() > 0) {
            onHand.add(unitDeck.get(0)); drew(unitDeck.get(0)); unitDeck.remove(0); return true;
        }
        return false;
    }
    public boolean drawSpell() {
        if(spellDeck.size() > 0) {
            onHand.add(spellDeck.get(0)); drew(spellDeck.get(0)); spellDeck.remove(0); return true;
        }
        return false;
    }
    public boolean drawStructureOrItem() {
        if(structureAndItemDeck.size() > 0) {
            onHand.add(structureAndItemDeck.get(0)); drew(structureAndItemDeck.get(0));
            structureAndItemDeck.remove(0); return true;
        }
        return false;
    }

    public List<Card> getCards() { return onHand; }
    public List<Card> getUnitDeck() { return unitDeck; }
    public List<Card> getSpellDeck() { return spellDeck; }
    public List<Card> getStructureAndItemDeck() { return structureAndItemDeck; }

    public boolean hasCard(String cardName) {
        for(Card card:onHand) if(card.getName().equals(cardName)) return true;
        return false;
    }
    public Card takeCard(String cardName) {
        for(Card card:onHand) if(card.getName().equals(cardName)) { onHand.remove(card); return card; }
        return null;
    }
    public Card getCard(String cardName) {
        for(Card card:onHand) if(card.getName().equals(cardName)) return card;
        return null;
    }
    public Card getCard(int id) {
        for(Card card:onHand) if(card.getID() == id) return card;
        return null;
    }
    public boolean removeCard(Card aCard) {
        for(Card card:onHand) if(card.equals(aCard)) { onHand.remove(card); return true; }
        return false;
    }
    public int numberOfCardsOfGivenCategoryOnHand(String category) {
        int count=0; for(Card card:onHand) if(card.getCardCategory().equals(category)) count++;
        return count;
    }
    public void showCards() { for(Card card:onHand) Support.cardInfo(card); }
    public boolean canPay(Card card) {
        if(card.getCardCategory().equals("Spell") && card.getCost() <= mana) return true;
        else if(!card.getCardCategory().equals("Spell") && card.getCost() <= gold) return true;
        return false;
    }
    public void payCost(Card card) {
        if(card.getCardCategory().equals("Spell")) changeMana(-card.getCost()); else changeGold(-card.getCost());
        removeCard(card);
    }
}

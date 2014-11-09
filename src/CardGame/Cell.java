package CardGame;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class Cell {
    private int rowPos, colPos, hCode;
    private TerrainType terrain;
    private int ownedBy;
    private Set<Integer> influencedBy = new HashSet<Integer>();
    private List<Card> cards = new ArrayList<Card>();

    Cell(TerrainType terrainType, int _rowPos, int _colPos) {
        hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue();
        terrain = terrainType; rowPos = _rowPos; colPos = _colPos; ownedBy = -1;
    }
    Cell(Cell cell) throws InstantiationException, IllegalAccessException {
        hCode = cell.hashCode();
        rowPos = cell.getRowPos(); colPos = cell.getColPos(); terrain = new TerrainType(cell.getTerrain());
        ownedBy = cell.getOwnedBy();
        cards = Support.copyCards(cell.getCards());
        for(Card card:cards) card.setCurrentLocation(this);
    }

    public int hashCode() { return hCode; }

    public boolean equals(Cell cell) {
        if(cell.getRowPos() != rowPos || cell.getColPos() != colPos) return false;
        if(!cell.getCards().containsAll(cards) || !cards.containsAll(cell.getCards())) return false;
        return true;
    }

    public int getRowPos() { return rowPos; }
    public int getColPos() { return colPos; }
    public TerrainType getTerrain() { return terrain; }
    public Integer getOwnedBy() { return ownedBy; }
    public void setCorrectOwnerOfCards(List<Player> oldPlayers, List<Player> newPlayers) {
        for(Card card:cards) for(Player player:oldPlayers) if(card.getOwner().equals(player))
            for(Player newPlayer:newPlayers) if(player.getID() == newPlayer.getID()) card.setOwner(newPlayer);
    }

    public boolean hasCardAtCell(Player player) {
        for(Card card:cards) if(card.getOwner().getID() == player.getID() && card.getName() != "Base") return true;
        return false;
    }
    public boolean hasCard(int id) { for(Card card:cards) if(card.getID() == id) return true; return false; }
    public Card getCard(String name) {
        for(Card card:cards) if(card.getName().equals(name)) return card;
        return null;
    }
    public Card getCard(int ID) {
        for(Card card:cards) if(card.getID() == ID) return card;
        return null;
    }
    public Card getCard(int ID, Player player) {
        for(Card card:cards) if(card.getOwner().equals(player) && card.getID() == ID) return card;
        return null;
    }
    public List<Card> getCards() { return cards; }
    public List<Card> getCards(Player player) {
        List<Card> playersCards = new ArrayList<Card>();
        for(Card card:cards) {
            if(card.getOwner() == null) { System.out.println(); continue; }
            if(card.getOwner().equals(player)) playersCards.add(card);
        }
        return playersCards;
    }
    public void removeCard(Card card) { cards.remove(card); }
    public void resetCards() {
        List<Card> newList = new ArrayList<Card>();
        for(Card card:cards) if(card.getName().equals("Base")) newList.add(card);
        cards = newList;
    }
    public void placeMovedCard(Card card) { card.setCurrentLocation(this); cards.add(card); }
    public boolean placeCard(Card card) {
        if(card != null && ((card.getOwner() != null && ownedBy == card.getOwner().getID()) ||
           (card.getOwner() != null && influencedBy.contains(card.getOwner())) || card.getName().equals("Base")))
        {
            card.setCurrentLocation(this);
            cards.add(card);
            card.payCost();
            return true;
        }
        return false;
    }

    public void setOwner(Player player) { ownedBy = player.getID(); }
    public void setInfluence(Player player) { influencedBy.add(player.getID()); }
    public void setInfluence() { for(Card card:cards) if(!card.hasMoved()) influencedBy.add(card.getOwner().getID()); }
    public void removeInfluence() { influencedBy.clear(); }
    public boolean hasInfluenceOver(Player player) {
        return (ownedBy == player.getID() || influencedBy.contains(player.getID()));
    }
    public void activateAbilityInfluence() {
        for(Card card:cards) {
            List<Action> actions = card.getActions();
            for(Action action:actions) if(action.getClass().toString().equals("Outpost"))
                action.activateAction(card);
        }
    }
    public Set<Integer> getOpponentsInfluence(Player player) {
        Set<Integer> players = new HashSet<Integer>(influencedBy);
        players.remove(player.getID());
        return players;
    }

    public void removeDead() {
        for(Card card:new ArrayList<Card>(cards))
            if(Support.isBattleCard(card) && ((CardThatCanBattle)card).getHealth() == 0) cards.remove(card);
    }

    public void newTurn() {
        for(Card card:cards) card.newTurn();
    }
    public void endTurn() {
        for(Card card:cards) card.endTurn();
    }
}

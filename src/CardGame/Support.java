package CardGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Kristján on 17.4.2014.
 */
public class Support {

    static private Scanner keyboard = new Scanner(System.in);
    static private State state = null;
    static private Immunities immunities = null;

    public static void setState(State _state) { state = _state; }
    public static State getState() { return state; }

    public static String getInput() { return keyboard.nextLine(); }
    public static int getHP(Card card) { return ((CardThatCanBattle)card).getHealth(); }

    public static boolean isBattleCard(Card card) {
        return (card.getClass().getSuperclass().getName().equals("CardThatCanBattle"));
    }
    public static int numberOfCardsInAGivenCategory(String category, List<Card> cards) {
        int count=0; for(Card card:cards) if(card.getCardCategory().equals(category)) count++;
        return count;
    }
    public static boolean withinTheBoard(int row, int col) {
        return !(row < 0 || col < 0 || row > state.boardRowSize() || col > state.boardColSize());
    }
    public static int getManhattanDistance(int row1, int col1, int row2, int col2) {
        int distance, temp;
        distance = row1-row2; if(distance<0) distance = -1*distance;
        temp = col1-col2; if(temp<0) temp = -1*temp; distance += temp;
        return distance;
    }
    public static int getManhattanDistance(Cell cell1, Cell cell2) {
        return getManhattanDistance(cell1.getRowPos(), cell1.getColPos(), cell2.getRowPos(), cell2.getColPos());
    }
    public static boolean allAreOnBoard(List<Card> cards) {
        return (cards.get(0).isOnBoard() && cards.get(1).isOnBoard());
    }
    public static List<String> terrainsUsedForBattle(CardThatCanBattle attackerCard, CardThatCanBattle targetCard) {
        String targetTerrain = targetCard.getPreferredPlacement(), attackerTerrain = targetTerrain;
        if(attackerCard.getAttackRange() > 0) attackerTerrain = attackerCard.getPreferredPlacement();
        List<String> list = new ArrayList<String>(); list.add(attackerTerrain); list.add(targetTerrain);
        return list;
    }

    public static boolean isImmune(String debuff, List<String> unitType) {
        if(immunities == null) immunities = new Immunities();
        return immunities.isImmune(debuff, unitType);
    }

    // Make copy
    public static List<List<Cell>> copyBoard(List<List<Cell>> board) throws IllegalAccessException, InstantiationException {
        List<List<Cell>> newBoard = new ArrayList<List<Cell>>();
        for(List<Cell> row:board) {
            List<Cell> newRow = new ArrayList<Cell>();
            for(Cell cell:row) newRow.add(new Cell(cell));
            newBoard.add(newRow);
        }
        return newBoard;
    }
    public static List<Player> copyPlayers(List<Player> players) throws IllegalAccessException, InstantiationException {
        List<Player> newPlayers = new ArrayList<Player>();
        for(Player player:players) newPlayers.add(new Player(player));
        return newPlayers;
    }
    public static List<Card> copyCards(List<Card> cards) throws IllegalAccessException, InstantiationException {
        List<Card> newCards = new ArrayList<Card>();
        for(int i=0; i<cards.size(); i++) {
            Card card = cards.get(i);
            Card newCard;
            if(card.getCardCategory().equals("Base")) newCard = new Base((Base)card);
            else if(card.getCardCategory().equals("Unit")) newCard = new UnitCard((UnitCard)card);
            else if(card.getCardCategory().equals("Structure")) newCard = new StructureCard((StructureCard)card);
            else newCard = new UsableCard((UsableCard)card);
            newCards.add(i, newCard);
        }
        return newCards;
    }
    public static List<String> copyStrings(List<String> strings) {
        List<String> newStrings = new ArrayList<String>();
        for(String string:strings) newStrings.add(string);
        return newStrings;
    }
    public static List<Damage> copyDamages(List<Damage> damages) {
        List<Damage> newDamages = new ArrayList<Damage>();
        for(Damage damage:damages) newDamages.add(new Damage(damage));
        return newDamages;
    }
    public static List<Defence> copyDefences(List<Defence> defences) {
        List<Defence> newDefences = new ArrayList<Defence>();
        for(Defence defence:defences) newDefences.add(new Defence(defence));
        return newDefences;
    }
    public static List<DamageReduction> copyDamageReductions(List<DamageReduction> damageReductions) {
        List<DamageReduction> newDamageReductions = new ArrayList<DamageReduction>();
        for(DamageReduction damageReduction:damageReductions)
            newDamageReductions.add(new DamageReduction(damageReduction));
        return newDamageReductions;
    }
    public static List<Action> copyActions(List<Action> actions) throws IllegalAccessException, InstantiationException {
        List<Action> newActions = new ArrayList<Action>();
        for(Action action:actions) newActions.add(action.getClass().newInstance());
        return newActions;
    }
    public static List<Effect> copyEffects(List<Effect> effects) throws InstantiationException, IllegalAccessException {
        List<Effect> newEffects = new ArrayList<Effect>();
        for(Effect effect:effects) newEffects.add(effect.returnCopy());
        return newEffects;
    }

    // Add cards
    public static void addPlayersUnitCardsFromCell(Cell cell, Player player, List<Card> unitCards) {
        for(Card target:cell.getCards(player))
            if(target.getCardCategory().equals("Unit")) unitCards.add(target);
    }

    // Info on cards
    public static void cardInfo(Card card) { System.out.println("Name:"+card.getName()+". ID:"+card.getID()+";"); }
}

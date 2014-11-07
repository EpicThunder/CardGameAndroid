package CardGame;

import java.io.IOException;
import java.util.*;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    static CardFactory cardFactory;
    public static void main(String[] argv) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {

        List<List<Card>> decks = new ArrayList<List<Card>>();
        cardFactory = new CardFactory();
        decks.add(getDeck1()); decks.add(getDeck2());
        cardFactory.closeFactory();

        /*Map<String,Integer> terrain = new HashMap<String, Integer>(); terrain.put("Desert",100);
        TerrainType terrainType = new TerrainType(terrain); List<TerrainType> terrainRow = new ArrayList<TerrainType>();
        List<List<TerrainType>> terrainMap = new ArrayList<List<TerrainType>>();
        for(int i=0; i<20; i++) terrainRow.add(terrainType);
        for(int i=0; i<20; i++) { terrainMap.add(terrainRow); }

        List<List<Cell>> board = new ArrayList<List<Cell>>();
        List<Cell> cellRow = new ArrayList<Cell>();
        for (int row=0, rowSize=terrainMap.size(); row<rowSize; row++) {
            for (int col=0, colSize=terrainMap.size(); col<colSize; col++) {
                cellRow.add(new Cell(terrainMap.get(row).get(col), row, col));
            }
            board.add(new ArrayList<Cell>(cellRow));
            cellRow.clear();
        }

        List<Player> playerList = new ArrayList<Player>(); playerList.add(new Player(decks.get(0)));

        State state = new State(new Grid(board), playerList, new ArrayList<Boolean>(), new ArrayList<Card>(), 0, 0, true, true, 3);
        Set<State> stateSet = new HashSet<State>(); stateSet.add(state); System.out.println(stateSet.size());
        stateSet.add(state.deepCopy()); System.out.println(stateSet.size());*/

        //System.out.println(decks.get(0).get(0).getClass().getName());
        Board gameBoard = new Board(decks);

        // Create a simple desert map
        Map<String,Integer> terrain = new HashMap<String, Integer>(); terrain.put("Desert",100);
        TerrainType terrainType = new TerrainType(terrain); List<TerrainType> terrainRow = new ArrayList<TerrainType>();
        List<List<TerrainType>> terrainMap = new ArrayList<List<TerrainType>>();
        for(int i=0; i<6; i++) terrainRow.add(terrainType);
        for(int i=0; i<6; i++) { terrainMap.add(terrainRow); }
        List<Integer> baseRow = new ArrayList<Integer>(), baseCol = new ArrayList<Integer>();
        baseRow.add(1); baseRow.add(4); baseCol.add(1); baseCol.add(4);
        gameBoard.setUpBoard(terrainMap, baseRow, baseCol);

        //gameBoard.newGame();
        int winner, player0Wins=0, player1Wins=0; boolean x=true;
        for(int i=0; i<10; i++) {
            winner = gameBoard.play(2);
            if((x && winner == 0) || (!x && winner == 1)) player0Wins++;
            else if((!x && winner == 0) || (x && winner == 1)) player1Wins++;
            x = !x;
        }
        System.out.println("Number of wins. Player0: "+player0Wins+", Player1:"+player1Wins);
    }

    public static List<Card> getDeck1() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
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
        return deck;
    }

    public static List<Card> getDeck2() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
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
        return deck;
    }
}

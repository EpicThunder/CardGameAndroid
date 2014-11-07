package CardGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kristján on 6.11.2014.
 */
public class TerrainFactory {

    static public List<List<Cell>> getDesertTerrain() {
        Map<String,Integer> terrain = new HashMap<String, Integer>(); terrain.put("Desert",100);
        TerrainType terrainType = new TerrainType(terrain);
        List<List<Cell>> board = new ArrayList<List<Cell>>();
        List<Cell> cellRow = new ArrayList<Cell>();
        for (int row=0; row<6; row++) {
            for (int col=0; col<6; col++) {
                cellRow.add(new Cell(terrainType, row, col));
            }
            board.add(new ArrayList<Cell>(cellRow));
            cellRow.clear();
        }
        return board;
    }
}
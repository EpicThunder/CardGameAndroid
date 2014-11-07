package CardGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 16.6.2014.
 */
public class Grid {
    private List<List<Cell>> board = new ArrayList<List<Cell>>();
    private int hCode;
    Grid(List<List<Cell>> _board) { board = _board; hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue(); }
    Grid(Grid grid) throws IllegalAccessException, InstantiationException {
        hCode = grid.hashCode();
        for(int row=0; row<grid.rowSize(); row++) {
            List<Cell> aRow = new ArrayList<Cell>();
            for(int col=0; col<grid.colSize(); col++) aRow.add(new Cell(grid.board.get(row).get(col)));
            board.add(aRow);
        }
    }

    public int hashCode() { return hCode; }

    public boolean equals(Grid grid) {
        for(int row=0; row<grid.rowSize(); row++) {
            for(int col=0; col<grid.colSize(); col++) if(!grid.board.get(row).get(col).equals(board.get(row).get(col))) return false;
        }
        return true;
    }

    public int rowSize() { return board.size(); }
    public int colSize() { return board.get(0).size(); }
    public Cell getCell(int row, int col) { return board.get(row).get(col); }
    public List<Card> getCards(int row, int col, Player player) { return board.get(row).get(col).getCards(player); }
    public void setUpInfluence() {
        for(List<Cell> row:board)
            for(Cell cell:row) cell.removeInfluence();
        for(List<Cell> row:board)
            for(Cell cell:row) { cell.activateAbilityInfluence(); cell.setInfluence(); }
    }
    public List<Cell> getInfluencedCells(Player player) {
        List<Cell> cells = new ArrayList<Cell>();
        for(List<Cell> row:board)
            for(Cell cell:row) if(cell.hasInfluenceOver(player)) cells.add(cell);
        return cells;
    }
    public void newTurn() {
        for(List<Cell> row:board) {
            for(Cell cell:row) {
                cell.newTurn();
            }
        }
    }
    public void endTurn() {
        for(List<Cell> row:board) {
            for(Cell cell:row) {
                cell.endTurn();
            }
        }
    }
    public void checkCardDeath() {
        for(List<Cell> row:board) for(Cell cell:row) cell.removeDead();
    }
}

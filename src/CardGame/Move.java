package CardGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 14.3.2014
 * Time: 23:28
 * Code specifying movement of unit cards
 */
public interface Move {
    List<String> getCanStandOn();
    boolean legalMove(int cardRowPos, int cardColPos,
                      int newRowPos, int newColPos, int moveRange, Player currPlayer);
}

abstract class AbstractMove {

    List<String> canStandOn = new ArrayList<String>();

    public List<String> getCanStandOn() { return canStandOn; }

    abstract boolean lookForWithinDistance();

    abstract int getMaxPathLength(int moveRange);

    abstract boolean isThereAPath(List<List<Cell>> paths, int newRowPos, int newColPos, int moveRange, Player currPlayer);

    boolean isWithinDistance(int cardRowPos, int cardColPos, int newRowPos, int newColPos, int moveRange) {
        int rowDist = cardRowPos-newRowPos, colDist = cardColPos-newColPos;
        if(colDist<0) colDist = -1*colDist; if(rowDist<0) rowDist = -1*rowDist;
        return !(colDist+rowDist > moveRange);
    }

    public boolean legalMove(int cardRowPos, int cardColPos,
                             int newRowPos, int newColPos, int moveRange, Player currPlayer)
    {
        if(lookForWithinDistance() && !isWithinDistance(cardRowPos, cardColPos, newRowPos, newColPos, moveRange))
            return false;
        List<List<Cell>> paths = new ArrayList<List<Cell>>(), pathsTemp = new ArrayList<List<Cell>>();
        List<Cell> path = new ArrayList<Cell>(); path.add(Support.getState().getCell(cardRowPos, cardColPos));
        paths.add(path);
        for(int i = 0; i<getMaxPathLength(moveRange); i++) {
            for(List<Cell> aPath:paths) {
                if(Support.getState().boardRowSize()-1 > aPath.get(aPath.size()-1).getRowPos()) {
                    path = new ArrayList<Cell>(aPath);
                    path.add(Support.getState().getCell(aPath.get(aPath.size()-1).getRowPos() + 1, aPath.get(aPath.size()-1).getColPos()));
                    pathsTemp.add(path);
                }
                if(0 != aPath.get(aPath.size()-1).getRowPos()) {
                    path = new ArrayList<Cell>(aPath);
                    path.add(Support.getState().getCell(aPath.get(aPath.size()-1).getRowPos() - 1, aPath.get(aPath.size()-1).getColPos()));
                    pathsTemp.add(path);
                }
                if(Support.getState().boardColSize()-1 > aPath.get(aPath.size()-1).getColPos()) {
                    path = new ArrayList<Cell>(aPath);
                    path.add(Support.getState().getCell(aPath.get(aPath.size()-1).getRowPos(), aPath.get(aPath.size()-1).getColPos()+1));
                    pathsTemp.add(path);
                }
                if(0 != aPath.get(aPath.size()-1).getColPos()) {
                    path = new ArrayList<Cell>(aPath);
                    path.add(Support.getState().getCell(aPath.get(aPath.size()-1).getRowPos(), aPath.get(aPath.size()-1).getColPos()-1));
                    pathsTemp.add(path);
                }
            }
            paths = pathsTemp; pathsTemp = new ArrayList<List<Cell>>();
            if(isThereAPath(paths, newRowPos, newColPos, moveRange, currPlayer)) return true;
        }
        return false;
    }
}

class StandardMove extends AbstractMove implements Move {

    StandardMove() { canStandOn.add("Grass"); canStandOn.add("Desert"); canStandOn.add("Town"); }

    @Override
    boolean lookForWithinDistance() { return true; }

    @Override
    int getMaxPathLength(int moveRange) { return moveRange; }

    @Override
    boolean isThereAPath(List<List<Cell>> paths, int newRowPos, int newColPos, int moveRange, Player currPlayer) {
        Cell cell;
        boolean canTraverse, canTraverseAllTheWay;
        for(List<Cell> aPath:paths) {
            cell = aPath.get(aPath.size()-1);
            if(cell.getRowPos() == newRowPos && cell.getColPos() == newColPos) {
                canTraverseAllTheWay = true;
                for(Cell aCell:aPath) {
                    canTraverse = false;
                    for(String aTerrain:aCell.getTerrain().getTerrains()) {
                        if(canStandOn.contains(aTerrain)) canTraverse = true;
                    }
                    if (!canTraverse) { canTraverseAllTheWay = false; break; }
                }
                if(canTraverseAllTheWay) return true;
            }
        }
        return false;
    }
}

class MoveOnlyInWater extends AbstractMove implements Move {

    MoveOnlyInWater() { canStandOn.add("Water"); }

    @Override
    boolean lookForWithinDistance() { return true; }

    @Override
    int getMaxPathLength(int moveRange) { return moveRange; }

    @Override
    boolean isThereAPath(List<List<Cell>> paths, int newRowPos, int newColPos, int moveRange, Player currPlayer) {
        Cell cell;
        boolean canTraverse, canTraverseAllTheWay;
        for(List<Cell> aPath:paths) {
            cell = aPath.get(aPath.size()-1);
            if(cell.getRowPos() == newRowPos && cell.getColPos() == newColPos) {
                canTraverseAllTheWay = true;
                for(Cell aCell:aPath) {
                    canTraverse = false;
                    for(String aTerrain:aCell.getTerrain().getTerrains()) {
                        if(canStandOn.contains(aTerrain)) canTraverse = true;
                    }
                    if (!canTraverse) { canTraverseAllTheWay = false; break; }
                }
                if(canTraverseAllTheWay) return true;
            }
        }
        return false;
    }
}

class MoveOverAll extends AbstractMove implements Move {

    MoveOverAll() { canStandOn.add("Grass"); canStandOn.add("Desert"); canStandOn.add("Town"); canStandOn.add("Water"); }

    @Override
    boolean lookForWithinDistance() { return true; }

    @Override
    int getMaxPathLength(int moveRange) { return moveRange; }

    @Override
    boolean isThereAPath(List<List<Cell>> paths, int newRowPos, int newColPos, int moveRange, Player currPlayer) {
        return true;
    }
}

class TerritoryTraverseMove extends AbstractMove implements Move {

    TerritoryTraverseMove() { canStandOn.add("Grass"); canStandOn.add("Desert"); canStandOn.add("Town"); }

    @Override
    boolean lookForWithinDistance() {
        return false;
    }

    @Override
    int getMaxPathLength(int moveRange) {
        return ++moveRange;
    }

    @Override
    boolean isThereAPath(List<List<Cell>> paths, int newRowPos, int newColPos, int moveRange, Player currPlayer) {
        Cell cell;
        boolean canTraverse, canTraverseAllTheWay, thereIsEnemyTerritory, thereIsNonEnemyTerritory;
        for(List<Cell> aPath:paths) {
            cell = aPath.get(aPath.size()-1);
            if(cell.getRowPos() == newRowPos && cell.getColPos() == newColPos) {
                canTraverseAllTheWay = true;
                thereIsEnemyTerritory = thereIsNonEnemyTerritory = false;
                for(Cell aCell:aPath) {
                    if(aCell.getOpponentsInfluence(currPlayer).size() == 0) thereIsNonEnemyTerritory = true;
                    else thereIsEnemyTerritory = true;
                    canTraverse = false;
                    for(String aTerrain:aCell.getTerrain().getTerrains()) {
                        if(canStandOn.contains(aTerrain)) canTraverse = true;
                    }
                    if (!canTraverse) { canTraverseAllTheWay = false; break; }
                }
                if(canTraverseAllTheWay &&
                        (aPath.size() != moveRange+2 || (thereIsEnemyTerritory && thereIsNonEnemyTerritory))) {
                    return true;
                }
            }
        }
        return false;
    }
}

class CanNotMove implements Move {

    public List<String> getCanStandOn() { return new ArrayList<String>(); }

    public boolean legalMove(int cardRowPos, int cardColPos,
                             int newRowPos, int newColPos, int moveRange, Player currPlayer)
    {
        return false;
    }
}

package CardGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 16.6.2014.
 */
public class State {
    private Grid board;
    private List<Player> players = new ArrayList<Player>();
    private List<Boolean> playersLost = new ArrayList<Boolean>();
    private List<Card> activeCards = new ArrayList<Card>();
    private int playersSize = 0, playersTurn = 0, hCode;
    private boolean gameWon, firstRound;
    private String notEqual = "";

    public State() { hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue(); }
    State(Grid _board, List<Player> _players, List<Boolean> _playersLost, List<Card> _activeCards, int _playersSize,
          int _playersTurn, boolean _gameWon, boolean _firstRound, int hashCode) {
        hCode = hashCode;
        board = _board; players = _players; playersLost = _playersLost; activeCards =_activeCards;
        playersSize = _playersSize; playersTurn = _playersTurn; gameWon = _gameWon; firstRound = _firstRound;
    }
    State(Grid _board, List<Player> _players, List<Boolean> _playersLost, List<Card> _activeCards, int _playersSize,
          int _playersTurn, boolean _gameWon, boolean _firstRound) {
        hCode = new Double(Integer.MAX_VALUE * Math.random()).intValue();
        board = _board; players = _players; playersLost = _playersLost; activeCards =_activeCards;
        playersSize = _playersSize; playersTurn = _playersTurn; gameWon = _gameWon; firstRound = _firstRound;
    }
    State(State state) throws InstantiationException, IllegalAccessException {
        board = new Grid(state.board);
        players = Support.copyPlayers(state.getPlayers());
        for(int row = 0; row<board.rowSize(); row++) for(int col = 0; col<board.colSize(); col++)
            board.getCell(row, col).setCorrectOwnerOfCards(state.getPlayers(), players);
        List<Boolean> newPlayersLost = new ArrayList<Boolean>();
        for(Boolean bool:state.playersLost) newPlayersLost.add(bool);
        playersLost = newPlayersLost;
        activeCards = Support.copyCards(state.getActiveCards());
        playersSize = state.getPlayersSize(); playersTurn = state.getPlayersTurn(); hCode = state.hashCode();
        gameWon = state.isGameWon(); firstRound = state.firstRound;
    }

    public void printNotEqual() { if(notEqual == "") return; System.out.println(notEqual); for(Player player:players) player.printNotEqual(); }

    public int hashCode() { return hCode; }

    public boolean equals(Object o) {
        notEqual = "";
        if(o == null || !o.getClass().getSimpleName().equals("State")) return false;
        State s = (State)o;
        /*return (board.equals(s.board) && players.equals(s.players) && playersLost.equals(s.playersLost) &&
                activeCards.equals(s.activeCards) && playersSize == s.playersSize && playersTurn == s.playersTurn &&
                gameWon == s.gameWon && firstRound == s.firstRound);*/
        if(!board.equals(s.board)) { notEqual = "Grid not equal"; return false; }
        if(!(players.containsAll(s.players) && s.players.containsAll(players))) { notEqual = "Players not equal"; return false; }
        if(!playersLost.equals(s.playersLost)) { notEqual = "Win not equal"; return false; }
        if(!activeCards.equals(s.activeCards)) { notEqual = "Active cards not equal"; return false; }
        if(!(playersSize == s.playersSize && playersTurn == s.playersTurn &&
             gameWon == s.gameWon && firstRound == s.firstRound)) { notEqual = "Simple not equal"; return false; }
        return true;
    }

    public void addPlayer(Player player) { players.add(player); playersLost.add(false); playersSize = players.size(); }
    public void setBoard(List<List<Cell>> _board) { board = new Grid(_board); }
    public boolean addCardToBoard(Card card, int row, int col) { return board.getCell(row, col).placeCard(card); }
    public void newPlacementOfCard(Card card, int row, int col) { board.getCell(row, col).placeMovedCard(card); }
    public int boardRowSize() { return board.rowSize(); }
    public int boardColSize() { return board.colSize(); }
    public Cell getCell(int row, int col) { return board.getCell(row, col); }
    public void newInitialState() throws InstantiationException, IllegalAccessException {
        for(int row=0; row<boardRowSize(); row++) for(int col=0; col<boardColSize(); col++)
            getCell(row,col).resetCards();
        for(Player player:players) player.readyGame();
        for(int i=0; i<playersLost.size(); i++ ) playersLost.set(i, false);
        playersTurn = 0; gameWon = false;
    }
    public boolean isGameWon() { return gameWon; }
    public void setFirstRound(boolean bool) { firstRound = bool; }
    public boolean isFirstRound() { return firstRound; }
    public int getPlayersTurn() { return playersTurn; }
    public int getPlayersSize() { return playersSize; }
    public void resourcesAvailable() {
        System.out.println("Your gold is " + players.get(playersTurn).getGold() +
                           " and your mana is " + players.get(playersTurn).getMana());
    }
    public void cardsOnHand() { players.get(playersTurn).showCards(); }
    public Player getCurrentPlayer() { return players.get(playersTurn); }
    public List<Player> getOpponents() {
        return getOpponentsOf(players.get(playersTurn));
    }
    public List<Player> getOpponentsOf(Player player) {
        List<Player> opponents = new ArrayList<Player>(players); opponents.remove(player); return opponents;
    }
    public List<Card> getActiveCards() { return activeCards; }
    public void removeActiveCard(Card card) { activeCards.remove(card); }
    public Card findCard(int id) {
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col<board.colSize(); col++)
            if(board.getCell(row, col).hasCard(id)) return board.getCell(row, col).getCard(id);
        return null;
    }
    public Base findBase(Player player) {
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col< board.colSize(); col++) {
            List<Card> cards = board.getCell(row, col).getCards(player);
            for(Card card:cards) if(card.getName().equals("Base")) return (Base)card;
        }
        return null;
    }
    public List<Card> getPlayersCardsFromTheBoard(Player player) {
        List<Card> playersCards = new ArrayList<Card>();
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col< board.colSize(); col++) {
            playersCards.addAll(board.getCell(row, col).getCards(player));
        }
        return playersCards;
    }

    public void setUpBoardInfluence() { board.setUpInfluence(); }
    public List<Cell> getInfluencedCells(Player player) { return board.getInfluencedCells(player); }
    public void setOwner(int row, int col, Player player) { board.getCell(row, col).setOwner(player); }
    public Player getPlayer(int id) { return players.get(id); }
    public List<Player> getPlayers() { return players; }
    public void newTurn() {
        players.get(playersTurn).changeGold(200); players.get(playersTurn).changeMana(10);
        board.newTurn();
    }
    public void endTurn() {
        board.endTurn();

        do {
            if (playersTurn == players.size() - 1) { firstRound = false; playersTurn = 0; } else playersTurn++;
        } while(playersLost.get(playersTurn));
    }
    public void checkLoss() {
        Card card = null;
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col<board.colSize(); col++) {
            card = board.getCell(row, col).getCard("Base");
            if(card != null) for(Player opponent:getOpponentsOf(card.getOwner()))
                if(board.getCards(row, col, opponent).size() > 0 && board.getCards(row, col, card.getOwner()).size() == 1)
                    playersLost.set(card.getOwner().getID(), true);
        } int countFalse=0;
        for(Boolean bool: playersLost) { if(!bool) countFalse++; if(countFalse>1) return; }
        gameWon=true;
    }

    public void checkCardDeath() {
        board.checkCardDeath();
    }

    // print info

    public void allCardsOnBoardAndLocation() {
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col<board.colSize(); col++)
            for(Card card:board.getCell(row, col).getCards())
                System.out.print("(Player"+card.getOwner().getID()+",ID:"+card.getID()+",Name:"+card.getName()+",R:"+row+",C:"+col+".) ");
        System.out.println();
    }

    public void winner() {
        for(int i=0; i<players.size(); i++) if(playersLost.get(i) == false) System.out.println("Player"+i+" won");
    }

    public int getWinner() { for(int i=0; i<players.size(); i++) if(playersLost.get(i) == false) return i; return -1; }

    public void allCardsInPlay() {
        allCardsForPlayer(getCurrentPlayer());
        for(Player opponent:getOpponents()) allCardsForPlayer(opponent);
    }

    public void allCardsForPlayer(Player player) {
        System.out.println("All the cards for player" + player.getID() + "\n -On board:");
        cardsFromOnBoardInfo(player);
        System.out.println(" -Cards in play:");
        cardsInPlayInfo(player);
    }

    public void cardsFromOnBoardInfo() { cardsFromOnBoardInfo(players.get(playersTurn)); }
    public void cardsFromOnBoardInfo(Player player) {
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col<board.colSize(); col++)
            for(Card card:board.getCards(row, col, player))
                cardFromOnBoardInfo(card, card.getCurrentLocation().getRowPos(), card.getCurrentLocation().getColPos());
    }
    public void battleCardsFromOnBoardInfo() { battleCardsFromOnBoardInfo(players.get(playersTurn)); }
    public void battleCardsFromOnBoardInfo(Player player) {
        for(int row=0; row<board.rowSize(); row++) for(int col=0; col<board.colSize(); col++)
            for(Card card:board.getCards(row, col, player)) if(Support.isBattleCard(card))
                cardFromOnBoardInfo(card, card.getCurrentLocation().getRowPos(), card.getCurrentLocation().getColPos());
    }
    public void cardFromOnBoardInfo(Card card, int row, int col) {
        System.out.print("Name:"+card.getName()+". ID:"+card.getID()+". Location:"+row+", "+col);
        if(!card.getClass().getSuperclass().toString().equals("CardThatCanBattle")) System.out.println(";");
        else System.out.println(". Health:" + ((CardThatCanBattle) card).getHealth() + ";");
    }
    public void cardsInPlayInfo(Player player) {
        for(Card activeCard:activeCards) if(activeCard.getOwner().equals(player))
            System.out.print("Name:" + activeCard.getName() + ". ID:" + activeCard.getID() + "; ");
        System.out.println();
    }
    public static void cardsFullInfo(Card card) {
        if(Support.isBattleCard(card)) {
            CardThatCanBattle aCard = (UnitCard)card;
            System.out.print("Name:"+aCard.getName()+". ID:"+aCard.getID()+". "+aCard.getHealth()+". "+aCard.getMana());
            if(aCard.getCardCategory().equals("Unit")) {
                System.out.println(". "+((UnitCard)aCard).getWeakSpotChance()+";");
                armorInfo(((UnitCard)aCard).getDefences());
            }
            else System.out.println(";");
        }
        else System.out.println("Name:"+card.getName()+". ID:"+card.getID());
        for(Effect effect:card.getInflictedEffects()) System.out.print(effect.getClass().getName()+"; "); System.out.println();
    }
    public static void armorInfo(List<Defence> defences) {
        for(Defence defence :defences)
            System.out.println(defence.getType()+","+ defence.getCoverage()+","+ defence.getDefence()+","+ defence.getLayer()+";");
    }
}

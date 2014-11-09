package CardGame;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kristján Tryggvason
 * Date: 8.3.2014
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class Board implements AIuses {
    private State state = new State();
    private List<List<Cell>> board = new ArrayList<List<Cell>>();
    private List<Player> players = new ArrayList<Player>();
    private List<AI> ai = new ArrayList<AI>();
    private boolean gameWon, endTurn, firstRound;
    private List<Boolean> playersLost = new ArrayList<Boolean>();
    private List<Card> activeCards = new ArrayList<Card>();
    private int playersSize, playersTurn, unitsPlaced;

    Board(List<List<Card>> decks) {
        playersSize = decks.size();
        // Set up all players
        for(List<Card> deck:decks) {
            state.addPlayer(new Player(deck));
        }
    }

    public void setState(State newState) { state = newState; }

    public boolean setUpBoard(List<List<TerrainType>> terrainMap,
                           List<Integer> playerBaseRowLocation, List<Integer> playerBaseColLocation)
    {
        if(terrainMap == null || terrainMap.size() == 0 || terrainMap.get(0) == null || terrainMap.get(0).size() == 0
           || playerBaseRowLocation.size() != playerBaseColLocation.size()) { return false; }

        setTerrain(terrainMap);
        int rowLocation, colLocation, top, bottom, left, right, areaDistance = 1;
        for(int i=0, size = playerBaseRowLocation.size(); i<size; i++) {
            rowLocation = playerBaseRowLocation.get(i); colLocation = playerBaseColLocation.get(i);
            state.addCardToBoard(new Base(state.getPlayer(i)), rowLocation, colLocation);
            top = rowLocation - areaDistance; if(top<0) top = 0; left = colLocation - areaDistance; if(left<0) left = 0;
            bottom = rowLocation + areaDistance; if(state.boardRowSize()-1<bottom) bottom = state.boardRowSize()-1;
            right = colLocation + areaDistance; if(state.boardColSize()-1<right) right = state.boardColSize()-1;
            for(int row=top; row<=bottom; row++) for(int col=left; col<=right; col++)
                state.setOwner(row, col, state.getPlayer(i));
        }
        state.setUpBoardInfluence();
        return true;
    }
    public void setTerrain(List<List<TerrainType>> terrainMap) {
        // board.clear();
        List<List<Cell>> board = new ArrayList<List<Cell>>();
        List<Cell> cellRow = new ArrayList<Cell>();
        for (int row=0, rowSize=terrainMap.size(); row<rowSize; row++) {
            for (int col=0, colSize=terrainMap.size(); col<colSize; col++) {
                cellRow.add(new Cell(terrainMap.get(row).get(col), row, col));
            }
            board.add(new ArrayList<Cell>(cellRow));
            cellRow.clear();
        }
        state.setBoard(board);
    }

    public void newGame() throws IllegalAccessException, InstantiationException {
        state.newInitialState();
    }

    public int play(int numberOfAI) throws IOException, IllegalAccessException, InstantiationException {
        if(numberOfAI>state.getPlayersSize()) numberOfAI = state.getPlayersSize();
        for(int i=0; i<numberOfAI; i++) { ai.add(new Test(i, state)); }
        state.newInitialState();
        Support.setState(state);
        char input;
        state.setFirstRound(true);
        while(!state.isGameWon()) {
            System.out.println("Player" + state.getPlayersTurn() + " turn");
            if(state.isFirstRound()) {
                System.out.print("Draw five cards, ");
                for(int i=0;i<5;i++) {
                    if(state.getPlayersTurn() >= numberOfAI) {
                        System.out.println("select u for unit card, s for spell card and i for item or structure card");
                        String in;
                        in = Support.getInput().toLowerCase();
                        if(!in.equals("")) drawCard(in.charAt(0));
                    }
                    else ai.get(state.getPlayersTurn()).draw(state);
                }
            }
            else {
                if(state.getPlayersTurn() >= numberOfAI) {
                    System.out.println("Draw a card, select u for unit card, s for spell card and i for item or structure card");
                    String in = Support.getInput().toLowerCase();
                    if (!in.equals("")) drawCard(in.charAt(0));
                }
                else ai.get(state.getPlayersTurn()).draw(state);
            }
            newTurn();
            while(!endTurn) {
                if(state.getPlayersTurn() >= numberOfAI) {
                    Support.getInput();
                    state.resourcesAvailable();
                    System.out.println("Enter the letter,\nh to see cards on hand\nb to see cards on board\n" +
                            "d to see cards full detail\np to place a card\na for attack\n" +
                            "m for move\nu to use ability\ne to end turn");
                    String in = Support.getInput().toLowerCase();
                    if (in.equals("")) input = 'q';
                    else input = in.charAt(0);

                    switch (input) {
                        case 'h':
                            state.cardsOnHand();
                            break;
                        case 'b':
                            state.allCardsInPlay();
                            break;
                        case 'd':
                            cardDetail();
                            break;
                        case 'p':
                            if (!playCard()) System.out.println("Could not play given card");
                            break;
                        case 'a':
                            if (!attack()) System.out.println("You did not succeed in attacking");
                            break;
                        case 'm':
                            if (!move()) System.out.println("You did not succeed in moving");
                            break;
                        case 'u':
                            if (!useAbility()) System.out.println("You did not succeed in using an action");
                            break;
                        case 'e':
                            endTurn();
                            break;
                        default:
                            break;
                    }
                }
                else {
                    ai.get(state.getPlayersTurn()).play(state, this);
                }
                checkCardDeath();
                checkLoss();
            }
        }
        state.winner();
        return state.getWinner();
    }

    public void newTurn() {
        unitsPlaced = 0;
        state.newTurn();
        endTurn = false;
    }

    public void endTurn() {
        state.endTurn();
        endTurn = true;
    }

    public boolean attack() {
        System.out.println("Your cards on the board are:");
        state.battleCardsFromOnBoardInfo();
        System.out.println("Opponents cards on the board are: ");
        for(Player player:state.getOpponents()) state.battleCardsFromOnBoardInfo(player);
        System.out.println("Enter the card that attacks and what card to attack in the form of: yourCardID, opponentCardID");
        String[] input = Support.getInput().split(","); if(input.length < 2) return false;
        int id = Integer.parseInt(input[0]), opponentsId = Integer.parseInt(input[1]);
        Card card = state.findCard(id);
        Card opponentsCard = state.findCard(opponentsId);
        if(card == null || opponentsCard == null || !Support.isBattleCard(card) || !Support.isBattleCard(opponentsCard)
                        || card.hasActed()) return false;
        CardThatCanBattle yourBattleCard = (CardThatCanBattle)card, opponentsBattleCard = (CardThatCanBattle)opponentsCard;
        if(!yourBattleCard.inRange(card, opponentsCard)) return false;
        List<String> terrainPlacement = Support.terrainsUsedForBattle(yourBattleCard, opponentsBattleCard);
        List<Damage> attack = yourBattleCard.whatIsYourAttack(opponentsBattleCard, terrainPlacement.get(1),
                                                              yourBattleCard, terrainPlacement.get(0));
        opponentsBattleCard.takeHit(yourBattleCard, terrainPlacement.get(0), terrainPlacement.get(1), attack);
        yourBattleCard.acted();
        if(opponentsBattleCard.getHealth() == 0) {
            opponentsBattleCard.getCurrentLocation().removeCard(opponentsBattleCard); return true;
        }
        attack = opponentsBattleCard.whatIsYourAttack(yourBattleCard, terrainPlacement.get(0),
                                                      opponentsBattleCard, terrainPlacement.get(1));
        yourBattleCard.takeHit(opponentsBattleCard, terrainPlacement.get(1), terrainPlacement.get(0), attack);
        if(yourBattleCard.getHealth() == 0) { yourBattleCard.getCurrentLocation().removeCard(yourBattleCard); }
        return true;
    }

    public boolean useAbility() throws InstantiationException, IllegalAccessException {
        System.out.println("Enter the card to use in the form of: cardID,abilityName\nYour cards on the board are:");
        state.cardsFromOnBoardInfo();
        String[] input = Support.getInput().split(","); if(input.length < 2) return false;
        Card yourCard = state.findCard(Integer.parseInt(input[0]));
        if(yourCard == null) return false;
        Action givenAction = yourCard.getAction(input[1]);
        if(givenAction == null || (yourCard.hasActed() && !givenAction.continuousAction())) return false;
        if(givenAction.actionTypeIs("Activate")) {
            if(!givenAction.continuousAction()) yourCard.acted();
            givenAction.activateAction(yourCard); return true;
        }
        else if(givenAction.actionTypeIs("TargetCard")) {
            System.out.print("Select "+givenAction.targetNumber()+" target");
            if(givenAction.targetNumber() != 1) System.out.print("s");
            System.out.println(" in the form of: cardID\nOpponents cards on the board are:");
            state.allCardsInPlay();
            input = Support.getInput().split(",");
            List<Card> targets = new ArrayList<Card>();
            Card enemy;
            for(String id:input) {
                enemy = state.findCard(Integer.parseInt(id));
                if(enemy == null) return false;
                targets.add(enemy);
            }
            if(!givenAction.continuousAction()) yourCard.acted();
            givenAction.useActionOn(targets, yourCard); return true;
        }
        return false;
    }

    public boolean move() {
        System.out.println("Enter what card and where to place it in the form of: cardID, toRow, toCol\n" +
                           "Your cards on the board are: ");
        state.cardsFromOnBoardInfo();
        String[] input = Support.getInput().split(","); if(input.length < 3) return false;
        int toRow = Integer.parseInt(input[1]), toCol = Integer.parseInt(input[2]);
        Card card = state.findCard(Integer.parseInt(input[0]));
        if(card == null || !Support.isBattleCard(card) || card.hasMoved()) return false;
        if(((CardThatCanBattle)card).isMoveLegal(toRow,toCol)) {
            card.getCurrentLocation().removeCard(card);
            state.newPlacementOfCard(card, toRow, toCol);
            card.moved();
            return true;
        }
        return false;
    }

    public boolean playCard() throws InstantiationException, IllegalAccessException {
        System.out.println("Enter what card you want to use, the cards in your hand that you can play are: ");
        Player player = state.getCurrentPlayer();
        for(Card card:player.getCards()) {
            System.out.print(card.getName() + "; ");
        } System.out.println();
        String cardName = Support.getInput();
        Card card = player.getCard(cardName);
        if(card == null || (card.getCardCategory().equals("Unit") && unitsPlaced > 1)) return false;
        if(card.playCard()) {
            if(card.getCardCategory().equals("Unit")) unitsPlaced++;
            player.removeCard(card); return true;
        }
        return false;
    }

    public void checkLoss() { state.checkLoss(); }

    public void drawCard(char aChar) {
        switch(aChar) {
            case 'u':
                state.getCurrentPlayer().drawUnit(); break;
            case 's':
                state.getCurrentPlayer().drawSpell(); break;
            case 'i':
                state.getCurrentPlayer().drawStructureOrItem(); break;
            default:
                break;
        }
    }

    public void cardDetail() {
        System.out.println("Enter what card to checkout in the form of: cardID\nYour cards on the board are:");
        state.cardsFromOnBoardInfo();
        Card yourCard = state.findCard(Integer.parseInt(Support.getInput()));
        if(yourCard != null) State.cardsFullInfo(yourCard);
    }

    public void checkCardDeath() { state.checkCardDeath(); }
}

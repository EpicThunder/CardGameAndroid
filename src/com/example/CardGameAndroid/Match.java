package com.example.CardGameAndroid;

import CardGame.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kristján on 3.11.2014.
 */
public class Match extends Activity {

    private State state = new State();
    private int cardsToDraw;
    private CardsView cardsView;
    private BoardView board;
    private TextView turnView, cardInfoView;
    private Player player;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        cardsView = (CardsView)findViewById(R.id.cards);
        board = (BoardView)findViewById(R.id.board);
        turnView = (TextView)findViewById(R.id.playerTurn);
        cardInfoView = (TextView)findViewById(R.id.cardInfo);

        //DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);

        //Cursor cursor = databaseAdapter.queryTableRow("Guard", "Unit", DbHelper.TableUnitCardsCols[1]);

        try {
            List<Card> deck = DeckFactory.getDeck1Second();
            state.addPlayer(new Player(deck));
            deck = DeckFactory.getDeck2Second();
            state.addPlayer(new Player(deck));
            List<List<Cell>> board = TerrainFactory.getDesertTerrain();
            state.setBoard(board);
            state.addCardToBoard(new Base(state.getPlayer(0)), 1, 1);
            state.addCardToBoard(new Base(state.getPlayer(1)), 4, 4);
            for(int r=0; r<3; r++) for(int c=0; c<3; c++) state.setOwner(r, c, state.getPlayer(0));
            for(int r=3; r<6; r++) for(int c=3; c<6; c++) state.setOwner(r, c, state.getPlayer(1));
            startNewMatch();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void cellClicked(int row, int col) {
        for(Cell cell:state.getInfluencedCells(state.getCurrentPlayer())) {
            if(cell.getRowPos() == row && cell.getColPos() == col) {
                Card cardToSet = cardsView.setCard();
                if(cell.placeCard(cardToSet)) {
                    turnView.setText("Player " + state.getPlayersTurn() + " turn. Gold:" + state.getCurrentPlayer().getGold() + ". Mana:" + state.getCurrentPlayer().getMana() + ".");
                    cardInfoView.setText("Your available cards");
                    board.invalidate();
                    cardsView.invalidate();
                }
                break;
            }
        }
    }

    public void cardSelected(Card card) {
        CardThatCanBattle battleCard;
        String text = card.getName()+" - Cost:"+card.getCost();
        if(card.getCardCategory() == "Unit" || card.getCardCategory() == "Structure") {
            battleCard = (CardThatCanBattle)card;
            text += " Health:"+battleCard.getHealth();
            int totalDamage = 0;
            for(Damage damage:battleCard.getAttack()) totalDamage += damage.getDamage();
            if(totalDamage > 0) text += " Attack:"+totalDamage;
            if(card.getCardCategory() == "Unit") {
                for(Defence defence:((UnitCard)card).getDefences()) {
                    text += " Defence:"+defence.getDefence()+" Mitigation chance:"+defence.getCoverage();
                }
            }
            else text += " Build time:"+((StructureCard)card).getBuildTime();
        } else if(card.getCardCategory() == "Spell") text += " mana";
        cardInfoView.setText(text);
    }

    public void buttonClick(View view) {
        cardsView.turnEnd();
    }

    public void imageButtonClick(View view) {
        ImageButton button = (ImageButton)view;
        int id = button.getId();
        if(id == R.id.unitDeck && cardsView.canDraw() ) {
            if(state.getCurrentPlayer().drawUnit()) cardsView.cardDrawn();
        }
        else if ( id == R.id.itemStructureDeck && cardsView.canDraw() ) {
            if(state.getCurrentPlayer().drawStructureOrItem()) cardsView.cardDrawn();
        }
        else if ( id == R.id.spellDeck && cardsView.canDraw() ) {
            if(state.getCurrentPlayer().drawSpell()) cardsView.cardDrawn();
        }
    }

    public void startNewMatch() {
        try {
            state.newInitialState();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        state.setFirstRound(true);
        state.newTurn();
        turnView.setText("Player " + state.getPlayersTurn() + " turn. Gold:" + state.getCurrentPlayer().getGold() + ". Mana:" + state.getCurrentPlayer().getMana() + ".");
        cardsToDraw = 5;
        board.setBoardState(6,state);
        cardsView.newGame(state);
    }
}
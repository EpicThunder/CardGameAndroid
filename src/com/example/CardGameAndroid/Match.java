package com.example.CardGameAndroid;

import CardGame.*;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);

        cardsView = (CardsView)findViewById(R.id.cards);
        board = (BoardView)findViewById(R.id.board);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);

        try {
            List<Card> deck = DeckFactory.getDeck1(databaseAdapter);
            if(deck.size()<1) ;
            Player player = new Player(deck);
            state.addPlayer(player);
            deck = DeckFactory.getDeck2(databaseAdapter);
            if(deck.size()<1) ;
            player = new Player(deck);
            state.addPlayer(player);
            state.setBoard(TerrainFactory.getDesertTerrain());
            state.addCardToBoard(new Base(state.getPlayer(0)), 1, 1);
            state.addCardToBoard(new Base(state.getPlayer(1)), 4, 4);
            for(int r=0; r<3; r++) for(int c=0; c<3; r++) state.setOwner(r, c, state.getPlayer(0));
            for(int r=4; r<7; r++) for(int c=4; c<7; r++) state.setOwner(r, c, state.getPlayer(1));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        startNewMatch();

        /*BoardFragment boardFragment = new BoardFragment();
        boardFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();*/
        //fragmentTransaction.add(R.id.board_fragment, boardFragment);
        //fragmentTransaction.commit();
    }

    public void buttonClick(View view) {
        Button button = (Button)view;
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
        //else if ( id == R.id.endTurn ) {}
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
        cardsToDraw = 5;
        cardsView.newGame(state);
    }
}
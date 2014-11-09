package com.example.CardGameAndroid;

import CardGame.Card;

/**
 * Created by Kristján on 14.9.2014.
 */
public class CardCoordinate {

    private int m_col, m_row;
    private Card card;
    CardCoordinate(int col, int row) {
        card = null;
        m_col = col;
        m_row = row;
    }

    public int getCol() {
        return m_col;
    }

    public int getRow() {
        return m_row;
    }

    public boolean noCard() { if(card == null) return true; else return false; }

}

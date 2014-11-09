package com.example.CardGameAndroid;

import CardGame.Card;
import CardGame.State;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 5.11.2014.
 */
public class CardsView extends View {

    private State state;
    private int cardsToDraw = 0, width, widthSpace, heightPadding, cardSelected;
    private boolean cardIsSelected;

    private BitmapDrawable card, selectedCard;
    private Paint paint = new Paint();
    private List<Card> onHand = new ArrayList<Card>();
    private Card cardClicked;

    public CardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cardIsSelected = false;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor( Color.GRAY );

        card = (BitmapDrawable)context.getResources().getDrawable(R.drawable.card);
        selectedCard  = (BitmapDrawable)context.getResources().getDrawable(R.drawable.card_selected);
    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        width  = xNew - getPaddingLeft() - getPaddingRight();
        int height = yNew - getPaddingTop() - getPaddingBottom();
        widthSpace = width - (card.getIntrinsicWidth()*onHand.size());
        heightPadding = (height - card.getIntrinsicHeight())/2;
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        for(int i=0, size=onHand.size(); i<size; i++) {
            if(cardIsSelected && cardSelected == i) {
                canvas.drawBitmap(selectedCard.getBitmap(), (i * card.getIntrinsicWidth()) + (i + 1) * (widthSpace / (size + 1)), heightPadding, paint);
            }
            else {
                canvas.drawBitmap(card.getBitmap(), (i * card.getIntrinsicWidth()) + (i + 1) * (widthSpace / (size + 1)), heightPadding, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();         // NOTE: event.getHistorical... might be needed.
        int y = (int) event.getY();



        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(heightPadding<=y && y<=heightPadding+card.getIntrinsicHeight()) {
                for (int i = 0, size = onHand.size(); i < size; i++) {
                    if((i*card.getIntrinsicWidth())+(i+1)*(widthSpace/(size+1))<=x &&
                        x<=(i*card.getIntrinsicWidth())+(i+1)*(widthSpace/(size+1))+card.getIntrinsicWidth()){
                        cardClicked = onHand.get(i);
                        cardSelected = i;
                        cardIsSelected = true;
                        invalidate();
                        Match match = (Match) this.getContext();
                        match.cardSelected(cardClicked);
                    }
                }
            }
        }
        return true;
    }

    public Card setCard() {
        if(cardIsSelected && onHand.get(cardSelected).canPay()) {
            cardIsSelected = false;
            return onHand.get(cardSelected);
        }
        else return null;
    }

    public void turnEnd() { cardsToDraw = 1; }

    public void newGame(State startState) { state = startState; cardsToDraw = 5; }

    public void setState(State newState) { state = newState; onHand = state.getCurrentPlayer().getCards(); }

    public void cardDrawn() {
        cardsToDraw--; onHand = state.getCurrentPlayer().getCards();
        myInvalidate();
    }

    public boolean canDraw() { return 0<cardsToDraw; }

    public void myInvalidate() {
        widthSpace = width - (card.getIntrinsicWidth()*onHand.size());
        invalidate();
    }
}

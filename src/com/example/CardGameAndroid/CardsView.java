package com.example.CardGameAndroid;

import CardGame.Card;
import CardGame.State;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 5.11.2014.
 */
public class CardsView extends View {

    private State state;
    private int cardsToDraw = 0, width, height, widthSpace, heightPadding;

    private BitmapDrawable card;
    private Paint paint = new Paint();
    private List<Card> onHand = new ArrayList<Card>();

    public CardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor( Color.GRAY );

        card = (BitmapDrawable)context.getResources().getDrawable(R.drawable.card);
    }

    /*@Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        width  = xNew - getPaddingLeft() - getPaddingRight();
        height = yNew - getPaddingTop() - getPaddingBottom();
        widthSpace = width - (card.getIntrinsicWidth()*onHand.size());
        heightPadding = (height - card.getIntrinsicHeight())/2;
    }*/

    @Override
    protected void onDraw( Canvas canvas ) {
        /*for(int i=0, size=onHand.size(); i<size; i++) {
            canvas.drawBitmap(card.getBitmap(), widthSpace/size, heightPadding, paint);
        }*/
        canvas.drawBitmap(card.getBitmap(), 0, 0, paint);
    }

    public void newGame(State startState) { state = startState; cardsToDraw = 5; }

    public void cardDrawn() {
        cardsToDraw--; onHand = state.getCurrentPlayer().getCards();
        //widthSpace = width - (card.getIntrinsicWidth()*onHand.size());
    }

    public boolean canDraw() { return 0<cardsToDraw; }
}

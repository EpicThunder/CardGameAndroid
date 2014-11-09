package com.example.CardGameAndroid;

import CardGame.Card;
import CardGame.Cell;
import CardGame.State;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristján on 3.11.2014.
 */
public class BoardView extends View {

    private int SIZE = 6;
    private int m_cellWidth, m_cellHeight, widthSpace, heightSpace, sw;
    private State state;
    private boolean stateSet;

    private BitmapDrawable cardToken, selectedCardToken, opponentCardToken, bothCardToken;
    private List<List<CardCoordinate>> cardsOnBoard = new ArrayList<List<CardCoordinate>>();
    private Rect m_rect = new Rect(), m_colorRect = new Rect();
    private Paint m_paintGrid = new Paint(), m_paintMyGrid = new Paint(), m_paintOpponentGrid = new Paint();

    private int xToCol( int x ) {
        return (x - getPaddingLeft()) / m_cellWidth;
    }

    private int yToRow( int y ) {
        return (y - getPaddingTop()) / m_cellHeight;
    }

    private int colToX( int col ) {
        return col * m_cellWidth + getPaddingLeft() ;
    }

    private int rowToY( int row ) {
        return row * m_cellHeight + getPaddingTop() ;
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        stateSet = false;
        m_paintGrid.setStyle(Paint.Style.STROKE);
        m_paintGrid.setColor( Color.GRAY );
        m_paintMyGrid.setColor( Color.BLUE ); //3368652
        m_paintOpponentGrid.setColor( Color.RED ); //16724736
        List<CardCoordinate> row;
        for(int r=0; r<SIZE; r++) {
            row = new ArrayList<CardCoordinate>();
            for(int c=0; c<SIZE; c++) row.add(new CardCoordinate(c, r));
            cardsOnBoard.add(row);
        }

        cardToken = (BitmapDrawable)context.getResources().getDrawable(R.drawable.card_token);
        selectedCardToken = (BitmapDrawable)context.getResources().getDrawable(R.drawable.card_token_selected);
        opponentCardToken = (BitmapDrawable)context.getResources().getDrawable(R.drawable.opponent_card_token);
        bothCardToken = (BitmapDrawable)context.getResources().getDrawable(R.drawable.both_player_card_token);
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );

        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension( size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom() );
    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        sw = Math.max(1, (int) m_paintGrid.getStrokeWidth());
        m_cellWidth  = (xNew - getPaddingLeft() - getPaddingRight() - sw) / SIZE;
        m_cellHeight = (yNew - getPaddingTop() - getPaddingBottom() - sw) / SIZE;
        widthSpace = m_cellWidth-cardToken.getIntrinsicWidth();
        heightSpace = m_cellHeight-cardToken.getIntrinsicHeight();
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        Cell cell;
        for(int r=0; r<SIZE; ++r){
            for(int c=0; c<SIZE; ++c) {
                int x = colToX(c), y = rowToY(r);
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                m_colorRect.set(x+sw, y+sw, x+m_cellWidth-sw, y+m_cellHeight-sw);
                if(!stateSet) canvas.drawRect(m_rect, m_paintGrid);
                else {
                    cell = state.getCell(r,c);
                    canvas.drawRect(m_rect, m_paintGrid);
                    if(cell.hasInfluenceOver(state.getPlayer(0))) canvas.drawRect(m_colorRect, m_paintMyGrid);
                    else if(cell.hasInfluenceOver(state.getPlayer(1))) canvas.drawRect(m_colorRect, m_paintOpponentGrid);
                    if(cell.hasCardAtCell(state.getPlayer(0)) && cell.hasCardAtCell(state.getPlayer(1))){
                        canvas.drawBitmap(bothCardToken.getBitmap(), x+(widthSpace/2), y+(heightSpace/2), m_paintGrid);
                    } else if(cell.hasCardAtCell(state.getPlayer(0))) {
                        canvas.drawBitmap(cardToken.getBitmap(), x+(widthSpace/2), y+(heightSpace/2), m_paintGrid);
                    } else if(cell.hasCardAtCell(state.getPlayer(1))) {
                        canvas.drawBitmap(opponentCardToken.getBitmap(), x+(widthSpace/2), y+(heightSpace/2), m_paintGrid);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();         // NOTE: event.getHistorical... might be needed.
        int y = (int) event.getY();
        int c = xToCol(x), r = yToRow(y);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Match match = (Match) this.getContext();
            match.cellClicked(r,c);
        }
        return true;
    }

    public void setBoardState(int size, State startState) {
        /*SIZE = size;
        List<CardCoordinate> row;
        for(int r=0; r<SIZE; r++) {
            row = new ArrayList<CardCoordinate>();
            for(int c=0; c<SIZE; c++) row.add(new CardCoordinate(c, r));
            cardsOnBoard.add(row);
        }*/
        state = startState;
        SIZE = state.boardColSize();
        stateSet = true;
    }
}

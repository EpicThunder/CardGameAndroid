package com.example.CardGameAndroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kristján on 3.11.2014.
 */
public class BoardView extends View {

    private int SIZE = 6;
    private int m_cellWidth;
    private int m_cellHeight;

    private Rect m_rect = new Rect();
    private Paint m_paintGrid = new Paint();

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
        m_paintGrid.setStyle(Paint.Style.STROKE);
        m_paintGrid.setColor( Color.GRAY );
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
        int sw = Math.max(1, (int) m_paintGrid.getStrokeWidth());
        m_cellWidth  = (xNew - getPaddingLeft() - getPaddingRight() - sw) / SIZE;
        m_cellHeight = (yNew - getPaddingTop() - getPaddingBottom() - sw) / SIZE;
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        for(int r=0; r<SIZE; ++r){
            for(int c=0; c<SIZE; ++c) {
                int x = colToX(c), y = rowToY(r);
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                canvas.drawRect(m_rect, m_paintGrid);
            }
        }
    }

    public void setBoardState(int size) {
        SIZE = size;
    }
}

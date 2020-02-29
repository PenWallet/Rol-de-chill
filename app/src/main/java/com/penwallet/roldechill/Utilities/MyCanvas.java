package com.penwallet.roldechill.Utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyCanvas extends View {
    float width;
    private ArrayList<Pair<Path, Paint>> paths;
    private Path pathInUse;
    private Paint paintInUse;
    boolean isPencil;

    public MyCanvas(Context context, float width, boolean isPencil, ArrayList<Pair<Path, Paint>> paths) {
        super(context);
        this.width = width;
        this.isPencil = isPencil;

        this.paths = paths;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Pair<Path, Paint> path : paths)
        {
            canvas.drawPath(path.first, path.second);
        }
        if(pathInUse != null && paintInUse != null)
        {
            canvas.drawPath(pathInUse, paintInUse);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                pathInUse = new Path();

                paintInUse = new Paint();
                paintInUse.setAntiAlias(true);
                paintInUse.setColor(isPencil ? Color.BLACK : Color.WHITE);
                paintInUse.setStrokeJoin(Paint.Join.ROUND);
                paintInUse.setStyle(Paint.Style.STROKE);
                paintInUse.setStrokeWidth(isPencil ? width : width*2);

                pathInUse.moveTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_MOVE:
                pathInUse.lineTo(xPos, yPos);
                break;

            case MotionEvent.ACTION_UP:
                paths.add(new Pair<>(pathInUse, paintInUse));
                pathInUse = null;
                paintInUse = null;
                break;

            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void changeToPencil()
    {
        isPencil = true;
    }

    public void changeToEraser()
    {
        isPencil = false;
    }

    public void changeStrokeWidth(float width)
    {
        this.width = width;
    }

    public void undoLast()
    {
        if(!paths.isEmpty())
        {
            paths.remove(paths.size()-1);
            invalidate();
        }
    }

    public void clearCanvas()
    {
        paths.clear();
        invalidate();
    }

    public ArrayList<Pair<Path, Paint>> getPaths() {
        return paths;
    }
}

package project.gobelins.wasabi.fresco.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * La vue contenant le dessin
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawView extends View
{
    Paint mPaint;

    /* Les points courants */
    ArrayList<Point> mPoints;
    private Path mPath;
    private int mColor;

    public DrawView(Context context)
    {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Initialisation de la couleur de base */
        mColor = Color.BLACK;

        /* Initialisation du canvas */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(10);

        /* La liste */
        mPoints = new ArrayList<Point>();

        /* Le path */
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        /* Réinitialisation du path */
        mPath.reset();

        /* Dessin au move */
        boolean first = true;
        for(Point point : mPoints)
        {
            if(first)
            {
                first = false;
                mPath.moveTo(point.x, point.y);
            }
            else
                mPath.lineTo(point.x, point.y);
        }

        /* Dessin du path */
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * Dessine du point précédent au nouveau
     *
     * @param p Le point jusqu'où tracer la ligne
     */
    public void drawTo(Point p)
    {
        /* Le point courant */
        mPoints.add(p);

        /* On dessine */
        invalidate();
    }

    /**
     * Initialise le point de départ (au touch down)
     *
     * @param p Le point de départ
     */
    public void setFirstPoint(Point p)
    {
        mPoints.add(p);
    }

    /**
     * Change la couleur du path
     *
     * @param colorFilter La nouvelle couleur du path
     */
    public void changeColor(ColorFilter colorFilter)
    {
        mPaint.setColorFilter(colorFilter);
        invalidate();
    }

    /**
     * Réinitialise le path
     */
    private void resetPath()
    {
        mPath.reset();
    }

    /**
     * @return La liste des points
     */
    public ArrayList<Point> getPoints()
    {
        return mPoints;
    }

    /**
     * Supprime tous les points
     */
    public void clearPoints()
    {
        mPoints.clear();
    }

    public void clear()
    {
        /* On réinitialise le path */
        resetPath();

        invalidate();
    }
}
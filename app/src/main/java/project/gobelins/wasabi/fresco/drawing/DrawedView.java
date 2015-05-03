package project.gobelins.wasabi.fresco.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;

/**
 * La vue contenant le dessin dessiné
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawedView extends DrawView
{
    Paint mPaint;

    /* Les points courants */
    private Path mPath;
    private int mColor;
    private ArrayList<ArrayList<Point>> mPoints;

    public DrawedView(Context context)
    {
        super(context);
    }

    public DrawedView(Context context, AttributeSet attrs)
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
        mPoints = new ArrayList<ArrayList<Point>>();

        /* Le path */
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        /* Changement de couleur */
        mPaint.setColor(mColor);

        /* On dessine une ligne smooth au up */
        if(mPoints.size() > 0)
        {
            drawSmoothLine();

            /* Dessin du path */
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * Dessine une ligne smooth
     */
    private void drawSmoothLine()
    {
        /* Tous les nouveaux points */
        ArrayList<Point> points = getCurvePoints(0.5);

        if(points.size() > 0)
        {
            mPath.moveTo(points.get(0).x, points.get(0).y);
            for(int i = 1; i < points.size() - 1; ++i)
                mPath.lineTo(points.get(i).x, points.get(i).y);
        }
    }

    /**
     * Smooth la courbe
     *
     * @param tension La tension
     * @return Le tableau de nouveaux points
     */
    private ArrayList<Point> getCurvePoints(double tension)
    {
        int numOfSegments = 16;

        ArrayList<Point> returnPoints = new ArrayList<Point>();
        ArrayList<Point> points = mPoints.get(mPoints.size() - 1);
        int i;
        double x, y, t1x, t2x, t1y, t2y, c1, c2, c3, c4, st, t;

        /* On duplique le point final pour les calculs */
        points.add(points.get(points.size() - 1));

        /* On parcourt tous les points */
        for(i = 1; i < (points.size() - 2); ++i)
        {
            /* On rajoute les segments */
            for(t = 0; t <= numOfSegments; ++t)
            {
                Point p = new Point();

                /* Calcul des vecteurs */
                t1x = (points.get(i + 1).x - points.get(i - 1).x) * tension;
                t2x = (points.get(i + 2).x - points.get(i).x) * tension;

                t1y = (points.get(i + 1).y - points.get(i - 1).y) * tension;
                t2y = (points.get(i + 2).y - points.get(i).y) * tension;

                /* Calcul du pas */
                st = t / numOfSegments;

                /* Calcul des cardinaux */
                c1 = 2 * Math.pow(st, 3) - 3 * Math.pow(st, 2) + 1;
                c2 = -(2 * Math.pow(st, 3)) + 3 * Math.pow(st, 2);
                c3 = Math.pow(st, 3) - 2 * Math.pow(st, 2) + st;
                c4 = Math.pow(st, 3) - Math.pow(st, 2);

                /* Calcul de x et y */
                x = c1 * points.get(i).x + c2 * points.get(i + 1).x + c3 * t1x + c4 * t2x;
                y = c1 * points.get(i).y + c2 * points.get(i + 1).y + c3 * t1y + c4 * t2y;

                /* Ajout du point */
                p.set(x, y);
                returnPoints.add(p);
            }
        }

        return returnPoints;
    }

    /**
     * Dessine une nouvelle courbe
     *
     * @param points Les points
     */
    public void draw(ArrayList<Point> points)
    {
        mPoints.add(points);
        invalidate();
    }
}
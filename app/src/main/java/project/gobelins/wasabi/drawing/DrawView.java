package project.gobelins.wasabi.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * La vue contenant le dessin
 *
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawView extends View
{
    Paint mPaint;

    /* Les points courants */
    ArrayList<Point> mPoints;
    private Path mPath;
    private boolean mSmoothLine;

    public DrawView(Context context)
    {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Initialisation du canvas */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth(10);

        /* La liste */
        mPoints = new ArrayList<Point>();
        mSmoothLine = false;

        /* Le path */
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        /* Réinitialisation du path */
        mPath.reset();

        /* On dessine une ligne smooth au up */
        if(mSmoothLine)
        {
            drawSmoothLine();
            mSmoothLine = false;
        }
        /* Dessin au move */
        else
        {
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
        }

        /* Dessin du path */
        canvas.drawPath(mPath, mPaint);
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
            for(int i = 1; i < points.size() - 1; i++)
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
        ArrayList<Point> points = mPoints;
        int i;
        double x, y, t1x, t2x, t1y, t2y, c1, c2, c3, c4, st, t;

        /* On duplique le poitn final pour les calculs */
        points.add(points.get(points.size() - 1));

        /* On parcourt tous les points */
        for(i = 1; i < (points.size() - 2); i++)
        {
            /* On rajoute les segments */
            for(t = 0; t <= numOfSegments; t++)
            {
                Point p = new Point();

                /* Calcul des vecteurs */
                t1x = (points.get(i + 1).x - points.get(i - 1).x) * tension;
                t2x = (points.get(i + 2).x - points.get(i).x) * tension;

                t1y = (points.get(i + 1).y - points.get(i - 1).y) * tension;
                t2y = (points.get(i + 2).y - points.get(i).y) * tension;

                /* Calcul du pas */
                st = t / numOfSegments;

                /* Calcule des cardinaux */
                c1 = 2 * Math.pow(st, 3) - 3 * Math.pow(st, 2) + 1;
                c2 = -(2 * Math.pow(st, 3)) + 3 * Math.pow(st, 2);
                c3 = Math.pow(st, 3) - 2 * Math.pow(st, 2) + st;
                c4 = Math.pow(st, 3) - Math.pow(st, 2);

                /* Calcule de x et y */
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
        mPoints.clear();
        mPoints.add(p);
    }

    /**
     * Rend la ligne plus smooth
     */
    public void smoothify()
    {
        mSmoothLine = true;
        invalidate();
    }
}
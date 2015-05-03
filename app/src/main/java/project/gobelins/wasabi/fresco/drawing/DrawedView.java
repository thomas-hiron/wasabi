package project.gobelins.wasabi.fresco.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * La vue contenant le dessin dessiné
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawedView extends DrawView
{
    private Paint mPaint;

    /* Les points courants */
    private Path mPath;

    private ArrayList<Path> mPaths;
    private ArrayList<Point> mPoints;
    private ArrayList<Integer> mColors;


    public DrawedView(Context context)
    {
        super(context);
    }

    public DrawedView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Initialisation de la paire Path/Paint */
        mPaths = new ArrayList<Path>();

        /* Initialisation des couleurs */
        mColors = new ArrayList<Integer>();

        /* Initialisation du paint */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        /* On dessine une ligne smooth au up */
        if(mPoints != null && mPoints.size() > 0)
        {
            /* On dessine */
            drawSmoothLine();

            /* Dessin du path */
            for(int i = 0, l = mPaths.size(); i < l; ++i)
            {
                /* Changement de la couleur */
                mPaint.setColor(mColors.get(i));
                /* On dessine */
                canvas.drawPath(mPaths.get(i), mPaint);
            }
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
        int i;
        double x, y, t1x, t2x, t1y, t2y, c1, c2, c3, c4, st, t;

        /* On duplique le point final pour les calculs */
        mPoints.add(mPoints.get(mPoints.size() - 1));

        /* On parcourt tous les points */
        for(i = 1; i < (mPoints.size() - 2); ++i)
        {
            /* On rajoute les segments */
            for(t = 0; t <= numOfSegments; ++t)
            {
                Point p = new Point();

                /* Calcul des vecteurs */
                t1x = (mPoints.get(i + 1).x - mPoints.get(i - 1).x) * tension;
                t2x = (mPoints.get(i + 2).x - mPoints.get(i).x) * tension;

                t1y = (mPoints.get(i + 1).y - mPoints.get(i - 1).y) * tension;
                t2y = (mPoints.get(i + 2).y - mPoints.get(i).y) * tension;

                /* Calcul du pas */
                st = t / numOfSegments;

                /* Calcul des cardinaux */
                c1 = 2 * Math.pow(st, 3) - 3 * Math.pow(st, 2) + 1;
                c2 = -(2 * Math.pow(st, 3)) + 3 * Math.pow(st, 2);
                c3 = Math.pow(st, 3) - 2 * Math.pow(st, 2) + st;
                c4 = Math.pow(st, 3) - Math.pow(st, 2);

                /* Calcul de x et y */
                x = c1 * mPoints.get(i).x + c2 * mPoints.get(i + 1).x + c3 * t1x + c4 * t2x;
                y = c1 * mPoints.get(i).y + c2 * mPoints.get(i + 1).y + c3 * t1y + c4 * t2y;

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
        /* Nouveau path */
        mPath = new Path();
        mPaths.add(mPath);

        /* Nouvelle couleur */
        Point point = points.get(0);

        /* On change la couleur */
        if(point instanceof ColorPoint)
            mColors.add(((ColorPoint) point).getColor());

        /* Changement des points */
        mPoints = points;

        /* On dessine */
        invalidate();
    }
}
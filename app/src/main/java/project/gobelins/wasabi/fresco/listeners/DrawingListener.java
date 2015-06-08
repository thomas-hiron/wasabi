package project.gobelins.wasabi.fresco.listeners;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.ColorPoint;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;

/**
 * Permet de dessiner
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawingListener implements View.OnTouchListener
{
    private Wasabi mWasabi;
    private Fresco mFresco;
    private DrawView mDrawView;
    private DrawedView mDrawedView;

    public DrawingListener(DrawView drawView, DrawedView drawedView, Fresco fresco)
    {
        mDrawView = drawView;
        mDrawedView = drawedView;
        mFresco = fresco;
    }

    public DrawingListener(DrawView drawView, DrawedView drawedView, Wasabi wasabi)
    {
        mDrawView = drawView;
        mDrawedView = drawedView;
        mWasabi = wasabi;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        /* On dessine */
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            ColorPoint p = new ColorPoint();
            p.set(event.getX(), event.getY());
            mDrawView.setFirstPoint(p);

            /* On cache les boutons d'action (dessiner, image, son, fermer) */
            if(mFresco != null)
                mFresco.hideInterfaceButtons();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            Point p = new Point();
            p.set(event.getX(), event.getY());
            mDrawView.drawTo(p);
        }
        else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            /* Suppression du path */
            mDrawView.clear();

            /* Récupération des points */
            ArrayList<Point> points = (ArrayList<Point>) mDrawView.getPoints().clone();

            /* Réinitialisation des points */
            mDrawView.clearPoints();

            /* Dessin des points dans l'autre vue */
            mDrawedView.draw(points);

            if(mFresco != null)
            {
                /* Enregistrement des points */
                mFresco.saveDrawing(points);

                /* Affichage des boutons d'interface */
                mFresco.showInterfaceButtons();
            }
            else if(mWasabi != null)
                mWasabi.saveDrawing(points);
            return false;
        }

        return true;
    }
}
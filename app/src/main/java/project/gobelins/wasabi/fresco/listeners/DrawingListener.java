package project.gobelins.wasabi.fresco.listeners;

import android.view.MotionEvent;
import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.Point;

/**
 * Permet de dessiner
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawingListener implements View.OnTouchListener
{
    private Fresco mFresco;
    private DrawView mDrawView;

    public DrawingListener(DrawView drawView, Fresco fresco)
    {
        mDrawView = drawView;
        mFresco = fresco;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        /* On dessine */
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Point p = new Point();
            p.set(event.getX(), event.getY());
            mDrawView.setFirstPoint(p);

            /* On cache les boutons d'action (dessiner, image, son, fermer) */
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
            /* Smooth de la courbe */
            mDrawView.smoothify();

            /* On affiche les boutons d'interface */
            mFresco.showInterfaceButtons();

            return false;
        }

        return true;
    }
}
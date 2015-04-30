package project.gobelins.wasabi.listeners;

import android.view.MotionEvent;
import android.view.View;

import project.gobelins.wasabi.drawing.DrawView;
import project.gobelins.wasabi.drawing.Point;

/**
 * Permet de dessiner
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class DrawingListener implements View.OnTouchListener
{
    private DrawView mDrawView;

    public DrawingListener(DrawView drawView)
    {
        mDrawView = drawView;
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
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            Point p = new Point();
            p.set(event.getX(), event.getY());
            mDrawView.drawTo(p);
        }
        else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            mDrawView.smoothify();
            return false;
        }

        return true;
    }
}
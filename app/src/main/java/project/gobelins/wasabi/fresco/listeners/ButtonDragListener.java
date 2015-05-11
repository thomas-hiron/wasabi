package project.gobelins.wasabi.fresco.listeners;

import android.view.MotionEvent;
import android.view.View;

/**
 * Classe perso qui gère le drag
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ButtonDragListener implements View.OnTouchListener
{
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        /* On replace l'élément */
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            view.setX(motionEvent.getRawX() - view.getWidth() / 2);
            view.setY(motionEvent.getRawY() - view.getHeight() / 2);
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            /* Suppression du listener */
            view.setOnTouchListener(null);

            // TODO : Enregistrement de la position du son
        }

        return true;
    }
}

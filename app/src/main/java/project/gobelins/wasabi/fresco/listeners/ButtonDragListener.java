package project.gobelins.wasabi.fresco.listeners;

import android.view.MotionEvent;
import android.view.View;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;

/**
 * Classe perso qui gère le drag
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ButtonDragListener implements View.OnTouchListener
{
    private boolean mInterfaceHidden;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        /* On replace l'élément */
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            /* Si interface non cachée (bool action ACTION_DOWN non appelé) */
            if(!mInterfaceHidden)
            {
                View root = view.getRootView();
                Fresco fresco = (Fresco) root.findViewById(R.id.fresco_container);

                fresco.hideInterfaceButtons();
                mInterfaceHidden = true;
            }

            view.setX(motionEvent.getRawX() - view.getWidth() / 2);
            view.setY(motionEvent.getRawY() - view.getHeight() / 2);

            /* Pour ne pas laisser de traces lors du drag */
            ((View) view.getParent()).invalidate();
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            /* Suppression du listener */
            view.setOnTouchListener(null);

            /* On raffiche l'interface */
            View root = view.getRootView();
            Fresco fresco = (Fresco) root.findViewById(R.id.fresco_container);

            fresco.showInterfaceButtons();
            mInterfaceHidden = false;

            // TODO : Enregistrement de la position du son
        }

        return true;
    }
}

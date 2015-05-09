package project.gobelins.wasabi.fresco.listeners;

import android.view.DragEvent;
import android.view.View;

import project.gobelins.wasabi.fresco.views.SoundButton;

/**
 * Classe perso qui gère le drag
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class SoundDragListener implements View.OnDragListener
{
    private SoundButton mSoundButton;

    public SoundDragListener(SoundButton soundButton)
    {
        mSoundButton = soundButton;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent)
    {
        /* Pour accepter le drag et recevoir le drop */
        if(dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED)
            return true;
        /* Au drop, on repositionne l'objet */
        else if(dragEvent.getAction() == DragEvent.ACTION_DROP)
        {
            /* On raffiche le bouton */
            mSoundButton.setVisibility(View.VISIBLE);

            /* On replace le bouton */
            mSoundButton.setX(dragEvent.getX() - mSoundButton.getWidth() / 2);
            mSoundButton.setY(dragEvent.getY() - mSoundButton.getHeight() / 2);

            view.setOnDragListener(null);
        }

        return false;
    }
}

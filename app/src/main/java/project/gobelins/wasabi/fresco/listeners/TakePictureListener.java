package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.interfaces.OnPictureListener;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class TakePictureListener implements View.OnClickListener
{
    private Fresco mListener;

    public TakePictureListener(Fresco listener)
    {
        mListener = listener;
    }

    @Override
    public void onClick(View view)
    {
        /* On se déplace */
        mListener.goToLastFragment(false);

        /* On ouvre l'appareil photo */
        mListener.onTakePicture();
    }
}

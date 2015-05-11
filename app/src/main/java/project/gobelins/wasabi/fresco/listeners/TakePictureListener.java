package project.gobelins.wasabi.fresco.listeners;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;
import project.gobelins.wasabi.interfaces.OnPictureListener;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class TakePictureListener implements View.OnClickListener
{
    private OnPictureListener mListener;

    public TakePictureListener(OnPictureListener listener)
    {
        mListener = listener;
    }

    @Override
    public void onClick(View view)
    {
        /* On ouvre l'appareil photo */
        mListener.onTakePicture();
    }
}

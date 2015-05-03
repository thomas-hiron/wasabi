package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.interfaces.OnCanceledListener;

/**
 * Annule le dernier dessin
 * Created by ThomasHiron on 03/05/2015.
 */
public class CancelLastDrawListener implements View.OnClickListener
{
    private OnCanceledListener mListener;

    public CancelLastDrawListener(Fresco fresco)
    {
        mListener = fresco;
    }

    @Override
    public void onClick(View view)
    {
        mListener.onCanceled();
    }
}

package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public abstract class FrescoActionButton extends ImageView
{
    protected Fresco mFresco;
    private boolean mStateActive;

    public FrescoActionButton(Context context)
    {
        super(context);
    }

    public FrescoActionButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mStateActive = false;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Le root */
        View rootView = getRootView();

        /* Initialisation de la fresque */
        mFresco = (Fresco) rootView.findViewById(R.id.fresco_container);
    }

    /**
     * Change l'état du bouton
     * @param active
     */
    public void changeState(boolean active)
    {
        /* Le bouton a été désactivé (encore actif) */
        if(mStateActive)
            buttonStateDisabled();

        /* On change l'état */
        mStateActive = active;
    }

    /**
     * Change l'état du bouton
     */
    public void changeState()
    {
        changeState(!mStateActive);
    }

    /**
     * @return si le bouton est actif
     */
    public boolean isActive()
    {
        return mStateActive;
    }

    /**
     * Traitement particuliers lorsque le bouton vient d'être désactivé
     */
    public abstract void buttonStateDisabled();
}

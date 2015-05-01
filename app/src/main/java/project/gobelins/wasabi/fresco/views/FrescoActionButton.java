package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public abstract class FrescoActionButton extends ImageView
{
    private int mResource;
    private int mActiveResource;
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

    /**
     * @return L'id de la resource
     */
    public int getResource()
    {
        return mResource;
    }

    /**
     * @param resourceId La resource par défaut
     */
    public void setResource(int resourceId)
    {
        mResource = resourceId;
    }

    /**
     * @return L'id de la resource
     */
    public int getActiveResource()
    {
        return mActiveResource;
    }

    public void setActiveResource(int activeResourceId)
    {
        mActiveResource = activeResourceId;
    }

    /**
     * Change l'état du bouton
     * @param active
     */
    public void changeState(boolean active)
    {
        mStateActive = active;

        /* Le bouton a été désactivé */
        if(!mStateActive)
            buttonStateDisabled();
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

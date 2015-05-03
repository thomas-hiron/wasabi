package project.gobelins.wasabi.fresco.views.buttons;

import android.content.Context;
import android.util.AttributeSet;

import project.gobelins.wasabi.fresco.views.FrescoActionButton;

/**
 * Created by ThomasHiron on 03/05/2015.
 */
public class CancelButton extends FrescoActionButton
{
    private boolean mIsActive;

    public CancelButton(Context context)
    {
        super(context);
    }

    public CancelButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mIsActive = false;
    }

    /**
     * Traitement particuliers lorsque le bouton vient d'être désactivé
     */
    @Override
    public void buttonStateDisabled()
    {

    }

    /**
     * @return Si le bouton est actif
     */
    public boolean isActive()
    {
        return mIsActive;
    }

    public void isActive(boolean isActive)
    {
        mIsActive = isActive;
    }
}

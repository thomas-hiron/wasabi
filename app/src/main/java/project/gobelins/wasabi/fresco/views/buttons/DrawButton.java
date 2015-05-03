package project.gobelins.wasabi.fresco.views.buttons;

import android.content.Context;
import android.util.AttributeSet;

import project.gobelins.wasabi.fresco.views.FrescoActionButton;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class DrawButton extends FrescoActionButton
{
    public DrawButton(Context context)
    {
        super(context);
    }

    public DrawButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Traitement particuliers lorsque le bouton vient d'être désactivé
     */
    @Override
    public void buttonStateDisabled()
    {
        /* On cache les couleurs */
        mFresco.hideColorsButtons();

        /* On cache la flèche */
        mFresco.hideCancelButton();

        /* On supprime le listener pour dessiner */
        mFresco.removeDrawingListeners();
    }
}

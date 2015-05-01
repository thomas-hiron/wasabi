package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;

/**
 * Listener au clic sur le bouton dessiner
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class BeginFrescoDrawingListener implements View.OnClickListener
{
    private Fresco mFresco;
    private boolean mIsDrawing;

    public BeginFrescoDrawingListener(Fresco fresco)
    {
        mFresco = fresco;
        mIsDrawing = false;
    }

    @Override
    public void onClick(View view)
    {
        /* Si on active le mode dessin */
        if(!mIsDrawing)
        {
            /* On se déplace */
            mFresco.goToLastFragment();

            /* On vérouille le viewPager */
            mFresco.lock();

            /* Affiche les boutons de couleur */
            mFresco.showColorsButtons();

            /* Initialisation du dessin */
            mFresco.initDrawing();
        }
        /* On quitte le mode dessin */
        else
        {
            /* On cache les couleurs */
            mFresco.hideColorsButtons();
        }

        /* On change l'état du bouton */
        mFresco.changeButtonState(Fresco.DRAW_BUTTON, mIsDrawing);

        /* On change l'état */
        mIsDrawing = !mIsDrawing;
    }
}

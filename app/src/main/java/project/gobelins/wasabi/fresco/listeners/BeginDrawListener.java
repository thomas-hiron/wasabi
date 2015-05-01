package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;

/**
 * Listener au clic sur le bouton dessiner
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class BeginDrawListener implements View.OnClickListener
{
    private Fresco mFresco;
    private FrescoActionButton mDrawButton;

    public BeginDrawListener(Fresco fresco, FrescoActionButton drawButton)
    {
        mFresco = fresco;
        mDrawButton = drawButton;
    }

    @Override
    public void onClick(View view)
    {
        /* Si on active le mode dessin */
        if(!mDrawButton.isActive())
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
        mFresco.changeButtonState(Fresco.DRAW_BUTTON, mDrawButton.isActive());

        /* On change l'état */
        mDrawButton.changeState();
    }
}

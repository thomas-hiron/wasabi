package project.gobelins.wasabi.listeners;

import android.view.View;

import project.gobelins.wasabi.Fresco;

/**
 * Listener au clic sur le bouton dessiner
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class BeginFrescoDrawingListener implements View.OnClickListener
{
    private Fresco mFresco;

    public BeginFrescoDrawingListener(Fresco fresco)
    {
        mFresco = fresco;
    }

    @Override
    public void onClick(View view)
    {
        /* On se déplace */
        mFresco.goToLastFragment();

        /* On vérouille le viewPager */
        mFresco.lock();

        /* Initialisation du dessin */
        mFresco.initDrawing();
    }
}

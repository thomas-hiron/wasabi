package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;

/**
 * Created by ThomasHiron on 27/05/2015.
 */
public class GoToLastFragmentListener implements View.OnClickListener
{
    private final Fresco mFresco;

    public GoToLastFragmentListener(Fresco fresco)
    {
        mFresco = fresco;
    }

    @Override
    public void onClick(View view)
    {
        /* Déplacement */
        mFresco.goToLastFragment();
    }
}

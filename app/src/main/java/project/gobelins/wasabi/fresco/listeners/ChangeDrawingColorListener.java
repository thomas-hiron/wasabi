package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.views.ColorButton;
import project.gobelins.wasabi.fresco.views.ColorsView;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class ChangeDrawingColorListener implements View.OnClickListener
{
    private ColorButton mButtonContainer;
    private ColorsView mButtonsContainer;

    @Override
    public void onClick(View view)
    {
        if(mButtonContainer == null)
        {
            mButtonContainer = (ColorButton) view.getParent();
            mButtonsContainer = (ColorsView) mButtonContainer.getParent();
        }

        /* Désactive tous les boutons */
        mButtonsContainer.disableAll();

        /* On active le bouton cliqué */
        mButtonContainer.activate();

        /* Récupération de la vue de dessin */
        Fresco fresco = (Fresco) mButtonContainer.getRootView().findViewById(R.id.fresco_container);

        View fragmentView = fresco.getLastFragment().getView();
        if(fragmentView != null)
        {
            DrawView drawView = fresco.getDrawView();

            /* Changement de la couleur */
            drawView.changeColor(mButtonContainer.getColor());
        }
    }
}
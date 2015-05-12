package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.views.ColorButton;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class ChangeDrawingColorListener implements View.OnClickListener
{
    @Override
    public void onClick(View view)
    {
        /* Récupération de la vue de dessin */
        Fresco fresco = (Fresco) view.getRootView().findViewById(R.id.fresco_container);

        View fragmentView = fresco.getLastFragment().getView();
        if(fragmentView != null)
        {
            DrawView drawView = fresco.getDrawView();

            /* On cast le bouton */
            ColorButton colorButton = (ColorButton) view;

            /* Changement de la couleur */
            drawView.changeColor(colorButton.getColor());
        }
    }
}

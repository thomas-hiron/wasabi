package project.gobelins.wasabi.fresco.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import project.gobelins.wasabi.R;
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
        /* Récupération de la vue parent */
        LinearLayout parent = (LinearLayout) getParent();

        /* Les couleurs */
        View viewById = parent.findViewById(R.id.colors_buttons);
        /* On cache les couleurs */

    }
}

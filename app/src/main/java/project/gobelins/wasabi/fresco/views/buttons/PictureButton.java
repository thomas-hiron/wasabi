package project.gobelins.wasabi.fresco.views.buttons;

import android.content.Context;
import android.util.AttributeSet;

import project.gobelins.wasabi.fresco.views.FrescoActionButton;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class PictureButton extends FrescoActionButton
{
    public PictureButton(Context context)
    {
        super(context);
    }

    public PictureButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Traitement particuliers lorsque le bouton vient d'être désactivé
     */
    @Override
    public void buttonStateDisabled()
    {

    }
}

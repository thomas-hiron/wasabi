package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class SoundButton extends Button
{
    public SoundButton(Context context)
    {
        super(context);
    }

    public SoundButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Animation du bouton */
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        scaleAnimation.setStartTime(200);

        /* Début animation */
        startAnimation(scaleAnimation);
    }
}

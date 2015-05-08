package project.gobelins.wasabi.fresco.listeners;

import android.view.View;
import android.view.animation.Animation;

/**
 * On cache la vue lorsque l'animation est terminée
 * <p/>
 * Created by ThomasHiron on 09/05/2015.
 */
public class AlphaAnimationListener implements Animation.AnimationListener
{
    private View mView;
    private float mTo;

    public AlphaAnimationListener(View view, float to)
    {
        mView = view;
        mTo = to;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        if(mTo == 0)
            mView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}

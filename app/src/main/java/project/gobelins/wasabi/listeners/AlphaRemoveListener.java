package project.gobelins.wasabi.listeners;

import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import project.gobelins.wasabi.views.AccompliceDrawed;

/**
 * Created by ThomasHiron on 08/06/2015.
 */
public class AlphaRemoveListener implements Animation.AnimationListener
{
    private View mViewToRemove;
    private FrameLayout mAppContainer;

    public AlphaRemoveListener(FrameLayout appContainer, View viewToRemove)
    {
        mAppContainer = appContainer;
        mViewToRemove = viewToRemove;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        mAppContainer.removeView(mViewToRemove);
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}

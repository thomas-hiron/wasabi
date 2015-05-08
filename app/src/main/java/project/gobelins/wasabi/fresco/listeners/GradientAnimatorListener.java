package project.gobelins.wasabi.fresco.listeners;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.Animator;

/**
 * Created by ThomasHiron on 08/05/2015.
 */
public class GradientAnimatorListener implements Animator.AnimatorListener
{
    private View mView;

    public GradientAnimatorListener(View view)
    {
        mView = view;
    }

    /**
     * <p>Notifies the start of the animation.</p>
     *
     * @param animation The started animation.
     */
    @Override
    public void onAnimationStart(Animator animation)
    {

    }

    /**
     * <p>Notifies the end of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which reached its end.
     */
    @Override
    public void onAnimationEnd(Animator animation)
    {
        MotionEvent motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_UP, 0, 0, 0
        );

        /* Dispatch touch event to view */
        mView.dispatchTouchEvent(motionEvent);
    }

    /**
     * <p>Notifies the cancellation of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which was canceled.
     */
    @Override
    public void onAnimationCancel(Animator animation)
    {

    }

    /**
     * <p>Notifies the repetition of the animation.</p>
     *
     * @param animation The animation which was repeated.
     */
    @Override
    public void onAnimationRepeat(Animator animation)
    {

    }
}

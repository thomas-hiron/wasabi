package project.gobelins.wasabi.fresco.listeners;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.ValueAnimator;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;

/**
 * Created by ThomasHiron on 03/05/2015.
 */
public class BeginRecordListener implements View.OnTouchListener
{
    private FrameLayout mRevealContainer;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            View rootView = view.getRootView();
            Fresco fresco = (Fresco) rootView.findViewById(R.id.fresco_container);

            /* On cache les éléments */
            fresco.hideInterfaceButtons();
            fresco.hideRecordinContainergView();
            fresco.hideDrawedView();

            /* Le conteneur du dégradé qui sera animé */
            mRevealContainer = (FrameLayout) view.getRootView().findViewById(R.id.record_gradient);

            /* Le dégradé */
            View gradient = mRevealContainer.getChildAt(0);

            /* On récupère la hauteur de l'écran */
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            /* On affiche la vue */
            mRevealContainer.setVisibility(View.VISIBLE);

            /* On change la hauteur du dégradé */
            ViewGroup.LayoutParams params = gradient.getLayoutParams();
            params.height = height;
            gradient.setLayoutParams(params);

            /* On lance l'animation */
            ValueAnimator animator = slideAnimator(0, height);
            animator.setDuration(10000);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            Log.v("test", "Stop recording");
        }

        return false;
    }

    /**
     * Anime la hauteur du conteneur du dégradé
     *
     * @param start La valeur de départ
     * @param end   La hauteur finale
     * @return L'animator
     */
    private ValueAnimator slideAnimator(int start, int end)
    {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mRevealContainer.getLayoutParams();
                layoutParams.height = value;
                mRevealContainer.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }
}
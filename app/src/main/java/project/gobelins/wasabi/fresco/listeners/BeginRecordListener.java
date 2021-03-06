package project.gobelins.wasabi.fresco.listeners;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.ValueAnimator;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.recording.RecordManager;

/**
 * Created by ThomasHiron on 03/05/2015.
 */
public class BeginRecordListener implements View.OnTouchListener
{
    private FrameLayout mRevealContainer;
    private ValueAnimator mAnimator;
    private RecordManager mRecordManager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            View rootView = view.getRootView();
            Fresco fresco = (Fresco) rootView.findViewById(R.id.fresco_container);

            /* On provoque le clic sur le bouton son pour le désactiver et cacher le rond */
            fresco.disableRecordButton();

            /* On remet le listener qui vient d'être supprimé par le fresco.disableRecordButton(); */
            view.setOnTouchListener(this);

            /* On cache les éléments */
            fresco.hideInterfaceButtons();
            fresco.hideDrawedView();
            fresco.hideSoundsView();
            fresco.hideImagesView();

            /* Le conteneur du dégradé qui sera animé */
            mRevealContainer = (FrameLayout) view.getRootView().findViewById(R.id.record_gradient);

            /* Le dégradé */
            View gradient = mRevealContainer.getChildAt(0);
            fresco.showRecordingGradient();

            /* On récupère la hauteur de l'écran */
            int height = Wasabi.SCREEN_HEIGHT;

            /* On affiche la vue */
            mRevealContainer.setVisibility(View.VISIBLE);

            /* On change la hauteur du dégradé */
            ViewGroup.LayoutParams params = gradient.getLayoutParams();
            params.height = height;
            gradient.setLayoutParams(params);

            /* On lance l'enregistrement */
            mRecordManager = new RecordManager();
            mRecordManager.start();

            /* On lance l'animation */
            mAnimator = slideAnimator(0, height);
            mAnimator.setDuration(10000);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();

            /* Ajout listener pour animationEnd */
            mAnimator.addListener(new GradientAnimatorListener(view));
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            View rootView = view.getRootView();
            Fresco fresco = (Fresco) rootView.findViewById(R.id.fresco_container);

            /* On affiche les éléments */
            fresco.hideRecordingGradient();
            fresco.showInterfaceButtons();
            fresco.showDrawedView();
            fresco.showSoundsView();
            fresco.showImagesView();

            /* On supprime les listener */
            view.setOnTouchListener(null);
            mAnimator.removeAllListeners();

            /* On arrête l'anim */
            mAnimator.cancel();

            /* On stoppe l'enregistrement et on ajoute le son */
            if(mRecordManager.stop())
                fresco.addNewSound(mRecordManager.getFileName());
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
                /* On met à jour la hauteur */
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mRevealContainer.getLayoutParams();
                layoutParams.height = value;
                mRevealContainer.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }
}
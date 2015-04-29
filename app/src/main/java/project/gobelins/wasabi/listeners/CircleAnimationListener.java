package project.gobelins.wasabi.listeners;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by ThomasHiron on 29/04/2015.
 */
public class CircleAnimationListener implements View.OnClickListener
{
    private View mView;
    private View mRevealContainer;

    private final int REVEAL_DURATION = 300;

    public CircleAnimationListener(View revealContainer)
    {
        mRevealContainer = revealContainer;
        mView = ((ViewGroup) mRevealContainer).getChildAt(0);
    }

    @Override
    public void onClick(View view)
    {
        /* Suppression du listener */
        view.setOnClickListener(null);

        /* On affiche la vue */
        mRevealContainer.setVisibility(View.VISIBLE);

        /* Le centre du bouton */
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        /* Le rayon final */
        float finalRadius = hypo(mView.getWidth(), mView.getHeight());

        /* On démarre l'animation */
        SupportAnimator reveal = ViewAnimationUtils.createCircularReveal(mView, cx, cy, 0, finalRadius);
        reveal.setInterpolator(new AccelerateDecelerateInterpolator());
        reveal.setDuration(REVEAL_DURATION);
        reveal.start();
    }

    /**
     *
     * @param width La taille
     * @param height La hauteur
     * @return Le rayon de fin
     */
    private float hypo(int width, int height)
    {
        return (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }
}

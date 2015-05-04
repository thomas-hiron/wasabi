package project.gobelins.wasabi.listeners;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * Gère le clic sur le bouton ouvrir fresque ou notif et fermer
 * <p/>
 * Created by ThomasHiron on 29/04/2015.
 */
public class CircleAnimationListener implements View.OnClickListener
{
    private Wasabi mListener;
    private View mView;
    private View mRevealContainer;
    private boolean mClose; /* Animation fermante */

    private final int REVEAL_DURATION = 300;

    /**
     * On ouvre la page
     *
     * @param revealContainer
     */
    public CircleAnimationListener(Wasabi wasabi, View revealContainer)
    {
        mRevealContainer = revealContainer;
        mView = ((ViewGroup) mRevealContainer).getChildAt(0);
        mClose = false;

        mListener = wasabi;
    }

    /**
     * On ferme la page
     *
     * @param revealContainer
     * @param b
     */
    public CircleAnimationListener(Wasabi wasabi, View revealContainer, boolean b)
    {
        mListener = wasabi;
        mRevealContainer = revealContainer;
        mView = ((ViewGroup) mRevealContainer).getChildAt(0);
        mClose = b;
    }

    @Override
    public void onClick(final View view)
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

        SupportAnimator reveal;

        /* On instancie l'animation */
        if(mClose)
            reveal = ViewAnimationUtils.createCircularReveal(mView, cx, cy, finalRadius, 0);
        else
            reveal = ViewAnimationUtils.createCircularReveal(mView, cx, cy, 0, finalRadius);

        /* Ajout du listener sur fermer */
        if(!mClose)
        {
            int id = view.getId() == R.id.open_fresco ? R.id.close_fresco : R.id.close_notification;
            ImageView close = (ImageView) mRevealContainer.findViewById(id);
            close.setOnClickListener(new CircleAnimationListener(mListener, mRevealContainer, true));
        }

        /* Si animation fermante, on supprime la vue lorsque l'animation est terminée */
        reveal.addListener(new SupportAnimator.AnimatorListener()
        {
            @Override
            public void onAnimationStart()
            {

            }

            @Override
            public void onAnimationEnd()
            {
                /* On supprime la vue */
                if(mClose && view.getId() == R.id.close_fresco)
                    mListener.onFrescoClosed();
                else if(mClose && view.getId() == R.id.close_notification)
                    mListener.onNotificationClosed();
                    /* On initialise la fresque */
                else if(view.getId() == R.id.open_fresco)
                    mListener.onFrescoOpened();
                else
                    mListener.onNotificationOpened();
            }

            @Override
            public void onAnimationCancel()
            {

            }

            @Override
            public void onAnimationRepeat()
            {

            }
        });

        /* On paramètre et lance l'animation */
        reveal.setInterpolator(new AccelerateDecelerateInterpolator());
        reveal.setDuration(REVEAL_DURATION);
        reveal.start();
    }

    /**
     * @param width  La taille
     * @param height La hauteur
     * @return Le rayon de fin
     */
    private float hypo(int width, int height)
    {
        return (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }
}
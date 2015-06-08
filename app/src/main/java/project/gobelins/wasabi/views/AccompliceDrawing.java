package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.fresco.views.buttons.DrawButton;

/**
 * Created by ThomasHiron on 08/06/2015.
 */
public class AccompliceDrawing extends FrameLayout
{
    private Wasabi mWasabi;
    private ImageView mValidate;

    public AccompliceDrawing(Context context)
    {
        super(context);
    }

    public AccompliceDrawing(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setWasabi(Wasabi wasabi)
    {
        mWasabi = wasabi;
    }

    /**
     * Initialise la vue
     */
    public void init()
    {
        /* Les vues */
        DrawView drawView = (DrawView) findViewById(R.id.draw_view);
        DrawedView drawedView = (DrawedView) findViewById(R.id.drawed_view);

        drawView.setOnTouchListener(new DrawingListener(drawView, drawedView, this));

        /* Bouton valider */
        mValidate = (ImageView) findViewById(R.id.identikit_completed);

        /* Listener sur le bouton terminer */
        mValidate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mWasabi.removeDrawingAccompliceView();
                mWasabi.addDrawedAccompliceView();
            }
        });

        /* Activation du bouton de dessin */
        DrawButton drawButton = (DrawButton) findViewById(R.id.draw_button);

        /* On met l'état actif */
        TransitionDrawable transition = (TransitionDrawable) drawButton.getBackground();
        transition.startTransition(0);
    }

    /**
     * Enregistre le dessin
     *
     * @param points
     */
    public void saveDrawing(ArrayList<Point> points)
    {
        mWasabi.saveDrawingAccomplice(points);

        /* On affiche le bouton valider */
        if(mValidate.getVisibility() == GONE)
        {
            mValidate.setVisibility(VISIBLE);

            /* Animation */
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);

            mValidate.startAnimation(alphaAnimation);
        }
    }
}

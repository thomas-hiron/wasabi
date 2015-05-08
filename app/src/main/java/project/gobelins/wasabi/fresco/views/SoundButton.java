package project.gobelins.wasabi.fresco.views;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.listeners.PlaySoundListener;
import project.gobelins.wasabi.fresco.listeners.SoundDragListener;

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
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, /* Début/fin pour X/Y */
                Animation.RELATIVE_TO_SELF, 0.5f, /* X */
                Animation.RELATIVE_TO_SELF, 0.5f); /* Y */
        scaleAnimation.setDuration(250);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
        scaleAnimation.setStartTime(200);

        /* Début animation */
        startAnimation(scaleAnimation);

        /* Au clic sur le bouton, on écoute le son */
        setOnClickListener(new PlaySoundListener());
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Initialisation du drag n' drop */
        setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                /* On démarre le drag et on cache la vue */
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                setVisibility(View.GONE);

                return true;
            }
        });

        FrameLayout parent = (FrameLayout) getParent();
        parent.setOnDragListener(new SoundDragListener(this));
    }
}

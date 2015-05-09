package project.gobelins.wasabi.fresco.views;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import project.gobelins.wasabi.fresco.listeners.PlaySoundListener;
import project.gobelins.wasabi.fresco.listeners.SoundDragListener;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class SoundButton extends Button
{
    private String mFileName;

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
    }

    /**
     * @param fileName Le chemin du fichier
     */
    public void setFileName(String fileName)
    {
        mFileName = fileName;
    }

    /**
     * @return Le chemin du fichier
     */
    public String getFileName()
    {
        return mFileName;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Ajout des listeners */
        addListeners();
    }

    /**
     * On supprime tous les listeners
     */
    public void removeListeners()
    {
        setOnClickListener(null);
        setOnLongClickListener(null);
    }

    /**
     * Ajoute les listeners drag et click
     */
    public void addListeners()
    {
        /* Au clic sur le bouton, on écoute le son */
        setOnClickListener(new PlaySoundListener());

        /* Initialisation du drag n' drop au clic long */
        setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                /* Ajout du drag */
                FrameLayout parent = (FrameLayout) getParent();
                parent.setOnDragListener(new SoundDragListener((SoundButton) view));

                /* On démarre le drag et on cache la vue */
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                setVisibility(View.GONE);

                return true;
            }
        });
    }
}

package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import project.gobelins.wasabi.fresco.listeners.SoundDragListener;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ImageButton extends ImageView
{
    private String mFileName;

    public ImageButton(Context context)
    {
        super(context);
    }

    public ImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
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
        setOnLongClickListener(null);
    }

    /**
     * Ajoute les listeners drag et click
     */
    public void addListeners()
    {
        /* Initialisation du drag n' drop au clic long */
        setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                /* Sorte de drag perso */
                setOnTouchListener(new SoundDragListener());

                return true;
            }
        });
    }

    /**
     * Affiche l'image et l'animation
     */
    public void appear()
    {
        /* Ajout du background */
        setImageURI(Uri.parse(mFileName));

        /* Animation du bouton */
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, /* Début/fin pour X/Y */
                Animation.RELATIVE_TO_SELF, 0.5f, /* X */
                Animation.RELATIVE_TO_SELF, 0.5f); /* Y */
        scaleAnimation.setDuration(250);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
        scaleAnimation.setStartTime(500);

        /* Début animation */
        startAnimation(scaleAnimation);
    }
}

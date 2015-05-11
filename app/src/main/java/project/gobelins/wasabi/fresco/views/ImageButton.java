package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.PicassoTarget;
import project.gobelins.wasabi.fresco.listeners.ButtonDragListener;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ImageButton extends CircularImageView
{
    private String mFileName;
    private Target mTarget;

    public ImageButton(Context context)
    {
        super(context);
    }

    public ImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mTarget = new PicassoTarget(this);
    }

    /**
     * @param fileName Le chemin du fichier
     */
    public void setFileName(String fileName)
    {
        mFileName = fileName;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* On charge l'image */
        Picasso.with(getContext()).load(Uri.parse("file:" + mFileName)).into(mTarget);

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
                setOnTouchListener(new ButtonDragListener());

                return true;
            }
        });
    }
}

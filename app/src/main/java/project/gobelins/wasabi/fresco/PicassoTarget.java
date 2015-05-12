package project.gobelins.wasabi.fresco;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import project.gobelins.wasabi.fresco.views.ImageButton;

/**
 * Classe perso lorsque le chargement picasso est terminé
 * <p/>
 * Created by ThomasHiron on 11/05/2015.
 */
public class PicassoTarget implements com.squareup.picasso.Target
{
    private ImageButton mImageButton;
    private boolean mSave;

    public void setSave(boolean save)
    {
        mSave = save;
    }

    public PicassoTarget(ImageButton imageButton)
    {
        mImageButton = imageButton;
    }

    /**
     * Callback when an image has been successfully loaded.
     *
     * @param bitmap
     * @param from
     */
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
    {
        mImageButton.setImageBitmap(bitmap);

        if(mSave)
        {
            /* Animation du bouton */
            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, /* Début/fin pour X/Y */
                    Animation.RELATIVE_TO_SELF, 0.5f, /* X */
                    Animation.RELATIVE_TO_SELF, 0.5f); /* Y */
            scaleAnimation.setDuration(250);
            scaleAnimation.setInterpolator(new OvershootInterpolator());

            /* Début animation */
            mImageButton.startAnimation(scaleAnimation);

            mImageButton.setLayoutParams(new FrameLayout.LayoutParams(mImageButton.getWidth(), mImageButton.getHeight(), Gravity.CENTER));
        }
    }

    /**
     * Callback indicating the image could not be successfully loaded.
     *
     * @param errorDrawable
     */
    @Override
    public void onBitmapFailed(Drawable errorDrawable)
    {

    }

    /**
     * Callback invoked right before your request is submitted.
     *
     * @param placeHolderDrawable
     */
    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable)
    {

    }
}
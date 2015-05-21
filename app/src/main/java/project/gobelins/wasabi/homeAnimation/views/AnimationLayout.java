package project.gobelins.wasabi.homeAnimation.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import project.gobelins.wasabi.R;

/**
 * Gestion de l'animation
 * Created by ThomasHiron on 21/05/2015.
 */
public class AnimationLayout extends FrameLayout
{
    private final int TIMING = 200;
    private int mIndex;
    private int[] mImagesInt;
    private Handler mHandler;
    private Runnable mRunnable;
    private ImageView mImages;

    public AnimationLayout(Context context)
    {
        super(context);
    }

    public AnimationLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mIndex = 1;
        mImagesInt = new int[]{R.drawable.anim1, R.drawable.anim2, R.drawable.anim3,
                R.drawable.anim4, R.drawable.anim5, R.drawable.anim6, R.drawable.anim7};

        mHandler = new Handler();
        mRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    /* Changement du background */
                    mImages.setBackgroundResource(mImagesInt[mIndex]);

                    /* Animation suivante si existante */
                    mHandler.postDelayed(mRunnable, TIMING);

                    /* Incrémentation */
                    ++mIndex;
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();

                    /* On supprime la vue */
                    FrameLayout parent = (FrameLayout) getParent();
                    parent.removeView(parent.getChildAt(0));
                    parent.setBackgroundResource(R.drawable.home);
                }

            }
        };
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        mImages = (ImageView) findViewById(R.id.images_animation);

        /* On joue l'animation */
        mHandler.postDelayed(mRunnable, TIMING);
    }
}

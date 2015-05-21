package project.gobelins.wasabi.homeAnimation.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;
import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.utils.Hypo;

/**
 * Gestion de l'animation
 * Created by ThomasHiron on 21/05/2015.
 */
public class AnimationLayout extends RevealFrameLayout
{
    private final int TIMING = 200;
    private final int REVEAL_DURATION = 1500;

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mImages = (ImageView) findViewById(R.id.images_animation);

        /* Animation du rond */
        if(mImages.getWidth() > 0)
            reveal();
    }

    /**
     * Affiche la vue
     */
    private void reveal()
    {
        int w = Wasabi.SCREEN_WIDTH;
        int h = Wasabi.SCREEN_HEIGHT;

        /* Le rayon final */
        float finalRadius = Hypo.hypo(w / 2, h / 2);

        /* Instanciation et paramétrage de l'anim */
        SupportAnimator reveal = ViewAnimationUtils.createCircularReveal(mImages, w / 2, h / 2, 0, finalRadius);
        reveal.setInterpolator(new AccelerateDecelerateInterpolator());
        reveal.setDuration(REVEAL_DURATION);
        reveal.start();

        /* On joue l'animation avec une bidouille dégueulasse */
        mHandler.postDelayed(mRunnable, REVEAL_DURATION);
    }
}

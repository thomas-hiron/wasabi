package project.gobelins.wasabi.homeAnimation.views;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

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
public class AnimationLayout extends FrameLayout
{
    public AnimationLayout(Context context)
    {
        super(context);
    }

    public AnimationLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* La vidéo */
        VideoView video = (VideoView) findViewById(R.id.video);

        /* Changement de la source et lecture */
        video.setVideoURI(
                Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.home_animation)
        );
        video.start();
    }
}

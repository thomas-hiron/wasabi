package project.gobelins.wasabi.homeAnimation.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * Gestion de l'animation
 * Created by ThomasHiron on 21/05/2015.
 */
public class AnimationLayout extends RelativeLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
{
    private Wasabi mWasabi;
    private VideoView mVideo;

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
        mVideo = (VideoView) findViewById(R.id.video);

        /* Changement de la source et lecture */
        mVideo.setVideoURI(
                Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.home_animation)
        );
        mVideo.setZOrderOnTop(true);
        mVideo.start();

        mVideo.setOnCompletionListener(this);
        mVideo.setOnPreparedListener(this);
    }

    public void setActivity(Wasabi activity)
    {
        mWasabi = activity;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        mWasabi.homeAnimationEnd();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer)
    {
        mVideo.setBackgroundResource(R.drawable.home);
    }
}

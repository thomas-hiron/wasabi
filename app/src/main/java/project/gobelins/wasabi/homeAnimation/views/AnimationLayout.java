package project.gobelins.wasabi.homeAnimation.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.notifications.NotificationsTypes;

/**
 * Gestion de l'animation
 * Created by ThomasHiron on 21/05/2015.
 */
public class AnimationLayout extends RelativeLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
{
    private Wasabi mWasabi;
    private VideoView mVideo;
    private Notification mNotification;

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

        /* La bonne animation */
        int videoId = 0;

        switch(mNotification.getType())
        {
            case NotificationsTypes.CHALLENGES:

                videoId = R.raw.challenge_animation;

                break;

            default:

                videoId = R.raw.home_animation;
        }

        /* Changement de la source et lecture */
        mVideo.setVideoURI(
                Uri.parse("android.resource://" + getContext().getPackageName() + "/" + videoId)
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

    public void setNotification(Notification notification)
    {
        mNotification = notification;
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

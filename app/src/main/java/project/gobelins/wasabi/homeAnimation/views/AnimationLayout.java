package project.gobelins.wasabi.homeAnimation.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
        int videoId = R.raw.home_animation;

        /* Répération des sharedPref pour savoir si la vidéo a été lue */
        SharedPreferences prefs = getContext().getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);
        boolean customAnimNotPlayed = prefs.getBoolean(Wasabi.CUSTOM_ANIM_NOT_PLAYED, false);

        /* Pour prévenir les bugs ou si la vidéo a déjà été jouée */
        if(mNotification == null || !customAnimNotPlayed)
            mNotification = new Notification();

        switch(mNotification.getType())
        {
            case NotificationsTypes.CHALLENGES:

                videoId = R.raw.challenge_animation;

                break;

            case NotificationsTypes.PLACES:

                videoId = R.raw.gps_animation;

                break;

            case NotificationsTypes.MESSAGES:

                videoId = R.raw.message_animation;

                break;

            case NotificationsTypes.CUSTOM_IMAGE:

                videoId = R.raw.custom_image_animation;

                break;
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
        /* On change le fond de la vidéo */
        switch(mNotification.getType())
        {
            case NotificationsTypes.CHALLENGES:
            case NotificationsTypes.PLACES:
            case NotificationsTypes.MESSAGES:
            case NotificationsTypes.CUSTOM_IMAGE:

                mVideo.setBackgroundColor(Color.WHITE);

                break;

            default:

                mVideo.setBackgroundResource(R.drawable.home);

                break;
        }
    }
}

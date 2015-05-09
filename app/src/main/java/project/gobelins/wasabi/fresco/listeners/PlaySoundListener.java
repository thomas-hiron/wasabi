package project.gobelins.wasabi.fresco.listeners;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import java.io.IOException;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.MediaPlayer;
import project.gobelins.wasabi.fresco.views.SoundButton;

/**
 * Joue le son
 * <p/>
 * Created by ThomasHiron on 09/05/2015.
 */
public class PlaySoundListener implements View.OnClickListener
{
    private Fresco mFresco;

    @Override
    public void onClick(View view)
    {
        /* Récupération de la fresque */
        if(mFresco == null)
        {
            View rootView = view.getRootView();
            mFresco = (Fresco) rootView.findViewById(R.id.fresco_container);
        }

        /* On cast le bouton pour récupérer le nom du fichier */
        SoundButton soundButton = (SoundButton) view;

        /* Animation du bouton */
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.95f, 1, 0.95f, 1, /* Début/fin pour X/Y */
                Animation.RELATIVE_TO_SELF, 0.5f, /* X */
                Animation.RELATIVE_TO_SELF, 0.5f); /* Y */
        scaleAnimation.setDuration(250);
        scaleAnimation.setInterpolator(new OvershootInterpolator());

        /* Début animation */
        soundButton.startAnimation(scaleAnimation);

        /* Instanciation et lecture du fichier audio */
        try
        {
            /* Récupération du player */
            MediaPlayer mediaPlayer = mFresco.getMediaPlayer();

            /* Réinitialisation */
            mediaPlayer.stop();

            /* Nouvelle piste */
            mediaPlayer.setDataSource(soundButton.getContext(), Uri.parse(soundButton.getFileName()));
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch(IOException e)
        {
            Context c = soundButton.getContext();
            Toast.makeText(c, c.getString(R.string.error_play_sound), Toast.LENGTH_SHORT).show();
        }
    }
}
package project.gobelins.wasabi.fresco.listeners;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
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
package project.gobelins.wasabi.fresco.listeners;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.views.SoundButton;

/**
 * Joue le son
 * <p/>
 * Created by ThomasHiron on 09/05/2015.
 */
public class PlaySoundListener implements View.OnClickListener
{
    @Override
    public void onClick(View view)
    {
        /* On cast le bouton pour récupérer le nom du fichier */
        SoundButton soundButton = (SoundButton) view;

        /* Instanciation et lecture du fichier audio */
        try
        {
            Log.v("test", soundButton.getFileName());
            MediaPlayer mediaPlayer = new MediaPlayer();
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
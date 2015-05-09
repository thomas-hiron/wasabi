package project.gobelins.wasabi.fresco;

import android.util.Log;

/**
 * MediaPlayer perso pour ajouter le listener
 *
 * Created by ThomasHiron on 09/05/2015.
 */
public class MediaPlayer extends android.media.MediaPlayer implements android.media.MediaPlayer.OnCompletionListener
{
    @Override
    public void start() throws IllegalStateException
    {
        super.start();

        setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(android.media.MediaPlayer mediaPlayer)
    {
        setOnCompletionListener(null);

        stop();
    }

    @Override
    public void stop() throws IllegalStateException
    {
        super.stop();

        reset();
    }
}

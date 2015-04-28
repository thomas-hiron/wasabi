package project.gobelins.wasabi.notifications.utils;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Détecte l'amplitude du son
 * Created by ThomasHiron on 28/04/2015.
 */
public class SoundMeter
{
    private MediaRecorder mRecorder = null;

    /**
     * Démarre l'enregistrement sans stocker le fichier sur le téléphone
     */
    public void start()
    {
        if(mRecorder == null)
        {
            try
            {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                mRecorder.prepare();
                mRecorder.start();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stoppe l'enregistrement
     */
    public void stop()
    {
        if(mRecorder != null)
        {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * @return L'amplitude du son courant
     */
    public double getAmplitude()
    {
        if(mRecorder != null)
            return mRecorder.getMaxAmplitude();
        else
            return 0;

    }
}

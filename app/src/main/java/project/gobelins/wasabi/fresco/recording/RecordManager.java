package project.gobelins.wasabi.fresco.recording;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by ThomasHiron on 09/05/2015.
 */
public class RecordManager
{
    private String mFileName;
    private MediaRecorder mRecorder;

    public RecordManager()
    {
        /* Le dossier de base */
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Wasabi/sounds/";

        /* Création si non existant */
        File folder = new File(mFileName);
        if(!folder.exists())
            folder.mkdirs();

        /* Formatage du fichier */
        Date date = new Date();
        mFileName += date.getTime() + ".3gp";
    }

    /**
     * Démarre l'enregistrement
     */
    public void start()
    {
        /* Instanciation et initialisation des paramètres */
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            mRecorder.prepare();
        }
        catch(IOException e)
        {
            Log.v("test", "Recording failed");
        }
        catch(IllegalStateException e) /* Si on supprime un dossier, erreur */
        {
            Log.v("test", "Recording failed");
        }

        /* Démarrage de l'enregistrement */
        mRecorder.start();
    }

    /**
     * Stoppe l'enregistrement
     */
    public void stop()
    {
        /* Suppression de l'enregistrement en cours */
        if(mRecorder != null)
        {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * @return Le fichier enregistré
     */
    public String getFileName()
    {
        return mFileName;
    }
}

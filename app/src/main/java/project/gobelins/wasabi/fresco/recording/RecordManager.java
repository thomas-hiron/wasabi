package project.gobelins.wasabi.fresco.recording;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import project.gobelins.wasabi.utils.DateFormater;

/**
 * Created by ThomasHiron on 09/05/2015.
 */
public class RecordManager
{
    private File mFile;
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
        mFileName += DateFormater.getToday().getTime() + ".3gp";
        mFile = new File(mFileName);
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
        mRecorder.setOutputFile(mFile.getAbsolutePath());
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
     *
     * @return Si enregistrement effectué avec succès
     */
    public boolean stop()
    {
        boolean success = true;
        /* Suppression de l'enregistrement en cours */
        if(mRecorder != null)
        {
            try
            {
                mRecorder.stop();
            }
            catch(RuntimeException e)
            {
                /* Suppression du fichier si simple click sur le bouton */
                mFile.delete();
                success = false;
            }
            finally
            {
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }
        }

        return success;
    }

    /**
     * @return Le fichier enregistré
     */
    public String getFileName()
    {
        return mFileName;
    }
}

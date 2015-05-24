package project.gobelins.wasabi.fresco.recording;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import project.gobelins.wasabi.utils.AudioRecorder;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Created by ThomasHiron on 09/05/2015.
 */
public class RecordManager
{
    private File mFile;
    private String mFileName;
    private AudioRecorder mRecorder;

    public RecordManager()
    {
        /* Le dossier de base */
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Wasabi/sounds/";

        /* Création si non existant */
        File folder = new File(mFileName);
        if(!folder.exists())
            folder.mkdirs();

        /* Formatage du fichier */
        mFileName += DateFormater.getNow().getTime() + ".wav";
        mFile = new File(mFileName);
    }

    /**
     * Démarre l'enregistrement
     */
    public void start()
    {
        mRecorder = AudioRecorder.getInstance(false);

        mRecorder.setOutputFile(mFile.getAbsolutePath());
        mRecorder.prepare();
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

package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class RecordAudioListener implements View.OnClickListener
{
    private Fresco mFresco;
    private boolean mIsRecording;

    public RecordAudioListener(Fresco fresco)
    {
        mFresco = fresco;
        mIsRecording = false;
    }

    @Override
    public void onClick(View view)
    {
        /* On change l'état du bouton */
        mFresco.changeButtonState(Fresco.RECORD_BUTTON, mIsRecording);

        /* On change l'état */
        mIsRecording = !mIsRecording;
    }
}

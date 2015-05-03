package project.gobelins.wasabi.fresco.listeners;

import android.view.View;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class RecordAudioListener implements View.OnClickListener
{
    private Fresco mFresco;
    private FrescoActionButton mRecordButton;

    public RecordAudioListener(Fresco fresco, FrescoActionButton recordButton)
    {
        mFresco = fresco;
        mRecordButton = recordButton;
    }

    @Override
    public void onClick(View view)
    {
        /* Si on active le mode dessin */
        if(!mRecordButton.isActive())
        {
            /* On se déplace */
            mFresco.goToLastFragment();

            /* On vérouille le viewPager */
            mFresco.lock();

            /* On affiche la vue */
            mFresco.showRecordView();

            /* On ajoute le listener */
            mFresco.getLastFragment().addRecordingListener();
        }

        /* On change l'état */
        mRecordButton.changeState();

        /* On change l'état du bouton */
        mFresco.changeButtonState(Fresco.RECORD_BUTTON, mRecordButton.isActive());
    }
}

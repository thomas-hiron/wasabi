package project.gobelins.wasabi.notifications.views;

import android.content.Context;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class ChallengeView extends MyLayout
{
    public ChallengeView(Context context)
    {
        super(context);

        inflate(context, R.layout.challenge_view, this);
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {

    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {

    }
}

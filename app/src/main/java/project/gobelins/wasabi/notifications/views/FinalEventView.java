package project.gobelins.wasabi.notifications.views;

import android.content.Context;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.views.RoundGradientGpsView;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class FinalEventView extends MyLayout
{
    private RoundGradientGpsView mCounter;

    public FinalEventView(Context context)
    {
        super(context);

        inflate(context, R.layout.final_event_view, this);
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

package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.view.View;

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

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* On désactive le bouton retour */
        View closeNotification = getRootView().findViewById(R.id.close_notification);
        closeNotification.setVisibility(GONE);
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

package project.gobelins.wasabi.notifications.views;

import android.content.Context;

import project.gobelins.wasabi.R;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class GPSView extends MyLayout
{
    public GPSView(Context context)
    {
        super(context);

        inflate(context, R.layout.gps_view, this);
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

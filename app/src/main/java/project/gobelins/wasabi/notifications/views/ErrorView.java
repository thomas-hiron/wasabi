package project.gobelins.wasabi.notifications.views;

import android.content.Context;

import project.gobelins.wasabi.R;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class ErrorView extends MyLayout
{
    public ErrorView(Context context)
    {
        super(context);

        inflate(context, R.layout.error_view, this);
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

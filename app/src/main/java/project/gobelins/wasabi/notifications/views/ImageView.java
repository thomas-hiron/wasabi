package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.notifications.utils.SoundMeter;

/**
 * Affiche un message à l'écran, si caché, prend aléatoirement entre chant et toucher
 * Created by ThomasHiron on 28/04/2015.
 */
public class ImageView extends MyLayout
{
    public ImageView(Context context)
    {
        super(context);

        inflate(context, R.layout.image_view, this);
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
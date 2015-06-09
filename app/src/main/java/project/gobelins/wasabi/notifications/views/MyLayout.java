package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Classe abstraite permettant d'avoir une méthode commune lors du slide terminé
 *
 * Created by ThomasHiron on 29/04/2015.
 */
public abstract class MyLayout extends LinearLayout
{
    protected boolean mDisplayCloseNotification = true;

    public MyLayout(Context context)
    {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    public abstract void initialize();

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    public abstract void stop();

    public boolean getAnimateNotificationButton()
    {
        return mDisplayCloseNotification;
    }
}

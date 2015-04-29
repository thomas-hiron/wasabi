package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Classe abstraite permettant d'avoir une méthode commune lors du slide terminé
 *
 * Created by ThomasHiron on 29/04/2015.
 */
public abstract class MyLayout extends LinearLayout
{
    public MyLayout(Context context)
    {
        super(context);
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    public abstract void initialize();
}

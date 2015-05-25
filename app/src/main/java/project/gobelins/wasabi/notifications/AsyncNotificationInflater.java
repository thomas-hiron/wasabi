package project.gobelins.wasabi.notifications;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.notifications.views.ChallengeView;
import project.gobelins.wasabi.notifications.views.MessageView;
import project.gobelins.wasabi.notifications.views.MyLayout;

/**
 * Created by ThomasHiron on 25/05/2015.
 */
public class AsyncNotificationInflater extends AsyncTask
{
    private Wasabi mWasabi;
    private View mRevealContainerNotification;
    private Notification mLastNotification;
    private MyLayout mCustomView;

    public AsyncNotificationInflater(Wasabi wasabi, Notification lastNotification, View revealContainerNotification)
    {
        mWasabi = wasabi;
        mLastNotification = lastNotification;
        mRevealContainerNotification = revealContainerNotification;
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        /* Récupération du premier enfant */
        FrameLayout child = (FrameLayout) ((ViewGroup) mRevealContainerNotification).getChildAt(0);

        /* En fonction de la notification */
        switch(mLastNotification.getType())
        {
            /* Une image */
            case NotificationsTypes.IMAGES:

                /* Création de la vue */
                mCustomView = new project.gobelins.wasabi.notifications.views.ImageView(mWasabi.getApplicationContext());

                break;

            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                /* Création de la vue */
                mCustomView = new MessageView(mWasabi.getApplicationContext());

                break;

            default:

                mCustomView = new ChallengeView(mWasabi.getApplicationContext());

                break;
        }

        /* Ajout du message à la vue */
        child.addView(mCustomView);

        mWasabi.setCustomView(mCustomView);

        return null;
    }
}

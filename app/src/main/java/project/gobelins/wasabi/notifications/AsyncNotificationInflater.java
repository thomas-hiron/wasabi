package project.gobelins.wasabi.notifications;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.notifications.views.ChallengeView;
import project.gobelins.wasabi.notifications.views.ErrorView;
import project.gobelins.wasabi.notifications.views.FacebookView;
import project.gobelins.wasabi.notifications.views.FinalEventView;
import project.gobelins.wasabi.notifications.views.GPSView;
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

            /* GPS vers destination inconnue */
            case NotificationsTypes.PLACES:

                /* Création de la vue */
                mCustomView = new GPSView(mWasabi);

                break;

            /* Event Facebook */
            case NotificationsTypes.FACEBOOKEVENTS:

                /* Création de la vue */
                mCustomView = new FacebookView(mWasabi.getApplicationContext());

                break;

            /* Challenge de fin */
            case NotificationsTypes.CHALLENGES:

                /* Création de la vue */
                mCustomView = new ChallengeView(mWasabi.getApplicationContext());

                break;

            /* Evénement final */
            case NotificationsTypes.FINALEVENTS:

                /* Création de la vue */
                mCustomView = new FinalEventView(mWasabi.getApplicationContext());

                break;

            default:

                mCustomView = new ErrorView(mWasabi.getApplicationContext());

                break;
        }

        mWasabi.setCustomView(mCustomView);

        return null;
    }
}

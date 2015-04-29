package project.gobelins.wasabi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import project.gobelins.wasabi.interfaces.OnFrescoClosed;
import project.gobelins.wasabi.interfaces.OnFrescoOpened;
import project.gobelins.wasabi.interfaces.OnNotificationClosed;
import project.gobelins.wasabi.interfaces.OnNotificationOpened;
import project.gobelins.wasabi.listeners.CircleAnimationListener;
import project.gobelins.wasabi.notifications.NotificationsManager;
import project.gobelins.wasabi.notifications.NotificationsTypes;
import project.gobelins.wasabi.notifications.views.MessageView;
import project.gobelins.wasabi.notifications.views.MyLayout;

public class Wasabi extends FragmentActivity implements OnFrescoOpened, OnFrescoClosed, OnNotificationOpened, OnNotificationClosed
{
    public final static String TAG = "Wasabi";

    private NotificationsManager mNotificationsManager;
    private FrameLayout mAppContainer;
    private View mRevealContainerFresco;
    private View mRevealContainerNotification;
    private Button mFrescoButton;
    private Button mNotificationButton;
    private MyLayout mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Instanciation du manager des notifications */
        mNotificationsManager = new NotificationsManager(getContentResolver());

        /* On envoie le registration_id si première connexion */
        RegistrationIdManager registrationIdManager = new RegistrationIdManager(this);
        registrationIdManager.getRegistrationID();

        /* Ajout de la vue */
        setContentView(R.layout.activity_wasabi);

        /* Ajout des listeners d'animation */
        mFrescoButton = (Button) findViewById(R.id.fresco);
        mNotificationButton = (Button) findViewById(R.id.notification);

        /* L'élément racine de la vue de l'application */
        mAppContainer = (FrameLayout) findViewById(R.id.app_container);

        /* Les conteneurs qui vont s'ouvrir */
        mRevealContainerFresco = getLayoutInflater().inflate(R.layout.fresco, mAppContainer, false);
        mRevealContainerNotification = getLayoutInflater().inflate(R.layout.notification, mAppContainer, false);

        /* Ajout des conteneurs à la vue de l'application */
        mAppContainer.addView(mRevealContainerFresco);
        mAppContainer.addView(mRevealContainerNotification);

        mFrescoButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerFresco));
        mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));
    }

    /**
     * On cache la navigation bar et la notification bare
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && android.os.Build.VERSION.SDK_INT >= 19) // Mode immersif
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * La fresque a été ouverte
     */
    @Override
    public void onFrescoOpened()
    {

    }

    /**
     * La fresque a été fermée
     */
    @Override
    public void onFrescoClosed()
    {
        /* On remet le listener */
        mFrescoButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerFresco));

        /* On cache la vue */
        mRevealContainerFresco.setVisibility(View.INVISIBLE);
    }

    /**
     * La notification a été ouverte
     */
    @Override
    public void onNotificationOpened()
    {
        /* Récupération du premier enfant */
        FrameLayout child = (FrameLayout) ((ViewGroup) mRevealContainerNotification).getChildAt(0);
        int id = 6;

        /* En fonction de la notification */
        switch(id)
        {
            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                /* Création de la vue */
                mCustomView = new MessageView(this);

                /* Ajout du message à la vue */
                child.addView(mCustomView);

                mCustomView.initialize();

                break;

            default:

                break;
        }
    }

    /**
     * La notification a été fermée
     */
    @Override
    public void onNotificationClosed()
    {
        /* On remet le listener */
        mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));

        /* On cache la vue */
        mRevealContainerNotification.setVisibility(View.INVISIBLE);

        int id = 6;

        /* Selon les notifs, on stoppe le processus (vidéo, son,...) */
        switch(id)
        {
            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                mCustomView.stop();

                break;

            default:

                break;
        }
    }
}
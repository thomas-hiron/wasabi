package project.gobelins.wasabi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.interfaces.OnFrescoClosed;
import project.gobelins.wasabi.interfaces.OnFrescoOpened;
import project.gobelins.wasabi.interfaces.OnNotificationClosed;
import project.gobelins.wasabi.interfaces.OnNotificationOpened;
import project.gobelins.wasabi.interfaces.OnPictureListener;
import project.gobelins.wasabi.listeners.CircleAnimationListener;
import project.gobelins.wasabi.notifications.NotificationsManager;
import project.gobelins.wasabi.notifications.NotificationsTypes;
import project.gobelins.wasabi.notifications.views.MessageView;
import project.gobelins.wasabi.notifications.views.MyLayout;

public class Wasabi extends FragmentActivity implements OnFrescoOpened, OnFrescoClosed, OnNotificationOpened,
        OnNotificationClosed, OnPictureListener
{
    public final static String TAG = "Wasabi";
    private final int REQUEST_IMAGE = 1;

    private NotificationsManager mNotificationsManager;
    private FrameLayout mAppContainer;
    private View mRevealContainerFresco;
    private View mRevealContainerNotification;
    private ImageView mFrescoButton;
    private ImageView mNotificationButton;
    private MyLayout mCustomView;
    private Notification mLastNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Ajout de la vue */
        setContentView(R.layout.activity_wasabi);

        /* Instanciation du manager des notifications */
        mNotificationsManager = new NotificationsManager(getContentResolver());

        /* Récupération de la dernière notification */
        mLastNotification = mNotificationsManager.getLast();

        /* On envoie le registration_id si première connexion */
        RegistrationIdManager registrationIdManager = new RegistrationIdManager(this);
        registrationIdManager.getRegistrationID();

        /* Ajout des listeners d'animation */
        mFrescoButton = (ImageView) findViewById(R.id.open_fresco);
        mNotificationButton = (ImageView) findViewById(R.id.open_notification);

        /* L'élément racine de la vue de l'application */
        mAppContainer = (FrameLayout) findViewById(R.id.app_container);

        /* La fresque toujours présente (inflate, ajout de la vue et du listener) */
        mRevealContainerFresco = getLayoutInflater().inflate(R.layout.fresco, mAppContainer, false);
        mAppContainer.addView(mRevealContainerFresco);
        mFrescoButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerFresco));

        /* On initialise la fresque et le viewPager */
        Fresco fresco = (Fresco) mRevealContainerFresco.findViewById(R.id.fresco_container);
        fresco.initViewPager(getSupportFragmentManager());
        fresco.setPictureListener(this);

        /* La notif si != null (inflate, ajout de la vue et du listener) */
        if(mLastNotification != null)
        {
            mRevealContainerNotification = getLayoutInflater().inflate(R.layout.notification, mAppContainer, false);
            mAppContainer.addView(mRevealContainerNotification);
            mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));
        }
        /* Sinon on cache le bouton */
        else
            mNotificationButton.setVisibility(View.GONE);
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

        /* En fonction de la notification */
        switch(mLastNotification.getType())
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

        /* Récupération du premier enfant pour supprimer la customView */
        FrameLayout child = (FrameLayout) ((ViewGroup) mRevealContainerNotification).getChildAt(0);
        child.removeView(mCustomView);
    }

    /**
     * On a cliqué sur le bouton prendre une photo
     */
    @Override
    public void onTakePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /* Photo bien prise */
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
        }
    }
}
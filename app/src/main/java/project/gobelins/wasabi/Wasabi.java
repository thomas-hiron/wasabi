package project.gobelins.wasabi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import project.gobelins.wasabi.listeners.CircleAnimationListener;
import project.gobelins.wasabi.notifications.NotificationsManager;

public class Wasabi extends FragmentActivity
{
    public final static String TAG = "Wasabi";

    private NotificationsManager mNotificationsManager;
    private FrameLayout mAppContainer;
    private View mRevealContainer;

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
        Button frescoButton = (Button) findViewById(R.id.fresco);
        Button notificationButton = (Button) findViewById(R.id.notification);

        /* L'élément racine de la vue de l'application */
        mAppContainer = (FrameLayout) findViewById(R.id.app_container);
        /* Le conteneur qui va s'ouvrir */
        mRevealContainer = getLayoutInflater().inflate(R.layout.fresco, mAppContainer, false);
        /* Ajout du conteneur à la vue de l'application */
        mAppContainer.addView(mRevealContainer);

        frescoButton.setOnClickListener(new CircleAnimationListener(mRevealContainer));
        notificationButton.setOnClickListener(new CircleAnimationListener(mRevealContainer));
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
}
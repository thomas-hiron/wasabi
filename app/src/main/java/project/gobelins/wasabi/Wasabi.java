package project.gobelins.wasabi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.fragments.NotificationFragment;
import project.gobelins.wasabi.interfaces.OnNextNotificationListener;
import project.gobelins.wasabi.interfaces.OnPreviousNotificationListener;
import project.gobelins.wasabi.notifications.RegistrationIdManager;
import project.gobelins.wasabi.viewPager.MyViewPager;
import project.gobelins.wasabi.viewPager.ViewPagerAdapter;

public class Wasabi extends FragmentActivity implements OnNextNotificationListener, OnPreviousNotificationListener
{
    public final static String TAG = "Wasabi";

    private MyViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private NotificationsManager mNotificationsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Instanciation du manager des notifications */
        mNotificationsManager = new NotificationsManager(getContentResolver());

        /* On envoie le registration_id si première connexion */
        RegistrationIdManager registrationIdManager = new RegistrationIdManager(this);
        registrationIdManager.getRegistrationID();

        /* Récupération des notifications */
        ArrayList<Notification> notifications = mNotificationsManager.getNotifications();

        /* On a des notifications */
        if(notifications.size() > 0)
        {
            /* Création du viewPager */
            mViewPager = new MyViewPager(this);
            mViewPager.setId(R.id.appContainer);

            /* Ajout de l'adapter au viewPager */
            mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

            /* Ajout des notifications */
            for(Notification notification : notifications)
            {
                NotificationFragment notificationFragment = NotificationFragment.newInstance(notification);
                mViewPagerAdapter.add(notificationFragment);
            }

            /* Ajout de l'adapter */
            mViewPager.setAdapter(mViewPagerAdapter);

            /* On cache le bouton précédent du premier fragment et suivant du dernier */
            ((NotificationFragment) mViewPagerAdapter.getItem(0)).setHidePrevious(true);
            ((NotificationFragment) mViewPagerAdapter.getItem(mViewPagerAdapter.getCount() - 1)).setHideNext(true);

            /* On marque le premier comme lu */
            mNotificationsManager.markRead(0);

            /* Affichage du viewPager */
            setContentView(mViewPager);
        }
        /* Aucune notification */
        else
            setContentView(R.layout.activity_wasabi);
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
     * Au clic sur le bouton notification suivant on déplace le viewPager
     */
    @Override
    public void onNextNotificationListener()
    {
        int nextItem = mViewPager.getCurrentItem() + 1;
        mViewPager.setCurrentItem(nextItem, true);

        /* On marque la notif comme lue */
        Notification notification = mNotificationsManager.get(nextItem);

        if(!notification.isRead())
            mNotificationsManager.markRead(nextItem);
    }

    /**
     * Au clic sur le bouton notification précédente
     */
    @Override
    public void onPreviousNotificationListener()
    {
        int prevItem = mViewPager.getCurrentItem() - 1;
        mViewPager.setCurrentItem(prevItem, true);
    }
}
package project.gobelins.wasabi;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.fragments.NotificationFragment;
import project.gobelins.wasabi.interfaces.OnNextNotificationListener;
import project.gobelins.wasabi.interfaces.OnPreviousNotificationListener;
import project.gobelins.wasabi.notifications.RegistrationIdManager;
import project.gobelins.wasabi.sqlite.tables.Notifications;
import project.gobelins.wasabi.viewPager.MyViewPager;
import project.gobelins.wasabi.viewPager.ViewPagerAdapter;

public class Wasabi extends FragmentActivity implements OnNextNotificationListener, OnPreviousNotificationListener
{
    public final static String TAG = "Wasabi";

    private MyViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* On envoie le registration_id si première connexion */
        RegistrationIdManager registrationIdManager = new RegistrationIdManager(this);
        registrationIdManager.getRegistrationID();

        /* Création du viewPager */
        mViewPager = new MyViewPager(this);
        mViewPager.setId(R.id.appContainer);

        /* Ajout de l'adapter au viewPager */
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        /* Affichage du viewPager */
        setContentView(mViewPager);

        /* Les valeurs à insérer */
        ContentValues values = new ContentValues();

        /* Ajout de la date */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        /* Ajout des valeurs */
        values.put(Notifications.NOTIFICATIONS_READ, 0);
        values.put(Notifications.NOTIFICATIONS_TYPE, 5);
        values.put(Notifications.NOTIFICATIONS_RECEIVED_DATE, dateFormat.format(date));

        /* Insertion des données */
//        getContentResolver().insert(Notifications.CONTENT_URI_NOTIFICATIONS, values);

        /* Tous les messages non lus */
        String readCondition = Notifications.NOTIFICATIONS_READ + " = 0";
        String sortOrder = Notifications.NOTIFICATIONS_ID + " DESC";

        Cursor c = getContentResolver().query(Uri.parse(Notifications.URL_NOTIFICATIONS), null, readCondition, null, sortOrder);

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Notification notification = new Notification();
                notification.setId(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_ID)));
                notification.setRead(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_READ)));
                notification.setType(c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_TYPE)));
                notification.setDate(c.getString(c.getColumnIndex(Notifications.NOTIFICATIONS_RECEIVED_DATE)));

                /* Instanciation du fragment */
                NotificationFragment notificationFragment = NotificationFragment.newInstance(notification);

                mViewPagerAdapter.add(notificationFragment);
            }
            while(c.moveToNext());
        }

        mViewPager.setAdapter(mViewPagerAdapter);

        /* On cache le bouton précédent du premier fragment et suivant du dernier */
        ((NotificationFragment) mViewPagerAdapter.getItem(0)).setHidePrevious(true);
        ((NotificationFragment) mViewPagerAdapter.getItem(mViewPagerAdapter.getCount() - 1)).setHideNext(true);

        /* On marque le premier comme lu */

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
     * Au clic sur le bouton notification suivanten on déplace le viewPager
     */
    @Override
    public void onNextNotificationListener()
    {
        int nextItem = mViewPager.getCurrentItem() + 1;
        mViewPager.setCurrentItem(nextItem, true);
    }

    /**
     * Au clic sur le bouton notification suivante
     */
    @Override
    public void onPreviousNotificationListener()
    {
        int prevItem = mViewPager.getCurrentItem() - 1;
        mViewPager.setCurrentItem(prevItem, true);
    }
}
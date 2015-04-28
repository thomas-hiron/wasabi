package project.gobelins.wasabi;

import android.content.ContentResolver;
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

    private NotificationsManager mNotificationsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Instanciation du manager des notifs */
        mNotificationsManager = new NotificationsManager();

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
        DateFormat dateFormat = new SimpleDateFormat(NotificationsManager.DATE_FORMAT);
        Date date = new Date();

        /* Ajout des valeurs */
        values.put(Notifications.NOTIFICATIONS_READ, 0);
        values.put(Notifications.NOTIFICATIONS_TYPE, 5);
        values.put(Notifications.NOTIFICATIONS_RECEIVED_DATE, dateFormat.format(date));

        /* Insertion des données */
//        getContentResolver().insert(Notifications.CONTENT_URI_NOTIFICATIONS, values);



        ContentResolver contentResolver = getContentResolver();

        /* Insertion de la nouvelle valeur modifiée */
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(Notifications.NOTIFICATIONS_READ, 0);

        /* La condition d'update */
//        String condition = Notifications.NOTIFICATIONS_ID + " = " + notification.getId();

        /* Exécution de la requête */
//        contentResolver.update(Uri.parse(Notifications.URL_NOTIFICATIONS), contentValues, null, null);

        /* Tous les messages non lus ou date égale à aujourd'hui */
        String readCondition = Notifications.NOTIFICATIONS_READ + " = 0 OR " + Notifications.NOTIFICATIONS_RECEIVED_DATE + " = '" + dateFormat.format(date) + "'";
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

                /* Ajout de la notif au manager */
                mNotificationsManager.add(notification);

                /* Instanciation du fragment */
                NotificationFragment notificationFragment = NotificationFragment.newInstance(notification);

                mViewPagerAdapter.add(notificationFragment);
            }
            while(c.moveToNext());
        }

        /* Ajout de l'adapter */
        mViewPager.setAdapter(mViewPagerAdapter);

        /* Des notifs, on traite les boutons suivant/précédent et on marque la première comme lue */
        if(mViewPagerAdapter.getCount() > 0)
        {
            /* On cache le bouton précédent du premier fragment et suivant du dernier */
            ((NotificationFragment) mViewPagerAdapter.getItem(0)).setHidePrevious(true);
            ((NotificationFragment) mViewPagerAdapter.getItem(mViewPagerAdapter.getCount() - 1)).setHideNext(true);

            /* On marque le premier comme lu */
            mNotificationsManager.markRead(this, 0);
        }
        /* Aucune notification */
        else
        {
            int yeah = 0;
        }
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
            mNotificationsManager.markRead(this, nextItem);
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
package project.gobelins.wasabi;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import project.gobelins.wasabi.fragments.NotificationFragment;
import project.gobelins.wasabi.notifications.RegistrationIdManager;
import project.gobelins.wasabi.sqlite.tables.Notifications;
import project.gobelins.wasabi.viewPager.MyViewPager;
import project.gobelins.wasabi.viewPager.ViewPagerAdapter;

public class Wasabi extends FragmentActivity
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

//        /* Les valeurs à insérer */
//        ContentValues values = new ContentValues();
//
//        /* Ajout des valeurs */
//        values.put(Notifications.NOTIFICATIONS_READ, 0);
//        values.put(Notifications.NOTIFICATIONS_TYPE, 2);
//
//        getContentResolver().insert(Notifications.CONTENT_URI_NOTIFICATIONS, values);

        Cursor c = getContentResolver().query(Uri.parse(Notifications.URL_NOTIFICATIONS), null, null, null, null);

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation du fragment */
                NotificationFragment notificationFragment = NotificationFragment.newInstance(
                        c.getInt(c.getColumnIndex(Notifications.NOTIFICATIONS_TYPE))
                );

                mViewPagerAdapter.add(notificationFragment);
            }
            while(c.moveToNext());
        }

        mViewPager.setAdapter(mViewPagerAdapter);
    }


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
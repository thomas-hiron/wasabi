package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Base64;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.gps.GeolocationManager;
import project.gobelins.wasabi.httpRequests.AsyncPostRequests;

/**
 * La vue du GPS
 * <p/>
 * Created by ThomasHiron on 24/05/2015.
 */
public class GPSView extends MyLayout implements SensorEventListener, Animation.AnimationListener, View.OnClickListener
{
    private final int ROTATION_DURATION = 750;

    private GeolocationManager mGeolocation;
    private Location mCurrentLocation;
    private android.widget.ImageView mArrow;
    private TextView mDistance;
    private TextView mDistanceLeft;
    private Sensor mSenMagnetometer;
    private Sensor mSenAccelerometer;
    private SensorManager mSensorManager;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private boolean mNoAnimation;
    private Wasabi mWasabi;
    private float mCurrentAngle;
    private int mTotalDistance;
    private LinearLayout mGpsView;
    private LinearLayout mGpsOverView;
    private ImageView mTakePictureButton;
    private Notification mNotification;

    public GPSView(Wasabi wasabi, Notification notification)
    {
        super(wasabi);

        mWasabi = wasabi;
        mNotification = notification;

        inflate(mWasabi, R.layout.gps_view, this);

        /* Ajout du listener accéléromètre */
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        mSenAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSenMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        /* Initialisation des vues */
        mArrow = (android.widget.ImageView) findViewById(R.id.gps_arrow);
        mDistance = (TextView) findViewById(R.id.gps_distance);
        mDistanceLeft = (TextView) findViewById(R.id.gps_distance_left);
        mGpsView = (LinearLayout) findViewById(R.id.gps);
        mGpsOverView = (LinearLayout) findViewById(R.id.gps_over);
        mTakePictureButton = (ImageView) findViewById(R.id.take_picture_gps);

        mCurrentLocation = null;
        mNoAnimation = true;

        /* Gestion et connexion de la géolocalisation */
        mGeolocation = new GeolocationManager(this);

        /* On force l'arrivée */
        mArrow.setOnClickListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if(mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if(mGravity != null && mGeomagnetic != null && mNoAnimation && mCurrentLocation != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if(success)
            {
                /* Récupération du nord magnétique */
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuth = -(float) Math.toDegrees(orientation[0]);

                /* Converstion du nord magnétique en vrai nord */
                GeomagneticField geoField = new GeomagneticField(
                        (float) mCurrentLocation.getLatitude(),
                        (float) mCurrentLocation.getLongitude(),
                        (float) mCurrentLocation.getAltitude(),
                        System.currentTimeMillis());
                azimuth += geoField.getDeclination();

                /* Calcul de l'angle x par rapport à la cible */
                float bearing = mCurrentLocation.bearingTo(mGeolocation.getTarget());
                float angle = azimuth - bearing;

                /* Déclaration, instanciation et initialisation de l'animation */
                RotateAnimation rotation = new RotateAnimation(
                        mCurrentAngle, angle,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                );

                /* Propriétés de l'animation */
                rotation.setDuration(ROTATION_DURATION);
                rotation.setFillAfter(true);
                rotation.setFillEnabled(true);
                rotation.setInterpolator(new AccelerateDecelerateInterpolator());

               /* Démarrage de l'animation */
                mArrow.startAnimation(rotation);
                rotation.setAnimationListener(this);

                mCurrentAngle = angle;
                mNoAnimation = false;

                /* La distance entre les deux locations */
                int distanceLeft = (int) mCurrentLocation.distanceTo(mGeolocation.getTarget());

                /* Premier passe, on renseigne la distance totale */
                if(mTotalDistance == 0)
                    mTotalDistance = distanceLeft;

                /* Distance parcourue */
                int currentDistance = mTotalDistance - distanceLeft;

                /* Prévention */
                if(currentDistance < 0)
                    currentDistance = 0;

                mDistance.setText(String.valueOf(currentDistance));
                mDistanceLeft.setText(String.valueOf(distanceLeft));
            }
        }
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {
        mGeolocation.start();

        mSensorManager.registerListener(this, mSenAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSenMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {
        mGeolocation.stop();

        mSensorManager.unregisterListener(this, mSenAccelerometer);
        mSensorManager.unregisterListener(this, mSenMagnetometer);
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        animation.setAnimationListener(null);
        mNoAnimation = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    public void setCurrentLocation(Location currentLocation)
    {
        mCurrentLocation = currentLocation;
    }

    /**
     * On est arrivé à destination
     *
     * @param view La flèche
     */
    @Override
    public void onClick(View view)
    {
        if(view.getId() == mArrow.getId())
            clickOnArrow();
        else if(view.getId() == mTakePictureButton.getId())
            takePicture();
    }

    /**
     * Clic sur la flèche pour simuler la complétion
     */
    private void clickOnArrow()
    {
        /* Suppression du listener */
        mArrow.setOnClickListener(null);

        /* Arrêt */
        stop();

        /* Animations */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        mGpsView.startAnimation(alphaAnimation);

        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);

        mGpsOverView.setVisibility(VISIBLE);
        mGpsOverView.startAnimation(alphaAnimation);

        /* Ajout du listener sur le bouton prendre une photo */
        mTakePictureButton.setOnClickListener(this);
    }

    /**
     * Prise d'une photo
     */
    private void takePicture()
    {
        mWasabi.onTakePictureGPS();
    }

    /**
     * La photo a bien été prise
     *
     * @param photoPath Le chemin de la photo
     */
    public void photoOk(String photoPath)
    {
        /* Encodage du fichier en base64 */
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String picture64 = Base64.encodeToString(b, Base64.DEFAULT);

        /* Appel à l'API */
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("picture64", picture64));
        nameValuePairs.add(new BasicNameValuePair("request_id", String.valueOf(mNotification.getIdDb())));

        /* Exécution de la requête */
        new AsyncPostRequests(nameValuePairs).execute(
                Wasabi.URL + "/api/" + Wasabi.getApiKey() + "/request/gps"
        );
    }
}
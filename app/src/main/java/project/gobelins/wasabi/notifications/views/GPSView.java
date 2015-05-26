package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.gps.GeolocationManager;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class GPSView extends MyLayout implements SensorEventListener, Animation.AnimationListener
{
    private final int ROTATION_DURATION = 750;

    private GeolocationManager mGeolocation;
    private Location mCurrentLocation;
    private android.widget.ImageView mArrow;
    private Sensor mSenMagnetometer;
    private Sensor mSenAccelerometer;
    private SensorManager mSensorManager;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private boolean mNoAnimation;
    private float mCurrentAngle;

    public GPSView(Context context)
    {
        super(context);

        inflate(context, R.layout.gps_view, this);

        /* Ajout du listener accéléromètre */
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        mSenAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSenMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, mSenAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSenMagnetometer, SensorManager.SENSOR_DELAY_UI);

        /* Initialisation de l'imageView */
        mArrow = (android.widget.ImageView) findViewById(R.id.gps_arrow);

        mCurrentLocation = null;

        /* Gestion et connexion de la géolocalisation */
        mGeolocation = new GeolocationManager(this);
        mGeolocation.connect();
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
                int distance = (int) mCurrentLocation.distanceTo(mGeolocation.getTarget());
//                mDistance.setText(distance + "m");
            }
        }
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {

    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {

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
}

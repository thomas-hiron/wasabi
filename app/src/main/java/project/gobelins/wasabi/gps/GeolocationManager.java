package project.gobelins.wasabi.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import project.gobelins.wasabi.notifications.views.GPSView;

/**
 * Récupère la localisation courante
 * <p/>
 * Created by ThomasHiron on 26/05/2015.
 */
public class GeolocationManager implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private GPSView mGpsView;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private Location mHome;
    private Location mCurrentLocation;

    public GeolocationManager(GPSView unknownGPS)
    {
        mGpsView = unknownGPS;

        /* La piscine des marquisats */
        mHome = new Location("");
        mHome.setLatitude(45.905365);
        mHome.setLongitude(6.133305);

        mGoogleApiClient = new GoogleApiClient.Builder(mGpsView.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        /* Géolocalisation */
        mLocationManager = (LocationManager) mGpsView.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    /**
     * Recupère la localisation courante
     */
    private void getLocation()
    {
        // TODO : Tester si haute précision activée sinon requête

        try
        {
            /* Statut du GPS */
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            /* Statut du network */
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /* Rien n'est activé */
            if(!isGPSEnabled && !isNetworkEnabled)
            {
                Log.d("Network", "Network Disabled");
            }
            else
            {
                /* On prend le GPS */
                if(isGPSEnabled)
                {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                /* On prend le network si GPS null */
                if(isNetworkEnabled && mCurrentLocation == null)
                {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                mGpsView.setCurrentLocation(mCurrentLocation);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Location getTarget()
    {
        return mHome;
    }

    /**
     * Connexion à l'API
     */
    public void connect()
    {
        mGoogleApiClient.connect();
    }
}

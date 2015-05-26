package project.gobelins.wasabi.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
    private LocationRequest mLocationRequest;
    private GPSView mGpsView;
    private GoogleApiClient mGoogleApiClient;
    private Location mHome;

    public GeolocationManager(GPSView gps_view)
    {
        mGpsView = gps_view;

        /* Bonlieu */
        mHome = new Location("");
        mHome.setLatitude(45.901892);
        mHome.setLongitude(6.128723);

        mGoogleApiClient = new GoogleApiClient.Builder(mGpsView.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        /* La requête */
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        addLocationUpdates();

        checkGpsEnabled();
    }

    /**
     * Vérifie que le GPS est activé
     */
    private void checkGpsEnabled()
    {
        LocationManager locationManager = (LocationManager) mGpsView.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isGPS)
            Toast.makeText(mGpsView.getContext(), "Veuillez activer le GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        mGpsView.setCurrentLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

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

    /**
     * Démarre la récupération de la localisation
     */
    public void start()
    {
        connect();

        /* Si déjà connecté */
        if(mGoogleApiClient.isConnected())
            addLocationUpdates();
    }

    /**
     * Ajoute le requestLocationUpdates
     */
    public void addLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Suppression des mises à jour
     */
    public void stop()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}

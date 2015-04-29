package mx.com.webapps.vigilanteElectoral.webservice;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Jesus Lopez on 4/28/2015.
 */public class GpsTracker implements LocationListener {

    private Context mContext= null;
    boolean isGpsEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double longitude;
    double latitude;
    private static final long MIN_DISTANCE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATE =1000*60*1;
    protected LocationManager locationManager;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public GpsTracker(Context context)
    {
        this.mContext= context;
        getLocation();
    }



    public  Location getLocation()
    {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isNetworkEnabled&& !isNetworkEnabled)
        {

        }
        else
        {
            this.canGetLocation = true;

            if (isNetworkEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATE,MIN_DISTANCE_FOR_UPDATES,this);
                if(locationManager!=null)
                {
                    location= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!= null)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                    }
                }
            }// network if
            if (isGpsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATE,MIN_DISTANCE_FOR_UPDATES,this);
                if(locationManager!=null)
                {
                    location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location!= null)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                    }


                }
            }// network if
        }return location;
    }

    public  boolean canGetLocation ()
    {
        return this.canGetLocation;
    }


    public  void stopUsingGps()
    {
        if(locationManager!= null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

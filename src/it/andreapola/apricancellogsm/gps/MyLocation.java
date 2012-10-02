package it.andreapola.apricancellogsm.gps;


import it.andreapola.apricancellogsm.CurrentPref;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class MyLocation {

	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private Context context;
	private Handler handler;
	private LocationResult locationres;

	
	/** Called when the activity is first created. */
	public MyLocation(Context c,LocationResult l) {
		context = c;
		locManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		handler=new Handler();
		locationres = l;
	}

	public void startGPS() {

		try {gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);} catch (Exception ex) {}
		/*try {network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);} catch (Exception ex) {}*/

		// don't start listeners if no provider is enabled
		if (!gps_enabled /* && !network_enabled*/) {			
			locationres.getLocation(null);
			return;
		}

		if (gps_enabled) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
		}
		/*if (network_enabled) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
		}*/
		
		final Runnable r = new Runnable()
		{
		    public void run() 
		    {
		    	locManager.removeUpdates(locListener); 
		    	locationres.getLocation(null);
		    }
		};

		handler.postDelayed(r, CurrentPref.Get_GPSTime(context)*1000);

	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				locManager.removeUpdates(locListener); 
				locationres.getLocation(location);
			}else{
				locationres.getLocation(null);
			}
		}
		
		
	}	
	
	public static abstract class LocationResult{
        public abstract void getLocation(Location location);
    }

	
}


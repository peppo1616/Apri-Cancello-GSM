package it.andreapola.apricancellogsm.gps;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class HomePosition{
	
	private  static String bestprovider;
	private LocationManager manager = null;
	private Context context;
			
	private LocationListener listener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			@Override
			public void onProviderEnabled(String provider) {}
			@Override
			public void onProviderDisabled(String provider) {}			
			@Override
			public void onLocationChanged(Location location) {
				getCoordinates(location.getLatitude(),location.getLongitude());		
				CharSequence text = "fine"+location.getLatitude();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				stopListening();
			}
		};
		
		HomePosition(Context c){
			this.context = c;
			startListening();
		}

		
		
		private void startListening(){
			
			final Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			final String bestProvider = manager.getBestProvider(criteria, true);
			bestprovider = bestProvider;
			if (bestProvider != null && bestProvider.length() > 0) {
				manager.requestLocationUpdates(bestProvider, 500, 10, listener);
			}
			else {
				final List<String> providers = manager.getProviders(true);

				for (final String provider : providers) {
					manager.requestLocationUpdates(provider, 500, 50, listener);
				}
			}
		}
		
		private void stopListening(){
			try {
				if (manager != null && listener != null) {
					manager.removeUpdates(listener);
				}

				manager = null;
			}
			catch (final Exception ex) {

			}
		}
		
		private String getCoordinates(double latitude, double longitude){
			String info = "";

			info += ("\n" + "(lat " + latitude + ", lon " + longitude + ")");
			info += "\n Resource: "+ bestprovider;
			
			return info;
		}
		

}


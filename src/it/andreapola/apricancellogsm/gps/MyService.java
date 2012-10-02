package it.andreapola.apricancellogsm.gps;

import it.andreapola.apricancellogsm.CurrentPref;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements LocationListener{
	private static final String TAG = "MyService";
	Context context;
	private LocationManager locManager;
	private double HomeLat;
	private double HomeLong;
	Location home = new Location("reverseGeocoded");
	boolean athome = true;
	int Distance;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		context = getBaseContext();	
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    HomeLat = CurrentPref.Get_MyHomeLat(context);
		HomeLong = CurrentPref.Get_MyHomeLong(context);
		Distance = CurrentPref.Get_GPSOpenDistance(context);
		startListening();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		stopListening();
	}
	
	
	@Override
	public void onStart(Intent intent, int startid) {

		Log.d(TAG, "onStart");
		//startListening();
	}

	@Override
	public void onLocationChanged(Location location) {		
	    HomeLat = CurrentPref.Get_MyHomeLat(context);
		HomeLong = CurrentPref.Get_MyHomeLong(context);	
		
		home.setLatitude(HomeLat / 1e8);
		home.setLongitude(HomeLong / 1e8);
				
	    Float fh = location.distanceTo(home);
	    int distancetohome = fh.intValue();
	    
	    //polling policy, assume velocity of 55m/s
	    if (locManager != null) locManager.removeUpdates(this);
	    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,PollingPolicy.next_interval_multi(distancetohome) * 1000,0,this);
	    
	    
		Log.d("DISTANCE FROM HOME", ""+distancetohome);
		
		
		if((distancetohome < Distance) && (athome == false)){
			String url = "tel:"+CurrentPref.Get_GateNumber(context);
	    	Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));    		
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    	startActivity(intent);
			athome = true;
			
			Toast.makeText(context,"Apro il cancello, distanza: "+distancetohome,Toast.LENGTH_LONG).show();
		   
		} else if (distancetohome > Distance){
			athome = false;
			Toast.makeText(context,"Sono lontano da casa: "+distancetohome,Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		onDestroy();
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private void startListening() {
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1 * 10 * 1000,0,this);
    }

    private void stopListening() {
        if (locManager != null) locManager.removeUpdates(this);
    }
    

  

    private double DirectDistance(double lat1, double lng1, double lat2, double lng2) 

    {
      double earthRadius = 3958.75;
      double dLat = ToRadians(lat2-lat1);
      double dLng = ToRadians(lng2-lng1);
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
    		  	 Math.cos(ToRadians(lat1)) * Math.cos(ToRadians(lat2)) * 
    		  	 Math.sin(dLng/2) * Math.sin(dLng/2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      double dist = earthRadius * c;
      double meterConversion = 1609.00;
      return dist * meterConversion;
    }

    private double ToRadians(double degrees) 
    {
      double radians = degrees * Math.PI/ 180;
      return radians;
    }

}

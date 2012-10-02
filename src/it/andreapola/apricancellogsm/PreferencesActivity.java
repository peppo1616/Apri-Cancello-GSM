package it.andreapola.apricancellogsm;



import it.andreapola.apricancellogsm.CustomPreference.EditIntPreference;
import it.andreapola.apricancellogsm.gps.MyLocation;
import it.andreapola.apricancellogsm.gps.MyService;
import it.andreapola.apricancellogsm.gps.MyLocation.LocationResult;
import it.andreapola.apricancellogsm.map.ShowMapActivity;
import it.andreapola.apricancellogsm.widget.GSMGateWidgetProvider;
import it.andreapola.apricancellogsm.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.widget.Toast;



public class PreferencesActivity extends PreferenceActivity  {
	
	private ProgressDialog dialog;
	private AlertDialog alert;
	private AlertDialog alert2;
	private LocationManager locationManager;
	
	Preference ViewHomeMap;
	EditTextPreference CurrentHomeposition;
	EditTextPreference GateNumber;
	Preference SetCurrentGPSpositionPref;
	CheckBoxPreference GpsPref;
	CheckBoxPreference StartServicePref;
	PreferenceCategory GpsCategory;
	 EditIntPreference GPSOpenDistance;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences_2);
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        GpsCategory = (PreferenceCategory) findPreference("GpsCategory");
        GpsPref = (CheckBoxPreference) findPreference("GpsPref");
        StartServicePref = (CheckBoxPreference) findPreference("GpsServicePref");
        SetCurrentGPSpositionPref = (Preference) findPreference("SetCurrentGPSpositionPref");
        CurrentHomeposition = (EditTextPreference) findPreference("CurrentHomeposition");
        ViewHomeMap = (Preference) findPreference("ViewHomeMap");
        GPSOpenDistance = (EditIntPreference) findPreference("GPSOpenDistance");
        GateNumber = (EditTextPreference) findPreference("GateNumberPref");

      
        CurrentHomeposition.setEnabled(false);
        CurrentHomeposition.setSummary(CurrentHomeposition.getText());
        
        Preference slaves[] = {SetCurrentGPSpositionPref,StartServicePref,GPSOpenDistance};
        Preference slaves_onvalue[] = {ViewHomeMap,StartServicePref};
        toggle_gps(GpsPref,slaves); 
        toggleOnValue(CurrentHomeposition,slaves_onvalue);
        
        
        SetCurrentGPSpositionPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {	
				
				showSETHOMEDialog();
				return false;
			}});
        
        
        ViewHomeMap.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				if(CurrentHomeposition.getText().contains(",")){				
		    		Intent intent = new Intent(getBaseContext(), ShowMapActivity.class);    
		    		
		    		intent.putExtra( "home_lati", CurrentHomeposition.getText().split(",")[0].trim());
		    		intent.putExtra( "home_longi", CurrentHomeposition.getText().split(",")[1].trim());
			        startActivity(intent);
					return false;
				}
				return false;
			}
			
		});
        
        StartServicePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if(StartServicePref.isChecked()){
					startService(new Intent(getBaseContext(), MyService.class));
		        }else{
		        	stopService(new Intent(getBaseContext(), MyService.class));
		        }
				return false;
			}});
        
             
	}
	
	protected void onPause(){
		super.onPause();
		Intent intent = new Intent(this,GSMGateWidgetProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
		// since it seems the onUpdate() is only fired on that:
		//int[] ids = {widgetId};
		//intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		sendBroadcast(intent);	
		Toast.makeText(getBaseContext(), R.string.SET_CONFIRM, Toast.LENGTH_SHORT).show();		
	}
			
	private void toggleOnValue(final EditTextPreference mainp,final Preference[] slavep){
		
		if(mainp.getText().contains("Not defined") || mainp.getText() == "" || mainp.getText() == null){
			disable_slaves(slavep);
        }else{
        	enable_slaves(slavep);
        }
	}
	
	private void showGPSDialog(){
		
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

		}else{
			alert = new AlertDialog.Builder(this).create();
	        alert.setTitle("");
	        alert.setMessage(getString(R.string.GPS_NEEDED));
	        
	        alert.setButton("Ok",new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int id){
	    				Intent callGPSSettingIntent = new Intent(
	    				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    				startActivity(callGPSSettingIntent);

	    	}});
	        alert.setButton2("Cancel",new DialogInterface.OnClickListener(){
	        		public void onClick(DialogInterface dialog, int id){
	        		dialog.cancel();
	        		GpsPref.setChecked(false);
	        		Preference slaves[] = {SetCurrentGPSpositionPref};
	        		disable_slaves(slaves);
	        }});
	        alert.show();
		}
		
        		
		
	};
	
private void showSETHOMEDialog(){
		
			alert2 = new AlertDialog.Builder(this).create();
	        alert2.setTitle("");
	        alert2.setMessage(getString(R.string.GPS_ON_AIR));
	        
	        alert2.setButton("Ok I'm ready",new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int id){
	    				dialog.cancel();
	    				StartGPSSynch();

	    	}});
	        alert2.setButton2("Cancel",new DialogInterface.OnClickListener(){
	        		public void onClick(DialogInterface dialog, int id){
	        			dialog.cancel();
	        }});
	        alert2.show();        				
	};
	
	private void StartGPSSynch(){
		
		LocationResult locationResult = new LocationResult(){
			@Override
			public void getLocation(Location location) {							
					if(location == null){								
						Toast.makeText(PreferencesActivity.this,getString(R.string.DEVICE_NOT_READY), Toast.LENGTH_SHORT).show();								
						dialog.dismiss();
					}else{
						dialog.dismiss();
						String position = ""+location.getLatitude()+" , "+location.getLongitude();
		    			
		    			Toast.makeText(getBaseContext(), position, Toast.LENGTH_SHORT).show();
		    			
						
						final EditTextPreference CurrentHomeposition = (EditTextPreference) findPreference("CurrentHomeposition");
						CurrentHomeposition.setText(position);
						CurrentHomeposition.setSummary(CurrentHomeposition.getText());
						ViewHomeMap.setEnabled(true);
					}					
				};
	        };
	        
	        showLoadingDialog();
	        MyLocation myLocation = new MyLocation(PreferencesActivity.this,locationResult);					
			myLocation.startGPS();
	}
	
	private void showLoadingDialog(){
		dialog = new ProgressDialog(PreferencesActivity.this);
        dialog.setTitle("");
        dialog.setMessage(getString(R.string.GPS_LOADING));
        dialog.setIndeterminate(true);
        dialog.show();
	}
	
	private void toggle_gps(final CheckBoxPreference mainp,final Preference slavep[]){
		
		toggle_(mainp,slavep);
		
		mainp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if(mainp.isChecked()){
					showGPSDialog();
					enable_slaves(slavep);
					//startService(new Intent(getBaseContext(), MyService.class));
		        }else{
		        	disable_slaves(slavep);
		        	StartServicePref.setChecked(false);
		        	stopService(new Intent(getBaseContext(), MyService.class));
		        	//stopService(new Intent(getBaseContext(), MyService.class));
		        }
				return false;
			}});
	
	}
	
	private void toggle_(final CheckBoxPreference mainp,final Preference slavep[]){
		if(mainp.isChecked()){
			for(int i=0;i < slavep.length;i++){
					slavep[i].setEnabled(true);
			}
        }else{
        	for(int i= 0;i < slavep.length;i++){
				slavep[i].setEnabled(false);
        	}
        }
	}
	
	private void enable_slaves(final Preference slavep[]){
			for(int i= 0;i < slavep.length;i++){
					slavep[i].setEnabled(true);
			}
	}
	private void disable_slaves(final Preference slavep[]){
		for(int i= 0;i < slavep.length;i++){
				slavep[i].setEnabled(false);
		}
		
		
}
    
}
package it.andreapola.apricancellogsm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class CurrentPref {
	
	public  static boolean Get_GpsOption(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		boolean GpsOption = prefs.getBoolean("GpsPref", false);
		return GpsOption;
		
	}
	
	public static String Get_GateNumber(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String GateNumber = prefs.getString("GateNumberPref", "");
		return GateNumber;
		
	}
	
	public static String Get_MyHomeposition(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String MyHomeposition = prefs.getString("CurrentHomeposition", "Not defined");
		return MyHomeposition;
		
	}
	
	public static double Get_MyHomeLat(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String MyHomeposition = prefs.getString("CurrentHomeposition", "Not defined");
		Log.d("GET_MYHOMELAT", MyHomeposition);
		if(MyHomeposition.contains(",")){		   		
			return Double.parseDouble(MyHomeposition.split(",")[0].trim().replace(".", ""));
		}else{
			return 1;
		}
		
	}
	
	public static double Get_MyHomeLong(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String MyHomeposition = prefs.getString("CurrentHomeposition", "Not defined");
		
		if(MyHomeposition.contains(",")){		   		
			return Double.parseDouble(MyHomeposition.split(",")[1].trim().replace(".", ""));
		}else{
			return 1;
		}
		
	}
	
	public static int Get_GPSTime(Context c){
		return 30;
		
	}
	
	public static int Get_GPSOpenDistance(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
	    int GPSDistance = prefs.getInt("GPSOpenDistance", 15);
		return GPSDistance;
		
	}
	
		
	public static boolean PrefsIsNull(Context c){
		if ((Get_GateNumber(c).isEmpty()) || (Get_GateNumber(c) == "") || (Get_GateNumber(c).length() < 9)) return true;
		else return false;
	}

}

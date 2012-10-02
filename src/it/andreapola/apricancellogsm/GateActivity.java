package it.andreapola.apricancellogsm;


import it.andreapola.apricancellogsm.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class GateActivity extends Activity {
	
	public Vibrator v;
	Context context;
	 
	/*Avvia il servizio che intercetta le chiamate verso il numero impostato if TRUE*/       
	protected ProgressBar progress;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		setContentView(R.layout.main);
		context = getApplicationContext();
						
		/*Bottone principale*/
		ImageButton OpenButton = (ImageButton) this.findViewById(R.id.ImageOpenButton);
		
		OpenButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	GateActivity.this.v.vibrate(100);	    	
		    	if(!CurrentPref.PrefsIsNull(context)){
		    		
		    		String url = "tel:"+CurrentPref.Get_GateNumber(context);
		    		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));    		
			        startActivity(intent);
		    	}else{
					CharSequence text = (String) getString(R.string.NO_SETTINGS);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
		    	}			
		        
		    }
		});		
		
		/*Bottoni Prefenze in alto*/
		
		ImageButton PrefButton = (ImageButton) this.findViewById(R.id.bar_pref_button);
		
		PrefButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	GateActivity.this.v.vibrate(50);
		    	Intent myIntent = new Intent(getBaseContext(), PreferencesActivity.class);
				startActivity(myIntent);				
		    }
		  });
		
		
		/*Start del servizio se attivo nelle preferenze*/
				
		
		setNoSettingsAds();
		SetCustomFonts();
	}
	
	protected void onResume(){
		super.onResume();
		setNoSettingsAds();
	};

	
	protected void onDestroy(){
		super.onDestroy();
	};
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	GateActivity.this.v.vibrate(50);	
		
		if(item.getTitle() == getResources().getString(R.string.MENU_INFO)){
			return true;
			
		}else if(item.getTitle() == getResources().getString(R.string.MENU_PREF)){
					
			Intent myIntent = new Intent(getBaseContext(), PreferencesActivity.class);
			startActivity(myIntent);
			return true;
		}		
		return true;
	}
	
	private void setNoSettingsAds(){
		
		TextView gatenumber = (TextView) findViewById(R.id.Gate);
		
		if(CurrentPref.PrefsIsNull(context)){	
			gatenumber.setVisibility(View.VISIBLE);
			gatenumber.setText(R.string.NO_SETTINGS);
		}else{
			gatenumber.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private void SetCustomFonts(){
		
		TextView txt0 = (TextView) findViewById(R.id.TextView01);  
		TextView txt1 = (TextView) findViewById(R.id.Gate);
		Typeface font = Typeface.createFromAsset(getAssets(), "Harngton.ttf");  
		txt0.setTypeface(font); 
		txt1.setTypeface(font); 
	}
	

}
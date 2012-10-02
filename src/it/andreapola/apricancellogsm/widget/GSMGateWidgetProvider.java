package it.andreapola.apricancellogsm.widget;

import it.andreapola.apricancellogsm.CurrentPref;
import it.andreapola.apricancellogsm.PreferencesActivity;
import it.andreapola.apricancellogsm.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class GSMGateWidgetProvider extends AppWidgetProvider {
	
	private String GateNumber;
	private int[] WidgetId;
	private AppWidgetManager appman;
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gsmwidget);
		       
		if(!CurrentPref.PrefsIsNull(context)){	
        	CharSequence text = "Click to open the Gate";
            views.setTextViewText(R.id.txtInfo, text);
    		String url = "tel:"+CurrentPref.Get_GateNumber(context);
    		intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));    	
            views.setTextViewText(R.id.txtInfo, text);
    	}else{
            intent = new Intent(context, PreferencesActivity.class);
            CharSequence text = "Set your GSM Phone First, click to set!";
            views.setTextViewText(R.id.txtInfo, text);          
    	}
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.openbuttonwidget, pendingIntent);
        
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, GSMGateWidgetProvider.class);
        appWidgetManager.updateAppWidget(thisWidget, views);
		
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		appman = appWidgetManager;
		WidgetId = appWidgetIds;
		
		final int N = appWidgetIds.length;	
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gsmwidget);	

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent intent = null;
            
            if(!CurrentPref.PrefsIsNull(context)){	    		
	    		String url = "tel:"+CurrentPref.Get_GateNumber(context);
	    		intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));    		
	    	}else{
	            intent = new Intent(context, PreferencesActivity.class);
	            CharSequence text = "Set your GSM Phone First, click to set!";
	            views.setTextViewText(R.id.txtInfo, text);
	            
	    	}

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
          
            views.setOnClickPendingIntent(R.id.openbuttonwidget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        
        if(CurrentPref.Get_GpsOption(context)){};
        //context.startService(new Intent(context,GPSWidgetService.class));
	}
	
	
	
}

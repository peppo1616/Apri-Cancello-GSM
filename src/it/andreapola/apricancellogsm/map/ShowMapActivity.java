package it.andreapola.apricancellogsm.map;


import it.andreapola.apricancellogsm.R;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import com.google.android.maps.MapView.LayoutParams;
import android.view.View;
import android.widget.LinearLayout;
 
public class ShowMapActivity extends MapActivity 
{    
    MapView mapView; 
    MapController mc;
    GeoPoint p;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        Intent intent= getIntent();
        String lati = intent.getStringExtra("home_lati");
        String longi = intent.getStringExtra("home_longi");
        
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setSatellite(true);
        mapView.setStreetView(true);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
 
        mc = mapView.getController();
        double lng = Double.parseDouble(longi);
        double lat = Double.parseDouble(lati);
 
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
 
        mc.animateTo(p);
        mc.setZoom(19); 
        
        MapOverlay mapOverlay = new MapOverlay();
        List<com.google.android.maps.Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay); 
        
        mapView.invalidate();
    }
    
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.marker);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);         
            return true;
        }
    } 
      
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}
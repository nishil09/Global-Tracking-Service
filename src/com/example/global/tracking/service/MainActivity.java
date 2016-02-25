package com.example.global.tracking.service;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnLongClickListener;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;


public class MainActivity extends MapActivity implements LocationListener{
	
	MapView mapView;
	long start;
	long stop;
	MyLocationOverlay compass; 
	MapController Controller;
	int x,y;
	GeoPoint touchedPoint;
	Drawable d;
	List<Overlay> overlayList;
	LocationManager lm;
	String towers;
	int lat;
    int longi;
    boolean isGpsEnabled=false, isNetworkEnabled=false;
    Location location = null;
       
    ActionMode mMode;
    
    ActionMode.Callback mCallback = new ActionMode.Callback() {
    	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    		mode.setTitle("Global Tracking Service");
    		getMenuInflater().inflate(R.menu.activity_main, menu);
            return true;
    	}
    	
    	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    		// TODO Auto-generated method stub
    		return false;
    	}
    	
    	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    		switch(item.getItemId()){
            case R.id.action1:
                Toast.makeText(getBaseContext(), "Selected Action1 ", Toast.LENGTH_LONG).show();
                mode.finish();                          // Automatically exists the action mode, when the user selects this action
                mMode = startActionMode(mCallback);  
                break;
            case R.id.action2:
                Toast.makeText(getBaseContext(), "Selected Action2 ", Toast.LENGTH_LONG).show();
                mMode = startActionMode(mCallback); 
                break;
            case R.id.action3:
                Toast.makeText(getBaseContext(), "Selected Action3 ", Toast.LENGTH_LONG).show();
                mMode = startActionMode(mCallback); 
                break;
            }
            return false;
        }
    	
    	public void onDestroyActionMode(ActionMode mode) {
    		mMode = null;
    		
    	}
		
		
	};
    
    
    
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        TextView tvHello = (TextView) findViewById(R.id.tv_hello);
        
       
     /*   
        OnLongClickListener listener = new OnLongClickListener() {
        	 
           
            public boolean onLongClick(View v) {
                if(mMode!=null)
                    return false;
                else
                    mMode = startActionMode(mCallback);
                	
                	
                return true;
             }
            
            
            
            
            
            
            
            
            
       };
 
        tvHello.setOnLongClickListener(listener);
        
      */
        
        
     
        
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        Controller=mapView.getController();
        
        //mapController.setZoom(14);
        
        Touchy t = new Touchy();
        overlayList = mapView.getOverlays();
        overlayList.add(t);
        compass = new MyLocationOverlay(MainActivity.this, mapView);
        overlayList.add(compass);
        
        GeoPoint point = new GeoPoint((int) (22.5700 * 1E6), (int) (72.9300 * 1E6));
        //Controller.animateTo(point);
        Controller.setZoom(16);
        mapView.setTraffic(true);
        
        d = getResources().getDrawable(R.drawable.icon);
        
        //placing pinpoint at location
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        //Criteria crit = new Criteria();
        
        //towers = lm.getBestProvider(crit, false);
        //Location location = lm.getLastKnownLocation(towers);
        
        /*if (location != null){
        	lat = (int) (location.getLatitude() *1E6);
        	longi = (int) (location.getLongitude() *1E6);
        	GeoPoint ourLocation = new GeoPoint(lat,longi);
            OverlayItem overlayItem = new OverlayItem(ourLocation, "what's up","2nd String");
    		CustomPinpoint custom = new CustomPinpoint(d, MainActivity.this);
    		custom.insertpinpoint(overlayItem);
    		overlayList.add(custom);
        }else{
        	Toast.makeText(MainActivity.this, "Couldn't Get Provider", Toast.LENGTH_SHORT).show();
        	
        }*/
        
        if (!isGpsEnabled && !isNetworkEnabled){
        	Toast.makeText(MainActivity.this, "Couldn't Get Provider", Toast.LENGTH_SHORT).show();        	
        }else{
        	// if GPS Enabled get lat/long using GPS Services
        	if (isGpsEnabled) {
                if (location == null) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
                    //Log.d("GPS", "GPS Enabled");
                    if (lm != null) {
                        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                        	lat = (int) (location.getLatitude() *1E6);
                    		longi = (int) (location.getLongitude() *1E6);
                        }
                    }
                }
            }
        	if (isNetworkEnabled) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, this);
                //Log.d("Network", "Network Enabled");
                if (lm != null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                    	lat = (int) (location.getLatitude() *1E6);
                		longi = (int) (location.getLongitude() *1E6);
                    }
                }
            }
                        
        	
        }
   	 mMode = startActionMode(mCallback);   
        
    }
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		compass.disableCompass();
    	super.onPause();
    	lm.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		compass.enableCompass();
		super.onResume();
		//lm.requestLocationUpdates(towers, 500, 1, this);
	}

	@Override
	protected boolean isRouteDisplayed() {
	    return true;
	}
    
    class Touchy extends Overlay
	{
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if(e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = mapView.getProjection().fromPixels(x,y);
			}
			if(e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			if(stop - start > 750){
				AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
				alert.setTitle("pick an Option");
				alert.setButton("Add To Favourits", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						OverlayItem overlayItem = new OverlayItem(touchedPoint, "what's up","2nd String");
						CustomPinpoint custom = new CustomPinpoint(d, MainActivity.this);
						custom.insertpinpoint(overlayItem);
						overlayList.add(custom);
						
						
					}
				});
				alert.setButton2("Get Address Details", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
						   try{
							    List<Address> address = geocoder.getFromLocation(touchedPoint.getLatitudeE6() / 1E6,touchedPoint.getLongitudeE6() / 1E6, 1);
							    if(address.size()>0){
							    	String display = "";
							    	for(int i=0; i<address.get(0).getMaxAddressLineIndex(); i++){
							    	
							    		display += address.get(0).getAddressLine(i) + "\n";
							    	}
							    	Toast t = Toast.makeText(getBaseContext(), display, Toast.LENGTH_LONG);
							    	t.show();
							    	
							    }
						   } catch (IOException e) {
							   e.printStackTrace();
						   }finally{
							   
						   }
						
					}
				});
				alert.setButton3("Toggle View", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						if(mapView.isSatellite()){
							mapView.setSatellite(false);
							mapView.setStreetView(true);
						}else{
							mapView.setStreetView(false);
							mapView.setSatellite(true);
						}
					}
				});
				alert.show();
				return true; 
				
			}
			return false;
		}    	
	
	}

	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub
		/*lat = (int) (l.getLatitude() *1E6);
		longi = (int) (l.getLongitude() *1E6);*/
		lat = (int) (location.getLatitude() *1E6);
		longi = (int) (location.getLongitude() *1E6);
		GeoPoint ourLocation = new GeoPoint(lat,longi);
        OverlayItem overlayItem = new OverlayItem(ourLocation, "what's up","2nd String");
		CustomPinpoint custom = new CustomPinpoint(d, MainActivity.this);
		custom.insertpinpoint(overlayItem);
		overlayList.add(custom);
		Controller.animateTo(ourLocation);
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	

	
/*
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.setTitle("Demo");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
	}
	
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch(item.getItemId()){
        case R.id.action1:
            Toast.makeText(getBaseContext(), "Selected Action1 ", Toast.LENGTH_LONG).show();
            mode.finish();    // Automatically exists the action mode, when the user selects this action
            break;
        case R.id.action2:
            Toast.makeText(getBaseContext(), "Selected Action2 ", Toast.LENGTH_LONG).show();
            break;
        case R.id.action3:
            Toast.makeText(getBaseContext(), "Selected Action3 ", Toast.LENGTH_LONG).show();
            break;
        }
        return false;
    }
	
	public void onDestroyActionMode(ActionMode mode) {
		mMode = null;
		
	}
*/
	
    
    

	
	
	
}


	
	

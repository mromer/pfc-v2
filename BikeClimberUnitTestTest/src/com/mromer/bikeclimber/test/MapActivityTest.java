package com.mromer.bikeclimber.test;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.mromer.bikeclimber.MapActivity;
import com.mromer.bikeclimber.R;
import com.mromer.bikeclimber.commons.ConstantesMain;

public class MapActivityTest extends
		ActivityInstrumentationTestCase2<MapActivity> {

	private MapActivity mActivity;

	public MapActivityTest() {
		super(MapActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		
		Log.d("", "running setUp");
		
		super.setUp();
		
		// if is emulator maps does not work
		if ("google_sdk".equals(Build.PRODUCT)){
			return;
		}	

		Location loc1 = new Location("");
		loc1.setLatitude(36.711902);
		loc1.setLongitude(-4.632730);
		Location loc2 = new Location("");
		loc2.setLatitude(36.712157);
		loc2.setLongitude(-4.629873);

		Intent mIntent = new Intent();
		Bundle mBundle = new Bundle();
		mBundle.putInt(ConstantesMain.BUNDLE_MEDIO_SELECCIONADO,
				ConstantesMain.MEDIO_ANDANDO);
		mBundle.putParcelable(ConstantesMain.BUNDLE_FROM_LOCATION, loc1);
		mBundle.putParcelable(ConstantesMain.BUNDLE_TO_LOCATION, loc2);
		mBundle.putString(ConstantesMain.BUNDLE_FROM_TITLE, "From title");
		mBundle.putString(ConstantesMain.BUNDLE_TO_TITLE, "To title");
		mIntent.putExtras(mBundle);

		setActivityIntent(mIntent);
		mActivity = getActivity();
	}	

	
	public void testActivityInitialized() {
		// if is emulator maps does not work
		if ("google_sdk".equals(Build.PRODUCT)) {
			return;
		}
		
		// Showing action bar
		assertTrue(mActivity.getSupportActionBar().isShowing());

		GoogleMap mapa = ((SupportMapFragment) mActivity
				.getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		// Se ha creado el mapa
		assertTrue(mapa != null);	
	}

}

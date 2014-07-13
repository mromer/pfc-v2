package com.mromer.bikeclimber.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenciesUtil {
	
	public static int getValue(Context context, String key) {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);   
		String value = preferences.getString(key, null);
		
		if (value == null) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}		
	}
	
	public static void setPreference(Context context, String key, String value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

}

package com.mromer.bikeclimber;

import java.util.List;

import com.mromer.bikeclimber.utils.StringUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;

public class PreferenceWithHeaders extends PreferenceActivity  {

	public static String ANDANDO_FACIL = "andando_facil";
	public static String ANDANDO_MEDIO = "andando_medio";
	public static String ANDANDO_DIFICIL = "andando_dificil";

	public static String BICI_FACIL = "bici_facil";
	public static String BICI_MEDIO = "bici_medio";
	public static String BICI_DIFICIL = "bici_dificil";

	public static String ACCESIBLE_FACIL = "accesible_facil";
	public static String ACCESIBLE_MEDIO = "accesible_medio";
	public static String ACCESIBLE_DIFICIL = "accesible_dificil";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}    

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class Prefs1Fragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences_screen);			

			setSummaryFromShared(ANDANDO_FACIL);
			setSummaryFromShared(ANDANDO_MEDIO);
			setSummaryFromShared(ANDANDO_DIFICIL);		
			setEditTextController(ANDANDO_FACIL);
			setEditTextController(ANDANDO_MEDIO);
			setEditTextController(ANDANDO_DIFICIL);
			
			setSummaryFromShared(BICI_FACIL);
			setSummaryFromShared(BICI_MEDIO);
			setSummaryFromShared(BICI_DIFICIL);		
			setEditTextController(BICI_FACIL);
			setEditTextController(BICI_MEDIO);
			setEditTextController(BICI_DIFICIL);
			
			setSummaryFromShared(ACCESIBLE_FACIL);
			setSummaryFromShared(ACCESIBLE_MEDIO);
			setSummaryFromShared(ACCESIBLE_DIFICIL);		
			setEditTextController(ACCESIBLE_FACIL);
			setEditTextController(ACCESIBLE_MEDIO);
			setEditTextController(ACCESIBLE_DIFICIL);			
		}
		
		private void setEditTextController(String key) {
			EditTextPreference accesibleFacilETP = 
					(EditTextPreference) getPreferenceScreen().findPreference(key);					
			accesibleFacilETP.setOnPreferenceChangeListener(
					getOnPreferenceChangeListener(key));
		}
		
		
		private OnPreferenceChangeListener getOnPreferenceChangeListener(final String key) {
			return new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference,
						Object newValue) {	
					boolean result = preferencesChecker(key, (String) newValue);
					if (result) {
						getPreferenceScreen().findPreference(key).setSummary((CharSequence) newValue);
					}
					return preferencesChecker(key, (String) newValue);					
				}};
		}

		private void setSummaryFromShared(String key) {
			String value = getPreferenceScreen().getSharedPreferences().getString(key, "");
			getPreferenceScreen().findPreference(key).setSummary(value);
		}
		
		public boolean preferencesChecker(String key, String newValue) {
			boolean result = false;
			if (key.equals(ANDANDO_FACIL) || key.equals(ANDANDO_MEDIO) || key.equals(ANDANDO_DIFICIL)) {
				result = checkAccesible(key, newValue, ANDANDO_FACIL, ANDANDO_MEDIO, ANDANDO_DIFICIL);
			} else if (key.equals(BICI_FACIL) || key.equals(BICI_MEDIO) || key.equals(BICI_DIFICIL)) {
				result = checkAccesible(key, newValue, BICI_FACIL, BICI_MEDIO, BICI_DIFICIL);
			} else {
				result = checkAccesible(key, newValue, ACCESIBLE_FACIL, ACCESIBLE_MEDIO, ACCESIBLE_DIFICIL);
			}
			return result;
		}
		
		
		private boolean checkAccesible(String key, String newValue, 
				String keyFacil, String keyMedio, String keyDificil) {
			
			boolean result = false;
			SharedPreferences sharedPreferences= getPreferenceScreen().getSharedPreferences();
			String facilValue = sharedPreferences.getString(keyFacil, "");
			String medioValue = sharedPreferences.getString(keyMedio, "");
			String dificilValue = sharedPreferences.getString(keyDificil, "");
			
			if (key.equals(keyFacil)) {				
				if (StringUtil.checkValueLower(newValue, medioValue)) {
					result = true;
				}
			} else if (key.equals(keyMedio)) {				
				if (StringUtil.checkValueBetween(newValue, facilValue, dificilValue)) {
					result = true;
				}
			} else {				
				if (StringUtil.checkValueBigger(newValue, medioValue)) {
					result = true;
				}
			}
			return result;
		}
	}  
}
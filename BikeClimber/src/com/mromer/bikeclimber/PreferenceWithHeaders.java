package com.mromer.bikeclimber;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;

import com.mromer.bikeclimber.commons.DificultadHelper;
import com.mromer.bikeclimber.utils.SharedPreferenciesUtil;

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

		// Add a button to the header list.
		if (hasHeaders()) {
			Button button = new Button(this);
			button.setText("Some action");
			setListFooter(button);
		}
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
	public static class Prefs1Fragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

		private String andandoFacil;
		private String andandoMedio;
		private String andandoDificil;

		private String biciFacil;
		private String biciMedio;
		private String biciDificil;

		private String accesibleFacil;
		private String accesibleMedio;
		private String accesibleDificil;


		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences_screen);

			andandoFacil = setSummary(ANDANDO_FACIL, DificultadHelper.RANGO_ANDANDO_1);			
			andandoMedio = setSummary(ANDANDO_MEDIO, DificultadHelper.RANGO_ANDANDO_2);
			andandoDificil = setSummary(ANDANDO_DIFICIL, DificultadHelper.RANGO_ANDANDO_3);
			SharedPreferenciesUtil.setPreference(getActivity(), ANDANDO_FACIL, andandoFacil);
			SharedPreferenciesUtil.setPreference(getActivity(), ANDANDO_MEDIO, andandoMedio);
			SharedPreferenciesUtil.setPreference(getActivity(), ANDANDO_DIFICIL, andandoDificil);

			biciFacil = setSummary(BICI_FACIL, DificultadHelper.RANGO_BICI_1);
			biciMedio = setSummary(BICI_MEDIO, DificultadHelper.RANGO_BICI_2);
			biciDificil = setSummary(BICI_DIFICIL, DificultadHelper.RANGO_BICI_3);
			SharedPreferenciesUtil.setPreference(getActivity(), BICI_FACIL, biciFacil);
			SharedPreferenciesUtil.setPreference(getActivity(), BICI_MEDIO, biciMedio);
			SharedPreferenciesUtil.setPreference(getActivity(), BICI_DIFICIL, biciDificil);

			accesibleFacil = getPreferenceScreen().getSharedPreferences()
					.getString(ACCESIBLE_FACIL, DificultadHelper.RANGO_SILLA_1);
			accesibleMedio = getPreferenceScreen().getSharedPreferences()
					.getString(ACCESIBLE_MEDIO, DificultadHelper.RANGO_SILLA_2);
			accesibleDificil = getPreferenceScreen().getSharedPreferences()
					.getString(ACCESIBLE_DIFICIL, DificultadHelper.RANGO_SILLA_3);			
			SharedPreferenciesUtil.setPreference(getActivity(), ACCESIBLE_FACIL, accesibleFacil);
			SharedPreferenciesUtil.setPreference(getActivity(), ACCESIBLE_MEDIO, accesibleMedio);
			SharedPreferenciesUtil.setPreference(getActivity(), ACCESIBLE_DIFICIL, accesibleDificil);
			getPreferenceScreen().findPreference(ACCESIBLE_FACIL).setSummary(accesibleFacil);
			getPreferenceScreen().findPreference(ACCESIBLE_MEDIO).setSummary(accesibleMedio);
			getPreferenceScreen().findPreference(ACCESIBLE_DIFICIL).setSummary(accesibleDificil);

		}

		private String setSummary(String key, String defaultValue) {
			String value = getPreferenceScreen().getSharedPreferences().getString(key, defaultValue);
			getPreferenceScreen().findPreference(key).setSummary(value);			
			return value;
		}

		@Override
		public void onResume() {
			super.onResume();
			// Set up a listener whenever a key changes
			getPreferenceScreen().getSharedPreferences()
			.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			super.onPause();
			// Unregister the listener whenever a key changes
			getPreferenceScreen().getSharedPreferences()
			.unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {

			if (key.equals(ANDANDO_FACIL) || key.equals(ANDANDO_MEDIO) || key.equals(ANDANDO_DIFICIL)) {
				setAndando(sharedPreferences, key);
			} else if (key.equals(BICI_FACIL) || key.equals(BICI_MEDIO) || key.equals(BICI_DIFICIL)) {
				setBici(sharedPreferences, key);
			} else {
				setAccesible(sharedPreferences, key);
			}
		}		

		private void setAndando(SharedPreferences sharedPreferences, String key) {

			if (key.equals(ANDANDO_FACIL)) {
				String value = sharedPreferences.getString(ANDANDO_FACIL, DificultadHelper.RANGO_ANDANDO_1);
				if (!checkValueLower(value, andandoMedio)) {
					SharedPreferenciesUtil.setPreference(getActivity(), ANDANDO_FACIL, andandoFacil);
				} else {
					andandoFacil = value;					
				}
			} else if (key.equals(ANDANDO_MEDIO)) {
				String value = sharedPreferences.getString(ANDANDO_MEDIO, DificultadHelper.RANGO_ANDANDO_2);
				if (!checkValueBetween(value, andandoFacil, andandoDificil)) {
					SharedPreferenciesUtil.setPreference(getActivity(), ANDANDO_MEDIO, andandoMedio);
				} else {
					andandoMedio = value;
				}
			} else {
				String value = sharedPreferences.getString(ANDANDO_DIFICIL, DificultadHelper.RANGO_ANDANDO_3);
				if (!checkValueBigger(value, andandoMedio)) {
					SharedPreferenciesUtil.setPreference(getActivity(), ANDANDO_DIFICIL, andandoDificil);
				} else {
					andandoDificil = value;
				}
			}
		}

		private void setBici(SharedPreferences sharedPreferences, String key) {

			if (key.equals(BICI_FACIL)) {
				String value = sharedPreferences.getString(BICI_FACIL, DificultadHelper.RANGO_BICI_1);
				if (!checkValueLower(value, biciMedio)) {
					SharedPreferenciesUtil.setPreference(getActivity(), BICI_FACIL, biciFacil);
				} else {
					biciFacil = value;
				}
			} else if (key.equals(BICI_MEDIO)) {
				String value = sharedPreferences.getString(BICI_MEDIO, DificultadHelper.RANGO_BICI_2);
				if (!checkValueBetween(value, biciFacil, biciDificil)) {
					SharedPreferenciesUtil.setPreference(getActivity(), BICI_MEDIO, biciMedio);
				} else {
					biciMedio = value;
				}
			} else {
				String value = sharedPreferences.getString(BICI_DIFICIL, DificultadHelper.RANGO_BICI_3);
				if (!checkValueBigger(value, biciMedio)) {
					SharedPreferenciesUtil.setPreference(getActivity(), BICI_DIFICIL, biciDificil);
				} else {
					biciDificil = value;
				}
			}
		}
		
		private void setAccesible(SharedPreferences sharedPreferences, String key) {

			if (key.equals(ACCESIBLE_FACIL)) {
				String value = sharedPreferences.getString(ACCESIBLE_FACIL, DificultadHelper.RANGO_SILLA_1);
				if (!checkValueLower(value, accesibleMedio)) {
					SharedPreferenciesUtil.setPreference(getActivity(), ACCESIBLE_FACIL, accesibleFacil);
				} else {
					accesibleFacil = value;
					getPreferenceScreen().findPreference(ACCESIBLE_FACIL).setSummary(accesibleFacil);
				}
			} else if (key.equals(ACCESIBLE_MEDIO)) {
				String value = sharedPreferences.getString(ACCESIBLE_MEDIO, DificultadHelper.RANGO_SILLA_2);
				if (!checkValueBetween(value, accesibleFacil, accesibleDificil)) {
					SharedPreferenciesUtil.setPreference(getActivity(), ACCESIBLE_MEDIO, accesibleMedio);
				} else {
					accesibleMedio = value;
					getPreferenceScreen().findPreference(ACCESIBLE_MEDIO).setSummary(accesibleMedio);
				}
			} else {
				String value = sharedPreferences.getString(ACCESIBLE_DIFICIL, DificultadHelper.RANGO_SILLA_3);
				if (!checkValueBigger(value, accesibleMedio)) {
					SharedPreferenciesUtil.setPreference(getActivity(), ACCESIBLE_DIFICIL, accesibleDificil);
				} else {
					accesibleDificil = value;
					getPreferenceScreen().findPreference(ACCESIBLE_DIFICIL).setSummary(accesibleDificil);
				}
			}
		}

		private boolean checkValueBigger(String value, String valueUp) {
			return Integer.valueOf(value) > Integer.valueOf(valueUp);
		}

		private boolean checkValueBetween(String value, String valueDown,
				String valueUp) {			
			return (Integer.valueOf(value) > Integer.valueOf(valueDown)) &&
					Integer.valueOf(value) < Integer.valueOf(valueUp);
		}

		private boolean checkValueLower(String value, String valueDown) {			
			return Integer.valueOf(value) < Integer.valueOf(valueDown);
		}
	}  
}
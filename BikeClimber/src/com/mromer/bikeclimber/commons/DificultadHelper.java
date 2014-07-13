package com.mromer.bikeclimber.commons;

import com.mromer.bikeclimber.PreferenceWithHeaders;
import com.mromer.bikeclimber.R;
import com.mromer.bikeclimber.utils.SharedPreferenciesUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Calcula la dificultad en función del medio de
 * transporte.
 * */
public class DificultadHelper {	
	
	public static String RANGO_ANDANDO_1 = "4";
	public static String RANGO_ANDANDO_2 = "8";
	public static String RANGO_ANDANDO_3 = "20";	
	
	public static String RANGO_BICI_1 = "4";
	public static String RANGO_BICI_2 = "8";
	public static String RANGO_BICI_3 = "20";	
	
	public static String RANGO_SILLA_1 = "2";
	public static String RANGO_SILLA_2 = "4";
	public static String RANGO_SILLA_3 = "20";	

	public static Drawable getIconoDificultad(Context context, double pendiente, int medioTransporte) {
		if (medioTransporte == ConstantesMain.MEDIO_ANDANDO) {
			return getDificultadAndando(context, pendiente);
		} else if (medioTransporte == ConstantesMain.MEDIO_BICI) {
			return getDificultadBici(context, pendiente);
		} else if (medioTransporte == ConstantesMain.MEDIO_ACCESIBLE){
			return getDificultadAccesible(context, pendiente);
		} else {
			return getDificultadAccesible(context, pendiente);
		}
	}

	private static Drawable getDificultadAndando(Context context, double pendiente) {
		int andandoFacil = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.ANDANDO_FACIL);		
		andandoFacil = (andandoFacil == 0) ? Integer.parseInt(RANGO_ANDANDO_1) : andandoFacil;
		int andandoMedio = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.ANDANDO_MEDIO);		
		andandoMedio = (andandoMedio == 0) ? Integer.parseInt(RANGO_ANDANDO_2) : andandoMedio;
		int andandoDificil = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.ANDANDO_DIFICIL);		
		andandoDificil = (andandoDificil == 0) ? Integer.parseInt(RANGO_ANDANDO_3) : andandoDificil;
		
		Resources res = context.getResources();
		if (pendiente <= andandoFacil) {
			return res.getDrawable(R.drawable.walking0);
		}else if (pendiente > andandoFacil && pendiente <= andandoMedio) {
			return res.getDrawable(R.drawable.walking1);
		} else if (pendiente > andandoMedio && pendiente < andandoDificil) {			
			return res.getDrawable(R.drawable.walking2);
		} else {
			return res.getDrawable(R.drawable.walking3);
		}
	}

	private static Drawable getDificultadBici(Context context, double pendiente) {
		int biciFacil = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.BICI_FACIL);		
		biciFacil = (biciFacil == 0) ? Integer.parseInt(RANGO_BICI_1) : biciFacil;
		int biciMedio = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.BICI_MEDIO);		
		biciMedio = (biciMedio == 0) ? Integer.parseInt(RANGO_BICI_2) : biciMedio;
		int biciDificil = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.BICI_DIFICIL);		
		biciDificil = (biciDificil == 0) ? Integer.parseInt(RANGO_BICI_3) : biciDificil;
		
		Resources res = context.getResources();
		if (pendiente <= biciFacil) {
			return res.getDrawable(R.drawable.bici0);
		}else if (pendiente > biciFacil && pendiente <= biciMedio) {
			return res.getDrawable(R.drawable.bici1);
		} else if (pendiente > biciMedio && pendiente < biciDificil) {			
			return res.getDrawable(R.drawable.bici2);
		} else {
			return res.getDrawable(R.drawable.bici3);
		}
	}	

	private static Drawable getDificultadAccesible(Context context, double pendiente) {
		int accesibleFacil = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.ACCESIBLE_FACIL);		
		accesibleFacil = (accesibleFacil == 0) ? Integer.parseInt(RANGO_SILLA_1) : accesibleFacil;
		int accesibleMedio = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.ACCESIBLE_MEDIO);		
		accesibleMedio = (accesibleMedio == 0) ? Integer.parseInt(RANGO_SILLA_2) : accesibleMedio;
		int accesibleDificil = SharedPreferenciesUtil.getValue(context, PreferenceWithHeaders.ACCESIBLE_DIFICIL);		
		accesibleDificil = (accesibleDificil == 0) ? Integer.parseInt(RANGO_SILLA_3) : accesibleDificil;
		
		Resources res = context.getResources();
		if (pendiente <= accesibleFacil) {
			return res.getDrawable(R.drawable.iconoaccesible0);
		}else if (pendiente > accesibleFacil && pendiente <= accesibleMedio) {
			return res.getDrawable(R.drawable.iconoaccesible1);
		} else if (pendiente > accesibleMedio && pendiente < accesibleDificil) {			
			return res.getDrawable(R.drawable.iconoaccesible2);
		} else {
			return res.getDrawable(R.drawable.iconoaccesible3);
		}
	}
}
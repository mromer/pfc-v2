package com.mromer.bikeclimber.commons;

import com.mromer.bikeclimber.R;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Calcula la dificultad en función del medio de
 * transporte.
 * */
public class DificultadHelper {
	
	private static double RANGO_ANDANDO_0 = 0.0;
	private static double RANGO_ANDANDO_1 = 4.0;
	private static double RANGO_ANDANDO_2 = 8.0;
	private static double RANGO_ANDANDO_3 = 20.0;
	
	private static double RANGO_BICI_0 = 0.0;
	private static double RANGO_BICI_1 = 4.0;
	private static double RANGO_BICI_2 = 8.0;
	private static double RANGO_BICI_3 = 20.0;
	
	private static double RANGO_SILLA_0 = 0.0;
	private static double RANGO_SILLA_1 = 2.0;
	private static double RANGO_SILLA_2 = 4.0;
	private static double RANGO_SILLA_3 = 20.0;	

	public static Drawable getIconoDificultad(Resources res, double pendiente, int medioTransporte) {
		if (medioTransporte == ConstantesMain.MEDIO_ANDANDO) {
			return getDificultadAndando(res, pendiente);
		} else if (medioTransporte == ConstantesMain.MEDIO_BICI) {
			return getDificultadBici(res, pendiente);
		} else if (medioTransporte == ConstantesMain.MEDIO_ACCESIBLE){
			return getDificultadAccesible(res, pendiente);
		} else {
			return getDificultadAccesible(res, pendiente);
		}
	}

	private static Drawable getDificultadAndando(Resources res, double pendiente) {
		if (pendiente >= RANGO_ANDANDO_0 && pendiente <= RANGO_ANDANDO_1) {
			return res.getDrawable(R.drawable.walking0);
		}else if (pendiente > RANGO_ANDANDO_1 && pendiente <= RANGO_ANDANDO_2) {
			return res.getDrawable(R.drawable.walking1);
		} else if (pendiente > RANGO_ANDANDO_2 && pendiente < RANGO_ANDANDO_3) {			
			return res.getDrawable(R.drawable.walking2);
		} else {
			return res.getDrawable(R.drawable.walking3);
		}
	}

	private static Drawable getDificultadBici(Resources res, double pendiente) {
		if (pendiente >= RANGO_BICI_0 && pendiente <= RANGO_BICI_1) {
			return res.getDrawable(R.drawable.bici0);
		}else if (pendiente > RANGO_BICI_1 && pendiente <= RANGO_BICI_2) {
			return res.getDrawable(R.drawable.bici1);
		} else if (pendiente > RANGO_BICI_2 && pendiente < RANGO_BICI_3) {			
			return res.getDrawable(R.drawable.bici2);
		} else {
			return res.getDrawable(R.drawable.bici3);
		}
	}	

	private static Drawable getDificultadAccesible(Resources res, double pendiente) {
		if (pendiente >= RANGO_SILLA_0 && pendiente <= RANGO_SILLA_1) {
			return res.getDrawable(R.drawable.iconoaccesible0);
		}else if (pendiente > RANGO_SILLA_1 && pendiente <= RANGO_SILLA_2) {
			return res.getDrawable(R.drawable.iconoaccesible1);
		} else if (pendiente > RANGO_SILLA_2 && pendiente < RANGO_SILLA_3) {			
			return res.getDrawable(R.drawable.iconoaccesible2);
		} else {
			return res.getDrawable(R.drawable.iconoaccesible3);
		}
	}
}
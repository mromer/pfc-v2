package com.mromer.bikeclimber.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.google.android.gms.maps.model.LatLng;
import com.mromer.bikeclimber.bean.Location;
import com.mromer.bikeclimber.bean.Polyline;
import com.mromer.bikeclimber.bean.Route;
import com.mromer.bikeclimber.bean.Step;
import com.mromer.bikeclimber.bean.Track;
import com.mromer.bikeclimber.bean.Trackpoint;


public class PolylineUtil {

	/**
	 * Extrae todas las polylineas de una ruta. Devuelve un listado con todas ellas. 
	 * */
	public static ArrayList<Location> extraerLocalizaciones(Route route) {
		ArrayList<Location> result = new ArrayList<Location>();		

		List<Step> steps = route.listadoLeg.get(0).listadoSteps;
		int i = 0;
		for (Step step : steps) {
			
			Polyline polyline = new Polyline(step.polyline.points, step.polyline.points.length());

			
			for(LatLng inPoint : polyline) {
				i++;
				// Limite de la api
				if (i >= 500) {
//					MainActivityDirection.isRouteBig = true;
					break;
				}
				Location loc = new Location();

//				loc.latitud = inPoint.latitude / 1e6;
//				loc.longitud = inPoint.longitude / 1e6;	
				
				loc.latitud = inPoint.latitude;
				loc.longitud = inPoint.longitude;	

				result.add(loc);
			}
		}

		return result;
	}


	public static String getPolyline(ArrayList<Location> listadoLocation) {
		String resultado = null;	

		Track track = new Track();

		for (Location loc : listadoLocation) {
			Trackpoint tp = new Trackpoint(loc.latitud, loc.longitud);
			track.addTrackpoint(tp);
		}

		HashMap<String,String> encodedPolyline = PolylineEncoder.createEncodings(track, 17, 1);

		resultado = encodedPolyline.get("encodedPoints");

		return resultado;
	}


}

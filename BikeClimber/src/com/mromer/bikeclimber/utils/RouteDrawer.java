package com.mromer.bikeclimber.utils;

import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mromer.bikeclimber.R;
import com.mromer.bikeclimber.bean.ElevationPoint;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;

public class RouteDrawer {
	
	private GoogleMap mapa;
	private Activity context;	
	
	private static final int GROSOR_LINEA_NEGRA = 17;
	private static final int GROSOR_LINEA_RUTA_SELECCIONADA = 15;
	private static final int GROSOR_LINEA_RUTA_NO_SELECCIONADA = 5;	
	
	public RouteDrawer(Activity context, GoogleMap mapa) {		
		this.context = context;
		this.mapa = mapa;
	}
	
	public void pintarRuta(ElevationSearchResponse ruta, boolean seleccionada) {

		double pendienteMaxima = ruta.getPendienteMaxima();

		List<ElevationPoint> listadoPuntos = ruta.getListadoPuntos();				

		ElevationPoint elevationPointPrev = null;					

		int grosor = getGrosor(seleccionada);

		// Tenemos que pintar el último overlay la ruta seleccionada		
		for (ElevationPoint elevationPoint : listadoPuntos) {

			if (elevationPointPrev != null) {
				LatLng puntoInicio = new LatLng(elevationPointPrev.location.latitud, elevationPointPrev.location.longitud);
				LatLng puntoFin = new LatLng(elevationPoint.location.latitud, elevationPoint.location.longitud);
				int color = getColorLinea(elevationPoint.pendiente, pendienteMaxima, seleccionada);

				PolylineOptions polylineOptions = new PolylineOptions().add(puntoInicio, puntoFin);						
				polylineOptions.width(grosor);
				polylineOptions.color(color);		

				if (seleccionada) {
					// Indicamos que se pinte lo último
					polylineOptions.zIndex(10);
				} else {					
					polylineOptions.zIndex(3);
				}				

				mapa.addPolyline(polylineOptions);				

			}

			elevationPointPrev = elevationPoint;			
		}
	}
	
	
	public void pintarRutaNegra(ElevationSearchResponse ruta) {

		List<ElevationPoint> listadoPuntos = ruta.getListadoPuntos();				

		ElevationPoint elevationPointPrev = null;		

		// Tenemos que pintar el último overlay la ruta seleccionada
		for (ElevationPoint elevationPoint : listadoPuntos) {

			if (elevationPointPrev != null) {
				LatLng puntoInicio = new LatLng(elevationPointPrev.location.latitud, elevationPointPrev.location.longitud);
				LatLng puntoFin = new LatLng(elevationPoint.location.latitud, elevationPoint.location.longitud);
				int color = 0xff000000;

				PolylineOptions polylineOptions = new PolylineOptions().add(puntoInicio, puntoFin);						
				polylineOptions.width(GROSOR_LINEA_NEGRA);
				polylineOptions.color(color);

				// Indicamos que se pinte lo último
				polylineOptions.zIndex(5);

				mapa.addPolyline(polylineOptions);


			} else {
				elevationPointPrev = null;
			}

			elevationPointPrev = elevationPoint;
		}
	}
	
	
	private int getColorLinea(double pendiente, double pendienteMaxima, boolean seleccionado) {

		int R = 0;
		int G = 255;		

		pendiente = Math.abs(pendiente);
		if (pendiente <= (pendienteMaxima/2)) {
			R = (int) Math.round(255 * pendiente / (pendienteMaxima/2));

		} else {
			R = 255;
			G = (int) Math.round(((pendiente-(pendienteMaxima/2)) * (-255) / (pendienteMaxima/2)) + 255);
		}

		if (seleccionado) {
			return rgbSeleccionado(R, G, 0);
		} else {
			return rgbNoSeleccionado(R, G, 0);
		}


	}

	public static int rgbSeleccionado(int red, int green, int blue) {
		return (0xFF << 24) | (red << 16) | (green << 8) | blue;
	}

	public static int rgbNoSeleccionado(int red, int green, int blue) {
		return (0xAA << 24) | (red << 16) | (green << 8) | blue;
	}


	private int getGrosor(boolean seleccionada) {

		if (seleccionada) {
			return GROSOR_LINEA_RUTA_SELECCIONADA;
		} else {
			return GROSOR_LINEA_RUTA_NO_SELECCIONADA;
		}

	}
	
	public void pintarEscala (ElevationSearchResponse route, Double penditeneMaximaTotal) {		

		Resources res = context.getResources();

		Bitmap bitmapEscala = BitmapFactory.decodeResource(res, R.drawable.escala).copy(Bitmap.Config.ARGB_8888, true);		

		Bitmap bitmapCuadradoMedia = BitmapFactory.decodeResource(res, R.drawable.marcadormedia).copy(Bitmap.Config.ARGB_8888, true);

		Bitmap bitmapCuadradoMaxima = BitmapFactory.decodeResource(res, R.drawable.marcadormax).copy(Bitmap.Config.ARGB_8888, true);

		new BitMapUtils().getOverlayGradiente(bitmapEscala, bitmapCuadradoMedia, bitmapCuadradoMaxima,
				route.getPendienteMedia(), route.getPendienteMaxima(), penditeneMaximaTotal);

		ImageView escala = (ImageView) context.findViewById(R.id.escala);
		escala.setImageBitmap(bitmapEscala);	

		TextView textoDerecha =  (TextView) context.findViewById(R.id.textoDer);
		textoDerecha.setText((int) route.getPendienteMaxima() + "%");

		TextView textoIzq =  (TextView) context.findViewById(R.id.textoIzq);
		textoIzq.setText("0%");
	}

}

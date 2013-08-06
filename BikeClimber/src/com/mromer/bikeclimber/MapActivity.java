package com.mromer.bikeclimber;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mromer.bikeclimber.bean.ElevationPoint;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;
import com.mromer.bikeclimber.task.GetRoutesTask;
import com.mromer.bikeclimber.task.GetRoutesTaskResultI;

public class MapActivity extends FragmentActivity {	

	private static final int GROSOR_LINEA_RUTA_SELECCIONADA = 5;
	private static final int GROSOR_LINEA_RUTA_NO_SELECCIONADA = 5;

	private GoogleMap mapa;

	private List<ElevationSearchResponse> listElevationSearchResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		LatLng latLng = new LatLng(36.724335, -4.422737);

		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));

		new GetRoutesTask(new GetRoutesTaskResultI() {

			@Override
			public void taskSuccess(List<ElevationSearchResponse> result) {

				listElevationSearchResponse = result;

				// Para cada ruta...
				for (ElevationSearchResponse  elevationSearchResponse : listElevationSearchResponse) {

					double pendienteMaxima = elevationSearchResponse.pendienteMaxima;

					List<ElevationPoint> listadoPuntos = elevationSearchResponse.listadoPuntos;				

					ElevationPoint elevationPointPrev = null;
					for (ElevationPoint elevationPoint : listadoPuntos) {									

						if (elevationPointPrev != null) {
							LatLng puntoInicio = new LatLng(elevationPointPrev.location.latitud, elevationPointPrev.location.longitud);
							LatLng puntoFin = new LatLng(elevationPoint.location.latitud, elevationPoint.location.longitud);
							int color = getColorLinea(elevationPoint.pendiente, pendienteMaxima);
							int grosor = getGrosor(1);

							PolylineOptions polylineOptions = new PolylineOptions().add(puntoInicio, puntoFin);						
							polylineOptions.width(grosor);
							polylineOptions.color(color);

							mapa.addPolyline(polylineOptions);
						}

						elevationPointPrev = elevationPoint;

					}
				}

			}

			@Override
			public void taskFailure(String error) {
				// TODO Auto-generated method stub

			}
		}).execute();
	}



	private int getColorLinea(double pendiente, double pendienteMaxima) {

		int R = 0;
		int G = 255;

		pendiente = Math.abs(pendiente);
		if (pendiente <= (pendienteMaxima/2)) {
			R = (int) Math.round(255 * pendiente / (pendienteMaxima/2));

		} else {
			R = 255;
			G = (int) Math.round(((pendiente-(pendienteMaxima/2)) * (-255) / (pendienteMaxima/2)) + 255);
		}

		return rgb(R, G, 0);

	}

	public static int rgb(int red, int green, int blue) {
		return (0xFF << 24) | (red << 16) | (green << 8) | blue;
	}


	private int getGrosor(double pendiente) {

		return GROSOR_LINEA_RUTA_SELECCIONADA;
	}

}
package com.mromer.bikeclimber;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private static final float ZOOM = 13f;

	private GoogleMap mapa;

	private List<ElevationSearchResponse> listElevationSearchResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		LatLng inicio = new LatLng(36.714686, -4.444313);
		LatLng fin  = new LatLng(36.719829, -4.420002);	
		

		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, ZOOM));

		new GetRoutesTask(this, new GetRoutesTaskResultI() {

			@Override
			public void taskSuccess(List<ElevationSearchResponse> result) {

				listElevationSearchResponse = result;
				
				if (listElevationSearchResponse == null ||
						listElevationSearchResponse.size() == 0) {
					
					lanzarAlerta("No existen rutas con los criterios seleccionados"); 
					
					return;
					
				}

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
		}, inicio, fin).execute();
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
	
	private void lanzarAlerta(String mensaje) {	
	
		AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
        dialogo1.setTitle("Aviso");  
        dialogo1.setMessage(mensaje);            
        dialogo1.setCancelable(false);  
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialogo1, int id) {  
               
            }  
        });  
                  
        dialogo1.show(); 
        
        
	}

}
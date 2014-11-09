package com.mromer.bikeclimber.task;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.mromer.bikeclimber.bean.DireccionRequestGmaps;
import com.mromer.bikeclimber.bean.ElevationPoint;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;
import com.mromer.bikeclimber.bean.Location;
import com.mromer.bikeclimber.bean.Route;
import com.mromer.bikeclimber.bean.RoutesSearchResponse;
import com.mromer.bikeclimber.utils.ElevationRequestGmaps;
import com.mromer.bikeclimber.utils.PolylineUtil;


public class GetRoutesTask {
	
	private GetRoutesTaskResultI callBack;
	private LatLng inicio, fin;
	private Context contexto;
	private String medioTransporte;
	private String host;
	
	public GetRoutesTask (Context contexto, String host, GetRoutesTaskResultI callBack, 
			LatLng inicio, LatLng fin, String medioTransporte) {
		this.callBack = callBack;
		this.inicio = inicio;
		this.fin = fin;
		this.contexto = contexto;
		this.medioTransporte = medioTransporte;
		this.host = host;
	}

	public void execute() {
		new DoTask().execute("");
	}

	private class DoTask extends AsyncTask<String, Float, List<ElevationSearchResponse>>{
		ProgressDialog progress;
		
		protected void onPreExecute() {
			progress = new ProgressDialog(contexto);
			progress.setMessage("Cargando");
			progress.setCancelable(false);
			progress.show();
		}

		protected List<ElevationSearchResponse> doInBackground(String... urls) {
			
			List<ElevationSearchResponse> listRouteElevation = new ArrayList<ElevationSearchResponse>();

			
			DireccionRequestGmaps peticionesGmapsDirection = new DireccionRequestGmaps();


			try {

				// Prueba para M�laga
//				String json = peticionesGmapsDirection.obtenerRutas("36.714686", "-4.444313", "36.719829", "-4.420002");
				String json = peticionesGmapsDirection.obtenerRutas(
						host,
						Double.toString(inicio.latitude),
						Double.toString(inicio.longitude),
						Double.toString(fin.latitude),
						Double.toString(fin.longitude), medioTransporte);


				RoutesSearchResponse routesSearchResponse = peticionesGmapsDirection.obtenerGson(json);

				// 1� Extraemos los puntos de todas las polylineas de una ruta
				ArrayList<ArrayList<Location>> listadoLocalizaciones = new ArrayList<ArrayList<Location>>();
				for (Route route : routesSearchResponse.listadoRoutes){
					if (route != null){
						listadoLocalizaciones.add(PolylineUtil.extraerLocalizaciones(route));
					}
				}

				// 2� Construimos una polylinea conjunta
				ArrayList<String> listPolyline = new ArrayList<String>();
				for (ArrayList<Location> locations : listadoLocalizaciones) {						
					listPolyline.add(PolylineUtil.getPolyline(locations));
				}
				
				
				// 3� Consultamos su elevaci�n
				
				
				ElevationRequestGmaps peticionesGmapsElevation = new ElevationRequestGmaps();
				int j = 0;
				for (String polyline: listPolyline) {
					String jsonElevation = peticionesGmapsElevation.getElevationPolyline(host, polyline);

					ElevationSearchResponse elevationSearchResponse = peticionesGmapsElevation.obtenerGson(jsonElevation);

					elevationSearchResponse.setDistanciaText(routesSearchResponse.listadoRoutes.get(j).listadoLeg.get(0).distance.distanciaText);
					elevationSearchResponse.setDistanciaValue(routesSearchResponse.listadoRoutes.get(j).listadoLeg.get(0).distance.distanciaValue);

					listRouteElevation.add(elevationSearchResponse);

					j++;

				}
				
				// Calculamos la pendiente entre cada par de puntos
				for (ElevationSearchResponse evationSearchResponse: listRouteElevation) {

					// Inicializamos estad�sticos
					double distanciaSubidaAcumuladaParcial = 0.0;
					double direfenciasDeAlturasAcumuladasParcial = 0.0;

					int i = 0;
					ElevationPoint puntoAnterior = null; 
					for(ElevationPoint elevationPoint : evationSearchResponse.getListadoPuntos()) {				
						if (i == 0) {								
							i++;
						} else {						 				
							calculoPendiente(puntoAnterior, elevationPoint);
						}	

						puntoAnterior = elevationPoint;
						distanciaSubidaAcumuladaParcial = distanciaSubidaAcumuladaParcial + elevationPoint.distanciaEnPendiente;
						direfenciasDeAlturasAcumuladasParcial = direfenciasDeAlturasAcumuladasParcial + elevationPoint.diferenciaDeAltura;


						// Pendiente m�xima de la ruta
						if (elevationPoint.pendiente > evationSearchResponse.getPendienteMaxima() ) {
							evationSearchResponse.setPendienteMaxima(elevationPoint.pendiente);
						}

					}

					evationSearchResponse.setDistanciaSubidaAcumulada(distanciaSubidaAcumuladaParcial);
					evationSearchResponse.setDirefenciasDeAlturasAcumuladas(direfenciasDeAlturasAcumuladasParcial);

					if (distanciaSubidaAcumuladaParcial == 0) {
						evationSearchResponse.setDificuldad(0);			
						evationSearchResponse.setPendienteMedia(0);	
					} else {
						evationSearchResponse.setDificuldad(direfenciasDeAlturasAcumuladasParcial / distanciaSubidaAcumuladaParcial);			
						evationSearchResponse.setPendienteMedia(direfenciasDeAlturasAcumuladasParcial * 100 / distanciaSubidaAcumuladaParcial);	
					}

				}

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return listRouteElevation;
		}


		protected void onPostExecute(List<ElevationSearchResponse> result) {
			try {
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
			} catch (final IllegalArgumentException e) {
				// Handle or log or ignore
			} catch (final Exception e) {
				// Handle or log or ignore
			} finally {
				progress = null;
			}			
			callBack.taskSuccess(result);
		}
	}
	
	
	/**
	 * Calcula la pendiente en % y la distancia subida o bajada y la diferencia de alturas.
	 * */
	private static void calculoPendiente(ElevationPoint p1, ElevationPoint p2) {

		double pendiente = 0.0;		
		double distanciaEnPendiente = 0.0;
		double subidaVertical = 0.0;

		float[] results = new float[3];
		android.location.Location.distanceBetween(p1.location.latitud, p1.location.longitud, 
				p2.location.latitud, p2.location.longitud, results);

		float distanciaAB = results[0];		

		if (distanciaAB > 0 ) {
			subidaVertical = p2.elevacion - p1.elevacion;
			pendiente = subidaVertical * 100 / distanciaAB;			
		}

		distanciaEnPendiente = Math.hypot(distanciaAB, Math.abs(p2.elevacion - p1.elevacion));

		p2.pendiente = Math.abs(pendiente);		
		p2.distanciaEnPendiente =  Math.abs(distanciaEnPendiente);
		p2.diferenciaDeAltura =  Math.abs(subidaVertical);

	}

}

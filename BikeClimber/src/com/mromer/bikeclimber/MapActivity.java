package com.mromer.bikeclimber;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mromer.bikeclimber.adapter.ListRutasDialogAdapter;
import com.mromer.bikeclimber.bean.ElevationPoint;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;
import com.mromer.bikeclimber.task.GetRoutesTask;
import com.mromer.bikeclimber.task.GetRoutesTaskResultI;
import com.mromer.bikeclimber.utils.BitMapUtils;

public class MapActivity extends ActionBarActivity {	

	private static final int GROSOR_LINEA_NEGRA = 17;

	private static final int GROSOR_LINEA_RUTA_SELECCIONADA = 15;
	private static final int GROSOR_LINEA_RUTA_NO_SELECCIONADA = 5;
	private static final float ZOOM = 14f;

	private GoogleMap mapa;

	private double penditeneMaximaTotal = 0;

	private int bestRouteIndex;
	private int rutaSeleccionadaIndex;

	private Dialog dialogoRutas;

	private List<ElevationSearchResponse> listElevationSearchResponse;

	private LatLng origenLatLng = new LatLng(36.714686, -4.444313);
	private LatLng destinoLatLng  = new LatLng(36.719829, -4.420002);	



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		ActionBar actionBar = getSupportActionBar();		

		int walking = 1;
		if (walking == 1) {
			actionBar.setIcon(R.drawable.walkingactionbar);
		} else {
			actionBar.setIcon(R.drawable.walkingactionbar)	;		
		}

		actionBar.setDisplayHomeAsUpEnabled(true);

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		origenLatLng = new LatLng(36.714686, -4.444313);
		destinoLatLng  = new LatLng(36.719829, -4.420002);	

		mapa.addMarker(new MarkerOptions()
		.position(origenLatLng)
		.title("Calle inicio"));

		mapa.addMarker(new MarkerOptions()
		.position(destinoLatLng)
		.title("Calle fin"));


		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(origenLatLng, ZOOM));

		new GetRoutesTask(this, new GetRoutesTaskResultI() {

			@Override
			public void taskSuccess(List<ElevationSearchResponse> result) {

				listElevationSearchResponse = result;

				if (listElevationSearchResponse == null ||
						listElevationSearchResponse.size() == 0) {

					lanzarAlerta("No existen rutas con los criterios seleccionados"); 

					return;

				}

				bestRouteIndex = getBestRoute();
				int indexRoute = 0;

				// Para cada ruta...
				for (ElevationSearchResponse  elevationSearchResponse : listElevationSearchResponse) {					

					if (indexRoute != bestRouteIndex) {
						pintarRuta(elevationSearchResponse, false);
					}	

					// Aprobechamos para obtener la pendiente máxima de todas
					if (elevationSearchResponse.pendienteMaxima > penditeneMaximaTotal) {
						penditeneMaximaTotal = elevationSearchResponse.pendienteMaxima;
					}

					indexRoute++;
				}			

				// Pintamos la ruta seleccionada que coincide con la mejor				
				pintarRuta(listElevationSearchResponse.get(bestRouteIndex), true);
				pintarRutaNegra(listElevationSearchResponse.get(bestRouteIndex));

				rutaSeleccionadaIndex = bestRouteIndex;

				pintarEscala(listElevationSearchResponse.get(bestRouteIndex));

				dialogoRutas = crearDialogoListadoRutas();
				dialogoRutas.show();

			}		


			@Override
			public void taskFailure(String error) {
				// TODO Auto-generated method stub

			}
		}, origenLatLng, destinoLatLng).execute();
	}


	private void pintarRuta(ElevationSearchResponse ruta, boolean seleccionada) { 		


		double pendienteMaxima = ruta.pendienteMaxima;

		List<ElevationPoint> listadoPuntos = ruta.listadoPuntos;				

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

	private void pintarRutaNegra(ElevationSearchResponse ruta) {

		List<ElevationPoint> listadoPuntos = ruta.listadoPuntos;				

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



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			
			return true;

		case R.id.menu_list:
			if (!dialogoRutas.isShowing()) {
				dialogoRutas = crearDialogoListadoRutas();
				dialogoRutas.show();
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);
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

	private int getBestRoute() {	

		int best = 0;
		double bestDificultad = 0;
		int i = 0;
		for (ElevationSearchResponse elevationSearchResponse : listElevationSearchResponse) {	

			if (i == 0) {
				bestDificultad = elevationSearchResponse.dificuldad;
			} else if (elevationSearchResponse.dificuldad < bestDificultad){
				best = i;
				bestDificultad = elevationSearchResponse.dificuldad;
			}
			i++;
		}
		return best;
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

	private void pintarEscala (ElevationSearchResponse route) {		

		Resources res = getResources();

		Bitmap bitmapEscala = BitmapFactory.decodeResource(res, R.drawable.escala).copy(Bitmap.Config.ARGB_8888, true);		

		Bitmap bitmapCuadradoMedia = BitmapFactory.decodeResource(res, R.drawable.marcadormedia).copy(Bitmap.Config.ARGB_8888, true);

		Bitmap bitmapCuadradoMaxima = BitmapFactory.decodeResource(res, R.drawable.marcadormax).copy(Bitmap.Config.ARGB_8888, true);

		new BitMapUtils().getOverlayGradiente(bitmapEscala, bitmapCuadradoMedia, bitmapCuadradoMaxima,
				route.pendienteMedia, route.pendienteMaxima, penditeneMaximaTotal);

		ImageView escala = (ImageView) findViewById(R.id.escala);
		escala.setImageBitmap(bitmapEscala);	

		TextView textoDerecha =  (TextView) findViewById(R.id.textoDer);
		textoDerecha.setText((int) route.pendienteMaxima + "%");

		TextView textoIzq =  (TextView) findViewById(R.id.textoIzq);
		textoIzq.setText("0%");
	}

	/**
	 * Método que abre el dialog de selección de ruta.
	 */
	public  Dialog crearDialogoListadoRutas( ) {

		ListRutasDialogAdapter listAlertDialogAdapter = new ListRutasDialogAdapter(this
				, listElevationSearchResponse, rutaSeleccionadaIndex);		

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {


			public void onClick(DialogInterface dialog, int item) {

				if (item != rutaSeleccionadaIndex) {

					mapa.clear();

					mapa.addMarker(new MarkerOptions()
					.position(origenLatLng)
					.title("Calle inicio"));

					mapa.addMarker(new MarkerOptions()
					.position(destinoLatLng)
					.title("Calle fin"));

					rutaSeleccionadaIndex = item;

					int i = 0;

					for (ElevationSearchResponse route: listElevationSearchResponse){

						if (item != i) {
							pintarRuta(route, false);
						}
						i++;
					}

					pintarRuta(listElevationSearchResponse.get(item), true);				
					pintarRutaNegra(listElevationSearchResponse.get(item));

					pintarEscala(listElevationSearchResponse.get(item));

				}

				dialog.dismiss();
			}

		};


		builder.setAdapter(listAlertDialogAdapter, onClickListener);

		AlertDialog alert = builder.create();	

		return alert;
	}	

}
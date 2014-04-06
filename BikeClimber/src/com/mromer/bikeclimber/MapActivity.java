package com.mromer.bikeclimber;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mromer.bikeclimber.adapter.ListRutasDialogAdapter;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;
import com.mromer.bikeclimber.commons.ConstantesMain;
import com.mromer.bikeclimber.task.GetRoutesTask;
import com.mromer.bikeclimber.task.GetRoutesTaskResultI;
import com.mromer.bikeclimber.utils.RouteDrawer;

public class MapActivity extends ActionBarActivity {
	
	private static final float ZOOM = 14f;

	private GoogleMap mapa;

	private double penditeneMaximaTotal = 0;

	private int bestRouteIndex;
	private int rutaSeleccionadaIndex;

	private Dialog dialogoRutas;

	private List<ElevationSearchResponse> listElevationSearchResponse;

	private LatLng origenLatLng, destinoLatLng;	
	private String origenTitle, destinoTitle;

	private ActionBar actionBar;

	private String medioTransporte;
	
	private RouteDrawer routeDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		actionBar = getSupportActionBar();	
		
		Bundle bundle = getIntent().getExtras();
		
		Location locFrom = bundle.getParcelable(ConstantesMain.BUNDLE_FROM_LOCATION);
		Location locTo = bundle.getParcelable(ConstantesMain.BUNDLE_TO_LOCATION);
		origenTitle = bundle.getString(ConstantesMain.BUNDLE_FROM_TITLE);
		destinoTitle  = bundle.getString(ConstantesMain.BUNDLE_TO_TITLE);
		int medioSeleccionado = bundle.getInt(ConstantesMain.BUNDLE_MEDIO_SELECCIONADO);
		
		
		if (medioSeleccionado == ConstantesMain.MEDIO_ACCESIBLE) {
			actionBar.setIcon(R.drawable.iconoaccesibleactionbar);	
			medioTransporte = ConstantesMain.MEDIO_ANDANDO_QUERY;
			
		} else if (medioSeleccionado ==	ConstantesMain.MEDIO_BICI) {
			actionBar.setIcon(R.drawable.biciactionbar);
			medioTransporte = ConstantesMain.MEDIO_BICI_QUERY;
			
		} else {
			actionBar.setIcon(R.drawable.walkingactionbar);
			medioTransporte = ConstantesMain.MEDIO_ANDANDO_QUERY;
		}
		
		actionBar.setTitle("");

		actionBar.setDisplayHomeAsUpEnabled(true);

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

//		origenLatLng = new LatLng(36.714686, -4.444313);
//		destinoLatLng  = new LatLng(36.719829, -4.420002);
		
		origenLatLng = new LatLng(locFrom.getLatitude(), locFrom.getLongitude());
		destinoLatLng  = new LatLng(locTo.getLatitude(), locTo.getLongitude());

		mapa.addMarker(new MarkerOptions()
		.position(origenLatLng)
		.title(origenTitle));

		mapa.addMarker(new MarkerOptions()
		.position(destinoLatLng)
		.title(destinoTitle));


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
				
				// Route drawer
				routeDrawer = new RouteDrawer(MapActivity.this, mapa);

				// Para cada ruta...
				for (ElevationSearchResponse  elevationSearchResponse : listElevationSearchResponse) {					

					if (indexRoute != bestRouteIndex) {
						routeDrawer.pintarRuta(elevationSearchResponse, false);
					}

					// Aprobechamos para obtener la pendiente máxima de todas
					if (elevationSearchResponse.getPendienteMaxima() > penditeneMaximaTotal) {
						penditeneMaximaTotal = elevationSearchResponse.getPendienteMaxima();
					}
					indexRoute++;
				}			

				// Pintamos la ruta seleccionada que coincide con la mejor				
				routeDrawer.pintarRuta(listElevationSearchResponse.get(bestRouteIndex), true);
				routeDrawer.pintarRutaNegra(listElevationSearchResponse.get(bestRouteIndex));
				actionBar.setTitle("Ruta " + (bestRouteIndex+1));

				rutaSeleccionadaIndex = bestRouteIndex;

				routeDrawer.pintarEscala(listElevationSearchResponse.get(bestRouteIndex), penditeneMaximaTotal);

				dialogoRutas = crearDialogoListadoRutas();
				dialogoRutas.show();
			}		

			@Override
			public void taskFailure(String error) {
				// TODO Auto-generated method stub
			}
		}, origenLatLng, destinoLatLng, medioTransporte).execute();
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


	private int getBestRoute() {	

		int best = 0;
		double bestDificultad = 0;
		int i = 0;
		for (ElevationSearchResponse elevationSearchResponse : listElevationSearchResponse) {	

			if (i == 0) {
				bestDificultad = elevationSearchResponse.getDificuldad();
			} else if (elevationSearchResponse.getDificuldad() < bestDificultad){
				best = i;
				bestDificultad = elevationSearchResponse.getDificuldad();
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
					.title(origenTitle));

					mapa.addMarker(new MarkerOptions()
					.position(destinoLatLng)
					.title(destinoTitle));

					rutaSeleccionadaIndex = item;

					int i = 0;

					for (ElevationSearchResponse route: listElevationSearchResponse){

						if (item != i) {
							routeDrawer.pintarRuta(route, false);
						}
						i++;
					}

					routeDrawer.pintarRuta(listElevationSearchResponse.get(item), true);				
					routeDrawer.pintarRutaNegra(listElevationSearchResponse.get(item));
					actionBar.setTitle("Ruta " + (item+1));
					
					routeDrawer.pintarEscala(listElevationSearchResponse.get(item), penditeneMaximaTotal);

				}

				dialog.dismiss();
			}

		};


		builder.setAdapter(listAlertDialogAdapter, onClickListener);

		AlertDialog alert = builder.create();	

		return alert;
	}	

}
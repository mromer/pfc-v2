package com.mromer.bikeclimber.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mromer.bikeclimber.R;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;
import com.mromer.bikeclimber.commons.DificultadHelper;
import com.mromer.bikeclimber.utils.StringUtil;


public class ListRutasDialogAdapter extends BaseAdapter implements OnClickListener {	

	private Context context;
	private List<ElevationSearchResponse> listRutas;
	private int rutaSeleccionadaIndex;
	private int medioTransporte;
	private int bestRouteIndex;


	public ListRutasDialogAdapter(Context context, List<ElevationSearchResponse> listRutas, 
			int rutaSeleccionadaIndex, int medioTransporte, int bestRouteIndex) {
		this.context = context;
		this.listRutas = listRutas;
		this.rutaSeleccionadaIndex = rutaSeleccionadaIndex;	
		this.medioTransporte = medioTransporte;
		this.bestRouteIndex = bestRouteIndex;
	}


	@Override
	public int getCount() {
		return listRutas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listRutas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ElevationSearchResponse entry = listRutas.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_rutas_dialog, null);
		}		


		TextView texto = (TextView) convertView.findViewById(R.id.detalle_rutas);			
		texto.setText(context.getString(R.string.MAPA_DIALOG_RUTA) + " " + (position + 1));

		TextView textoFijoDistancia = (TextView) convertView.findViewById(R.id.textofijodistancia);			
		textoFijoDistancia.setText(context.getString(R.string.MAPA_DIALOG_DISTANCIA));

		TextView textoDistancia = (TextView) convertView.findViewById(R.id.distancia);			
		textoDistancia.setText(entry.getDistanciaText());


		// La mejor ruta lleva un icono

		ImageView imagenMejorRuta = (ImageView) convertView.findViewById(R.id.icono_mejor_ruta);
		if (position == bestRouteIndex) {			
			imagenMejorRuta.setVisibility(View.VISIBLE);
		} else {
			imagenMejorRuta.setVisibility(View.INVISIBLE);
		}

		// icono distancia media
		setIconoMedia(entry, convertView);		

		// icono distancia maxima
		setIconoMaxima(entry, convertView);		

		LinearLayout capa = (LinearLayout) convertView.findViewById(R.id.general_layout);
		// La ruta seleccionada tiene otro fondo
		if (position == rutaSeleccionadaIndex) {
			capa.setBackgroundResource(R.drawable.list_selector_color_seleccionado);
		} else {			
			capa.setBackgroundResource(R.drawable.list_selector_color_dialog);			
		}				

		return convertView;
	}

	private void setIconoMaxima(ElevationSearchResponse entry, View convertView) {
		// Set the text		
		TextView textoPendienteMaxima = (TextView) convertView.findViewById(R.id.pendientemaximaTexto);				
		textoPendienteMaxima.setText(context.getString(R.string.MAPA_DIALOG_MAX) 
				+ " " + StringUtil.oneDecimal(entry.getPendienteMaxima()) + "%");
		// Set the icon
		Drawable icon = getIconoPendiente(entry.getPendienteMaxima(), medioTransporte);		
		textoPendienteMaxima.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null);
	}


	private void setIconoMedia(ElevationSearchResponse entry, View convertView) {
		// Set the text
		TextView textoPendienteMedia = (TextView) convertView.findViewById(R.id.pendientemediaTexto);				
		textoPendienteMedia.setText(context.getString(R.string.MAPA_DIALOG_MEDIA) 
				+ " " + StringUtil.oneDecimal(entry.getPendienteMedia()) + "%");
		// Set the icon
		Drawable icon = getIconoPendiente(entry.getPendienteMedia(), medioTransporte);		
		textoPendienteMedia.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null);
	}	


	@Override
	public void onClick(View arg0) {
		Log.d("Debug", "Se ha hecho click en el listado" + arg0.getId());
	}


	/**
	 * Devuelve un drawable dependiendo de la pendiente y el medio
	 * de transporte.
	 * */
	public Drawable getIconoPendiente(double pendiente, int medioTransporte) {		
		return DificultadHelper.getIconoDificultad(context, pendiente, medioTransporte);
	}
}

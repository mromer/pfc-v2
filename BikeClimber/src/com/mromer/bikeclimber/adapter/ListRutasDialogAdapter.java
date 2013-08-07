package com.mromer.bikeclimber.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
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


public class ListRutasDialogAdapter extends BaseAdapter implements OnClickListener {


	private Context context;

	private List<ElevationSearchResponse> listRutas;

	private int rutaSeleccionadaIndex;

	public ListRutasDialogAdapter(Context context, List<ElevationSearchResponse> listRutas, int rutaSeleccionadaIndex) {
		this.context = context;
		this.listRutas = listRutas;
		this.rutaSeleccionadaIndex = rutaSeleccionadaIndex;
		
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
		textoDistancia.setText(entry.distanciaText);


		// La mejor ruta lleva un icono

		ImageView imagenMejorRuta = (ImageView) convertView.findViewById(R.id.icono_mejor_ruta);
		if (position == 0) {
			// TODO modificar el 0 por el index de la mejor ruta
			imagenMejorRuta.setVisibility(View.VISIBLE);
		} else {
			imagenMejorRuta.setVisibility(View.INVISIBLE);
		}


		ImageView iconoMedia = (ImageView) convertView.findViewById(R.id.pendientemedia);		
		iconoMedia.setImageDrawable(getDrawablePendienteMedia(entry.pendienteMedia));	
		TextView textoPendienteMedia = (TextView) convertView.findViewById(R.id.pendientemediaTexto);				
		textoPendienteMedia.setText(context.getString(R.string.MAPA_DIALOG_MEDIA) + " " + (int) Math.round(entry.pendienteMedia) + "%");
		
		ImageView iconoMax = (ImageView) convertView.findViewById(R.id.pendientemaxima);		
		iconoMax.setImageDrawable(getDrawablePendienteMedia(entry.pendienteMaxima));
		TextView textoPendienteMaxima = (TextView) convertView.findViewById(R.id.pendientemaximaTexto);				
		textoPendienteMaxima.setText(context.getString(R.string.MAPA_DIALOG_MAX) + " " + (int) Math.round(entry.pendienteMaxima) + "%");
				
		LinearLayout capa = (LinearLayout) convertView.findViewById(R.id.capa1);	

		// La ruta seleccionada tiene otro fondo
		if (position == rutaSeleccionadaIndex) {
			capa.setBackgroundResource(R.drawable.list_selector_color_seleccionado);			

		} else {			
			capa.setBackgroundResource(R.drawable.list_selector_color_dialog);			
		}
				
	
		return convertView;
	}

	@Override
	public void onClick(View arg0) {
		Log.d("Debug", "Se ha hecho click en el listado" + arg0.getId());		

	}
	

	public Drawable getDrawablePendienteMedia (double pendiente) {
		Resources res = context.getResources();
		if (pendiente >= 0.0 && pendiente <= 2.0) {
			return res.getDrawable(R.drawable.iconodificultad0);
		}else if (pendiente > 2.0 && pendiente <= 4.0) {
			return res.getDrawable(R.drawable.iconodificultad1);
		} else if (pendiente > 4.0 && pendiente < 20) {
			// TODO cambiar estos rangos
			return res.getDrawable(R.drawable.iconodificultad2);
		} else {
			return res.getDrawable(R.drawable.iconodificultad3);
		}
	}

}

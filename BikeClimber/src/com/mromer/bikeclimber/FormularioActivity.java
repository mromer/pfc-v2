package com.mromer.bikeclimber;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.mromer.bikeclimber.commons.ConstantesMain;
import com.mromer.bikeclimber.utils.GeocodeUtil;

public class FormularioActivity extends ActionBarActivity {	

	private int medioSeleccionado = ConstantesMain.MEDIO_ANDANDO;

	EditText editTextFrom, editTextTo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);


		editTextFrom = (EditText) findViewById(R.id.fromEditText);
		editTextTo = (EditText) findViewById(R.id.toEditText);

		listenersEditText();		

	}


	private void listenersEditText() {
		editTextFrom.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				String from = editTextFrom.getText().toString().trim();

				if (from != null && from.length() > 0 && !hasFocus) {

					GeocodeUtil geocodeUtil = new GeocodeUtil();
					List<String> lAddress = geocodeUtil.getAddresses(FormularioActivity.this,
							editTextFrom.getText().toString());

					Dialog dialog = crearDialogoAddress("Seleccione origen" , lAddress, editTextFrom);

					dialog.show();

				}

			}
		});


		editTextTo.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				String from = editTextTo.getText().toString().trim();

				if (from != null && from.length() > 0 && !hasFocus) {

					GeocodeUtil geocodeUtil = new GeocodeUtil();
					List<String> lAddress = geocodeUtil.getAddresses(FormularioActivity.this,
							editTextTo.getText().toString());

					Dialog dialog = crearDialogoAddress("Seleccione destino", lAddress, editTextTo);

					dialog.show();

				}

			}
		});

	}


	public void actionButton(View v) {
		
					
		switch (v.getId()) {
		case R.id.continuarBtn:		
			
			if (validarCampos()){
				Intent mIntent = new Intent(this, MapActivity.class);
				//			Bundle mBundle = new Bundle();
				//			mBundle.putInt(ConstantesMain.BUNDLE_MEDIO_SELECCIONADO, medioSeleccionado);
				//			mIntent.putExtras(mBundle);
				//
				//			startActivity(mIntent);
			}

			break;

		case R.id.walkingImg:

			medioSeleccionado(R.id.walkingImg);

			break;

		case R.id.bikingImg:

			medioSeleccionado(R.id.bikingImg);

			break;

		case R.id.wheelingImg:

			medioSeleccionado(R.id.wheelingImg);

			break;

		default:
			break;
		}

	}


	private boolean validarCampos() {
		if ((Boolean) editTextFrom.getTag() == false) {
			
			if (editTextFrom.getText() == null ||
					editTextFrom.getText().toString().trim().length() == 0) {
				lanzarAlerta("Seleccione origen");
				
			} else {
				GeocodeUtil geocodeUtil = new GeocodeUtil();
				List<String> lAddress = geocodeUtil.getAddresses(FormularioActivity.this,
						editTextFrom.getText().toString());

				Dialog dialog = crearDialogoAddress("Seleccione origen", lAddress, editTextFrom);

				dialog.show();
			}
			
			return false;
		}
		
		if ((Boolean) editTextTo.getTag() == false) {
			
			if (editTextTo.getText() == null ||
					editTextTo.getText().toString().trim().length() == 0) {
				lanzarAlerta("Seleccione destino");
				
			} else {				
				GeocodeUtil geocodeUtil = new GeocodeUtil();
				List<String> lAddress = geocodeUtil.getAddresses(FormularioActivity.this,
						editTextTo.getText().toString());

				Dialog dialog = crearDialogoAddress("Seleccione destino", lAddress, editTextTo);

				dialog.show();
			}
			
			return false;
		}
		
		return true;
		
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


	private Dialog crearDialogoAddress(String title, List<String> lAddress, final EditText editText) {

		final CharSequence[] arrayCharSequence = new CharSequence[lAddress.size()];

		int i = 0;
		for (String address : lAddress) {
			arrayCharSequence[i] = address;			
			i++;
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(arrayCharSequence, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				editText.setText(arrayCharSequence[item]);
				
				// Marcamos que se ha completado
				editText.setTag(new Boolean(true));
			}
		});
		AlertDialog alert = builder.create();		

		return alert;

	}


	private void medioSeleccionado(int idView) {

		// Deseleccionamos todos
		ImageView walkingImg = (ImageView) findViewById(R.id.walkingImg);
		ImageView bikingImg = (ImageView) findViewById(R.id.bikingImg);
		ImageView wheelingImg = (ImageView) findViewById(R.id.wheelingImg);

		walkingImg.setImageResource(R.drawable.selector_walking_off);
		bikingImg.setImageResource(R.drawable.selector_bici_off);
		wheelingImg.setImageResource(R.drawable.selector_accesible_off);

		ImageView vista = (ImageView) findViewById(idView);


		switch (idView) {		

		case R.id.walkingImg:
			medioSeleccionado = ConstantesMain.MEDIO_ANDANDO;
			vista.setImageResource(R.drawable.selector_walking_on);

			break;

		case R.id.bikingImg:
			medioSeleccionado = ConstantesMain.MEDIO_BICI;
			vista.setImageResource(R.drawable.selector_bici_on);

			break;

		case R.id.wheelingImg:
			medioSeleccionado = ConstantesMain.MEDIO_ACCESIBLE;
			vista.setImageResource(R.drawable.selector_accesible_on);

			break;

		default:
			break;
		}


	}

}
package com.mromer.bikeclimber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mromer.bikeclimber.commons.ConstantesMain;

public class FormularioActivity extends ActionBarActivity {	

	private int medioSeleccionado = ConstantesMain.MEDIO_ANDANDO;

	private TextView editTextFrom, editTextTo;

	private Address resultAddressFrom, resultAddressTo;

	private int mStackLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		editTextFrom = (TextView) findViewById(R.id.fromTextView);		
		editTextTo = (TextView) findViewById(R.id.toTextView);			

		editTextFrom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openAddressDialog("Seleccione origen", AddressDialogFragment.ACTION_FROM);				
			}			
		});
		
		
		editTextTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openAddressDialog("Seleccione destino", AddressDialogFragment.ACTION_TO);				
			}			
		});		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            setConfiguracion();
	            return true;	      
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void setConfiguracion() {		
		startActivity(new Intent(this, PreferenceWithHeaders.class));	
	}

	private void openAddressDialog(String title, String action) {		
		mStackLevel++;

	    // DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog.
	    DialogFragment newFragment = AddressDialogFragment.newInstance(mStackLevel, title, action);
	    newFragment.show(ft, "dialog");
	}
	

	public void actionButton(View v) {


		switch (v.getId()) {
		case R.id.continuarBtn:		

			if (validarCampos()){

				Location loc1 = new Location("");
				loc1.setLatitude(resultAddressFrom.getLatitude());
				loc1.setLongitude(resultAddressFrom.getLongitude());
				Location loc2 = new Location("");
				loc2.setLatitude(resultAddressTo.getLatitude());
				loc2.setLongitude(resultAddressTo.getLongitude());

				Intent mIntent = new Intent(this, MapActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putInt(ConstantesMain.BUNDLE_MEDIO_SELECCIONADO, medioSeleccionado);
				mBundle.putParcelable(ConstantesMain.BUNDLE_FROM_LOCATION, loc1);
				mBundle.putParcelable(ConstantesMain.BUNDLE_TO_LOCATION, loc2);
				mBundle.putString(ConstantesMain.BUNDLE_FROM_TITLE, editTextFrom.getText().toString());
				mBundle.putString(ConstantesMain.BUNDLE_TO_TITLE, editTextTo.getText().toString());
				mIntent.putExtras(mBundle);

				startActivity(mIntent);
			} else {
				lanzarAlerta("campos obligatorios");
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
	
	public void fillAddress(String action, String addressString, Address addressValue) {
		if (action.equals(AddressDialogFragment.ACTION_FROM)) {
			editTextFrom.setText(addressString);
			resultAddressFrom = addressValue;
		} else {
			editTextTo.setText(addressString);
			resultAddressTo = addressValue;
		}		
	}


	private boolean validarCampos() {	
		if (resultAddressFrom == null ||resultAddressTo == null) {
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
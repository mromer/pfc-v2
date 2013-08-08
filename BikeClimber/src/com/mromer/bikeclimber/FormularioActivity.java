package com.mromer.bikeclimber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

public class FormularioActivity extends ActionBarActivity {	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
	}


	public void actionButton(View v) {
		switch (v.getId()) {
		case R.id.continuarBtn:

			startActivity(new Intent(this, MapActivity.class));

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


	private void medioSeleccionado(int idView) {
		
		// Deseleccionamos todos
		ImageView walkingImg = (ImageView) findViewById(R.id.walkingImg);
		ImageView bikingImg = (ImageView) findViewById(R.id.bikingImg);
		ImageView wheelingImg = (ImageView) findViewById(R.id.wheelingImg);
		
		walkingImg.setImageResource(R.drawable.selector_walking_off);
		bikingImg.setImageResource(R.drawable.selector_walking_off);
		wheelingImg.setImageResource(R.drawable.selector_walking_off);
		
		ImageView vista = (ImageView) findViewById(idView);
		vista.setImageResource(R.drawable.selector_walking_on);
	}

}
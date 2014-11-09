package com.mromer.bikeclimber.test;

import android.support.v7.app.ActionBar;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.mromer.bikeclimber.FormularioActivity;
import com.mromer.bikeclimber.R;

public class FormularioActivityTest extends
		ActivityInstrumentationTestCase2<FormularioActivity> {

	private FormularioActivity mActivity;

	public FormularioActivityTest() {
		super(FormularioActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mActivity = getActivity();
	}

	/**
	 * Testeamos que tenemos el formulario con dos edit text y el button para
	 * aceptar.
	 * */
	public void testFormulario() {
		
		TextView editTextFrom = (TextView) mActivity.findViewById(R.id.fromTextView);		
		TextView editTextTo = (TextView) mActivity.findViewById(R.id.toTextView);
		
		// Existen los edit text
		assertTrue(editTextFrom != null);
		assertTrue(editTextTo != null);
		
		// Existe el action bar
		ActionBar actionBar = mActivity.getSupportActionBar();
		assertTrue(actionBar.isShowing());
	}
}

package com.mromer.bikeclimber.test.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.LatLng;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.mromer.bikeclimber.FormularioActivity;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;
import com.mromer.bikeclimber.commons.ConstantesMain;
import com.mromer.bikeclimber.task.GetRoutesTask;
import com.mromer.bikeclimber.task.GetRoutesTaskResultI;
import com.mromer.bikeclimber.test.R;

public class GetRoutesTaskTest extends ActivityInstrumentationTestCase2<FormularioActivity>{
	
	private static String HOST = "http://localhost";
	private static final int SERVER_PORT = 15650;
	
	private FormularioActivity mActivity;

	public GetRoutesTaskTest() {
		super(FormularioActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mActivity = getActivity();		
		
		createMockWebServer();
	}
	
	public void testTask() throws InterruptedException {		
		LatLng origenLatLng = new LatLng(36.711902, -4.632730);;
		LatLng destinoLatLng = new LatLng(36.712157, -4.629873);;
		String medioTransporteQuery = ConstantesMain.MEDIO_ANDANDO_QUERY;
		
		final CountDownLatch signal = new CountDownLatch(1);
		
		String host = HOST + ":" + SERVER_PORT;
		
		new GetRoutesTask(mActivity, host, new GetRoutesTaskResultI() {

			@Override
			public void taskSuccess(List<ElevationSearchResponse> result) {
				assertNotNull(result);
				signal.countDown();
			}		

			@Override
			public void taskFailure(String error) {
				assertTrue(false);
			}
		}, origenLatLng, destinoLatLng, medioTransporteQuery).execute();
		
		// Waiting until signal is woke up
		signal.await();
	}
	
	private void createMockWebServer() throws IOException {
		
		// Leer json de rutas		
		String elevationJson = toString(mActivity.getResources()
				.openRawResource(R.raw.elevation));
		String routesJson = toString(mActivity.getResources().openRawResource(
				R.raw.routes));

		MockWebServer server = new MockWebServer();
		// Schedule some responses.
		server.enqueue(new MockResponse().setBody(routesJson));
		server.enqueue(new MockResponse().setBody(elevationJson));
		
		// Start the server.
		server.play(SERVER_PORT);
	}
	
	private String toString(InputStream in) throws IOException {		
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb=new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();

		while(read != null) {
		    //System.out.println(read);
		    sb.append(read);
		    read =br.readLine();
		}

		return sb.toString();
	}
	

}

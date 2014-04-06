package com.mromer.bikeclimber.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.google.gson.Gson;



public class DireccionRequestGmaps {	

	public String obtenerRutas(String latitudDesde, String longitudDesde, 
			String latitudHasta, String longitudHasta, String medioTransporte) throws Exception {	
		
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		
		String endpoint = "http://maps.googleapis.com/maps/api/directions/json";
		String requestParameters =
				"origin=" + latitudDesde + "," + longitudDesde + 
				"&destination=" + latitudHasta + "," + longitudHasta + 
				"&mode=" + medioTransporte +
				"&alternatives=true" +
				"&sensor=false";

		
		String json = null;
		if (endpoint.startsWith("http://")) {
			// Send a GET request to the servlet
			try	{

				// Send data
				String urlStr = endpoint;
				Log.d("Debug", "Consulta de path:" + urlStr);
				if (requestParameters != null && requestParameters.length () > 0) {
					urlStr += "?" + requestParameters;
				}
				

				HttpClient client = new DefaultHttpClient(httpParameters);
				HttpGet request = new HttpGet(urlStr);				
				HttpResponse httpresponse = client.execute(request);


				BufferedReader reader = new BufferedReader(new InputStreamReader(
						httpresponse.getEntity().getContent(), "UTF-8"));

				String sResponse;
				StringBuilder s = new StringBuilder();

				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				json = s.toString();
				
				
			} catch (Exception e) {
				Log.d("Error", "Error obteniendo datos del servidor direccion");
				throw e;
			}
		}
		

		return json;
	}
	
	
	public RoutesSearchResponse obtenerGson(String json) {
		Gson gson = new Gson();
		RoutesSearchResponse searchResponse = gson.fromJson(json, RoutesSearchResponse.class);
		
		return searchResponse;
	}


}

package com.mromer.bikeclimber.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.google.gson.Gson;
import com.mromer.bikeclimber.bean.ElevationSearchResponse;



public class ElevationRequestGmaps {	
	
	public String getElevationPolyline(String host, String polyline) throws Exception {	
		
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		
		String endpoint = host + "/maps/api/elevation/json";
		String requestParameters = "locations=enc:" + URLEncoder.encode(polyline) + 
				"&sensor=false";
		
		String json = null;
		if (endpoint.startsWith("http://")) {
			// Send a GET request to the servlet
			try	{

				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length () > 0) {
					urlStr += "?" + requestParameters;
				}
								
								
				Log.d("Debug", "url de consulta:" + urlStr);
				
				HttpClient client = new DefaultHttpClient();
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
				Log.d("Error", "Error obteniendo datos del servidor elevation");
				throw e;
			}
		}		

		return json;
	}
	
	
	public ElevationSearchResponse obtenerGson(String json) {
		Gson gson = new Gson();
		ElevationSearchResponse searchResponse = gson.fromJson(json, ElevationSearchResponse.class);
		
		return searchResponse;
	}


}

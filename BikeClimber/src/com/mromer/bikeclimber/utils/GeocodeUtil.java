package com.mromer.bikeclimber.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GeocodeUtil {	
	
	public static String LIST_ADDRESS = "LIST_ADDRESS";
	public static String LIST_STRING = "LIST_STRING";
	
	public HashMap<String, Object> getAddresses(Context context, String paramAddress) {
		
		ArrayList<String> addressArray = new ArrayList<String>();		

		List<Address> listAddress = getAddressesGeocoder(context, paramAddress);
		
		for (Address address : listAddress) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
				sb.append(address.getAddressLine(i));
				if(address.getMaxAddressLineIndex()>i) sb.append(", ");
			}

			addressArray.add(sb.toString());
		}

//		Iterator<Address> iterator = listAddress.iterator();
//		
//		while (iterator.hasNext()) {
//			
//			Address tmpAddress = iterator.next();
//
//			StringBuffer sb = new StringBuffer();
//
//			for (int i = 0; i <= tmpAddress.getMaxAddressLineIndex(); i++){
//				sb.append(tmpAddress.getAddressLine(i));
//				if(tmpAddress.getMaxAddressLineIndex()>i)sb.append(", ");
//			}
//
//			addressArray.add(sb.toString());
//		}
		
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		resultado.put(LIST_ADDRESS, listAddress);
		resultado.put(LIST_STRING, addressArray);
		
		return resultado;
	}

	private List<Address> getAddressesGeocoder(Context context, String paramAddress) {
		Geocoder gc = new Geocoder(context);
	
		  List<Address> list = new ArrayList<Address>();
		try {
			  list = gc.getFromLocationName(paramAddress, 10);
		} catch (IOException e) {			
			e.printStackTrace();			  
		}
		  
		  return list;
	}

}

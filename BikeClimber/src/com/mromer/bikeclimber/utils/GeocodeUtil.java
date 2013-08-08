package com.mromer.bikeclimber.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GeocodeUtil {	
	
	
	public List<String> getAddresses(Context context, String paramAddress) {
		
		ArrayList<String> addressArray = new ArrayList<String>();		

		List<Address> listAddress = getAddressesGeocoder(context, paramAddress);

		Iterator<Address> iterator = listAddress.iterator();
		
		while (iterator.hasNext()) {
			
			Address tmpAddress = iterator.next();

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i <= tmpAddress.getMaxAddressLineIndex(); i++){
				sb.append(tmpAddress.getAddressLine(i));
				if(tmpAddress.getMaxAddressLineIndex()>i)sb.append(", ");
			}

			addressArray.add(sb.toString());
		}
		
		return addressArray;
	}

	private List<Address> getAddressesGeocoder(Context context, String paramAddress) {
		Geocoder gc = new Geocoder(context);
	
		  List<Address> list = new ArrayList<Address>();
		try {
			  list = gc.getFromLocationName(paramAddress, 1);
		} catch (IOException e) {			
			e.printStackTrace();			  
		}
		  
		  return list;
	}

}

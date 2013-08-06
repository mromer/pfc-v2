package com.mromer.bikeclimber.bean;

import com.google.gson.annotations.SerializedName;

public class Location {
	
	public int id;

	@SerializedName("lat")
	public Double latitud;
	
	@SerializedName("lng")
	public Double longitud;
}

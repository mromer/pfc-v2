package com.mromer.bikeclimber.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Route {
	
	@SerializedName("legs")
	public List<Leg> listadoLeg;	
	
}

package com.mromer.bikeclimber.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Leg {
	
	@SerializedName("steps")
	public List<Step> listadoSteps;	
	
	@SerializedName("distance")
	public Distance distance;	
	
	
}

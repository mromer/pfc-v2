package com.mromer.bikeclimber.bean;

import com.google.gson.annotations.SerializedName;

public class Step {
	@SerializedName("start_location")
	public Location locationStart;	
	
	
	@SerializedName("end_location")
	public Location locationEnd;
	
	@SerializedName("polyline")
	public Points polyline;
	
	
}

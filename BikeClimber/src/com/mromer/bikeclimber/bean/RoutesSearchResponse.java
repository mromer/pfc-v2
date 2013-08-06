package com.mromer.bikeclimber.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RoutesSearchResponse {
	
	@SerializedName("routes")
	public List<Route> listadoRoutes;
	
}

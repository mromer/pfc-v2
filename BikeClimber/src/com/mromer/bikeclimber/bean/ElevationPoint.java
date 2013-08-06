package com.mromer.bikeclimber.bean;

import com.google.gson.annotations.SerializedName;

public class ElevationPoint {
	@SerializedName("location")
	public Location location;	
	
	
	@SerializedName("elevation")
	public Double elevacion;
	
	/** Pendiente respecto al punto anterior.*/
	public double pendiente;
	
	/** Distancia absoluta de subida o bajada respecto al punto anterior.*/
	public double distanciaEnPendiente;
	
	/** Diferencia absoluta de altura respecto al punto anterior.*/
	public double diferenciaDeAltura;
	
}

package com.mromer.bikeclimber.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ElevationSearchResponse {
	
	@SerializedName("results")
	public List<ElevationPoint> listadoPuntos;
	
	public double distanciaSubidaAcumulada;
	
	public double direfenciasDeAlturasAcumuladas;
	
	public double dificuldad;
	
	public double pendienteMedia;
	
	public double pendienteMaxima;
	
	public String distanciaText;
	
	public int distanciaValue;
}

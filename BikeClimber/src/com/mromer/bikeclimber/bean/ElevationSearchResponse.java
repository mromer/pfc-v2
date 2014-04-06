package com.mromer.bikeclimber.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ElevationSearchResponse {
	
	@SerializedName("results")
	private List<ElevationPoint> listadoPuntos;
	
	private double distanciaSubidaAcumulada;
	
	private double direfenciasDeAlturasAcumuladas;
	
	private double dificuldad;
	
	private double pendienteMedia;
	
	private double pendienteMaxima;
	
	private String distanciaText;
	
	private int distanciaValue;

	public List<ElevationPoint> getListadoPuntos() {
		return listadoPuntos;
	}

	public void setListadoPuntos(List<ElevationPoint> listadoPuntos) {
		this.listadoPuntos = listadoPuntos;
	}

	public double getDistanciaSubidaAcumulada() {
		return distanciaSubidaAcumulada;
	}

	public void setDistanciaSubidaAcumulada(double distanciaSubidaAcumulada) {
		this.distanciaSubidaAcumulada = distanciaSubidaAcumulada;
	}

	public double getDirefenciasDeAlturasAcumuladas() {
		return direfenciasDeAlturasAcumuladas;
	}

	public void setDirefenciasDeAlturasAcumuladas(
			double direfenciasDeAlturasAcumuladas) {
		this.direfenciasDeAlturasAcumuladas = direfenciasDeAlturasAcumuladas;
	}

	public double getDificuldad() {
		return dificuldad;
	}

	public void setDificuldad(double dificuldad) {
		this.dificuldad = dificuldad;
	}

	public double getPendienteMedia() {
		return pendienteMedia;
	}

	public void setPendienteMedia(double pendienteMedia) {
		this.pendienteMedia = pendienteMedia;
	}

	public double getPendienteMaxima() {
		return pendienteMaxima;
	}

	public void setPendienteMaxima(double pendienteMaxima) {
		this.pendienteMaxima = pendienteMaxima;
	}

	public String getDistanciaText() {
		return distanciaText;
	}

	public void setDistanciaText(String distanciaText) {
		this.distanciaText = distanciaText;
	}

	public int getDistanciaValue() {
		return distanciaValue;
	}

	public void setDistanciaValue(int distanciaValue) {
		this.distanciaValue = distanciaValue;
	}
	
	
}

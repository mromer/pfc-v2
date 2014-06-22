package com.mromer.bikeclimber.utils;

public class StringUtil {

	/**
	 * Redondea un float a un decimal.
	 * */
	public static double oneDecimal(double originValue) {
		return (double)Math.round(originValue * 10) / 10;
	}

}

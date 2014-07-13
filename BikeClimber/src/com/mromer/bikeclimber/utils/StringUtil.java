package com.mromer.bikeclimber.utils;

public class StringUtil {

	/**
	 * Redondea un float a un decimal.
	 * */
	public static double oneDecimal(double originValue) {
		return (double)Math.round(originValue * 10) / 10;
	}
	

	public static boolean checkValueBigger(String value, String valueUp) {
		return Integer.valueOf(value) > Integer.valueOf(valueUp);
	}

	public static boolean checkValueBetween(String value, String valueDown,
			String valueUp) {			
		return (Integer.valueOf(value) > Integer.valueOf(valueDown)) &&
				Integer.valueOf(value) < Integer.valueOf(valueUp);
	}

	public static boolean checkValueLower(String value, String valueDown) {			
		return Integer.valueOf(value) < Integer.valueOf(valueDown);
	}

}

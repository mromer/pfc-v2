package com.mromer.bikeclimber.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BitMapUtils {

	public Bitmap getOverlayGradiente(Bitmap bitmapEscala, Bitmap bitmapCuadradoMedia, Bitmap bitmapCuadradoMaxima
			, double pendienteMedia, double pendienteMaxima, double penditeneMaximaTotal) {
		
	    Canvas canvas = new Canvas(bitmapEscala);
	    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);	    
	    canvas.drawBitmap(bitmapCuadradoMedia, posicion(bitmapEscala.getWidth(), bitmapCuadradoMedia.getWidth(), pendienteMedia, penditeneMaximaTotal), 0, paint);
	    canvas.drawBitmap(bitmapCuadradoMaxima, posicion(bitmapEscala.getWidth(), bitmapCuadradoMaxima.getWidth(), pendienteMaxima, penditeneMaximaTotal), 0, paint);
	    
	    return bitmapEscala;
	}
	
	private int posicion (int pixelsEscala, int pixelsOverlay, double pendiente, double pendienteMaxima) {
		
		if (pendiente >= pendienteMaxima) {
			return pixelsEscala- pixelsOverlay;
		}  else {
			int posicion =  (int) (pendiente*pixelsEscala/ pendienteMaxima);
			
			if (posicion > pixelsEscala - pixelsOverlay) {
				return pixelsEscala - pixelsOverlay;
			}
			return posicion;
		}
		
	}
		
}

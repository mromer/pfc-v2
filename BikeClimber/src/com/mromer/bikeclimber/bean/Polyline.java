/**
 * Class Polyline represents a single polyline. Stores the encoded polyline,
 * encoded levels, and coordinate points of the points on the polyline.
 * 
 * CAUTION: NOT YET FULLY TESTED.
 * 
 * @author Michael Eng
 */

package com.mromer.bikeclimber.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.android.gms.maps.model.LatLng;


public class Polyline implements Iterable<LatLng> {
        private String encodedPolyline;
        private String encodedLevels;
        private ArrayList<LatLng> coordinates;
        
        /**
         * Polyline constructor that stores the given encodedPolyline and number of levels
         * that follows Google's encoding standards.
         * @param encodedPolyline
         * @param encodedLevels
         */
        public Polyline(String encodedPolyline, int length) {
                coordinates = new ArrayList<LatLng>();
                StringBuilder sb = new StringBuilder();
                
                // The encodedLevels is a string of "B" of size length.
                for(int i = 0; i < length; i++) {
                        sb.append("B");
                }
                
                this.encodedPolyline = encodedPolyline;
                this.encodedLevels = sb.toString();
                this.decodeLine();
        }
        
        /**
         * Polyline constructor that stores the given encodedPolyline and encodedLevels
         * that follows Google's encoding standards.
         * @param encodedPolyline
         * @param encodedLevels
         */
        public Polyline(String encodedPolyline, String encodedLevels) {
                this.encodedPolyline = encodedPolyline;
                this.encodedLevels = encodedLevels;
        }
        
        /**
         * 
         * @return the encodedPolyline
         */
        public String getEncodedPolyline() {
                return encodedPolyline;
        }
        
        /**
         * 
         * @return the encodedLevels
         */
        public String getEncodedLevels() {
                return encodedLevels;
        }
        
        /**
         * @return an iterator for the list of GeoPoints.
         */
        public Iterator<LatLng> iterator() {
                PolylineIterator p = new PolylineIterator(coordinates);
                return p;
        }
        
        /**
         * Iterator class that iterates through the list of GeoPoints.
         * @author Michael Eng
         *
         */
        public class PolylineIterator implements Iterator<LatLng> {
                private int index;
                private ArrayList<LatLng> coordinates;
                
                /**
                 * Constructor for PolylineIterator. Takes list of GeoPoints for
                 * future use for iteration.
                 * @param coordinates
                 */
                public PolylineIterator(ArrayList<LatLng> coordinates) {
                        index = 0;
                        this.coordinates = coordinates;
                }
                
                /**
                 * @return true if there is an available GeoPoint.
                 */
                public boolean hasNext() {
                        return index < coordinates.size();
                }
                
                /**
                 * @returns the next GeoPoint in the list.
                 */
                public LatLng next() {
                        if(hasNext()) {
                                return coordinates.get(index++);
                        } else {
                                throw new NoSuchElementException();
                        }
                }
                
                /**
                 * This method is not supported in the implementation.
                 */
                public void remove() {
                        throw new UnsupportedOperationException();
                }
        }
        
        /**
         * This function is from Google's polyline utility written in Javascript
         * that I rewrote in Java.
         * Decodes the class' encodedPolyline and stores the GeoPoints in the 
         * list of coordinates.
         */
        private void decodeLine() {
          int len = this.encodedPolyline.length();
          int index = 0;
          int lat = 0;
          int lng = 0;

          while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
              b = this.encodedPolyline.charAt(index++) - 63;
              result |= (b & 0x1f) << shift;
              shift += 5;
            } while (b >= 0x20);
            int dlat = (((result & 1) != 0) ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
              b = this.encodedPolyline.charAt(index++) - 63;
              result |= (b & 0x1f) << shift;
              shift += 5;
            } while (b >= 0x20);
            int dlng = (((result & 1) != 0) ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            
            double latitud = lat * 10 / 1e6;
            double longitud = lng * 10 / 1e6;
            
            LatLng nextPoint = new LatLng(latitud, longitud);
            
            coordinates.add(nextPoint);
          }
        }
}

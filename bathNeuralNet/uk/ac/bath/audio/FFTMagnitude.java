
/*
 *
 * Copyright (c) 2006 P.J.Leonard
 * 
 * http://www.frinika.com
 * 
 * This file is part of Frinika.
 * 
 * Frinika is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * Frinika is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Frinika; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package uk.ac.bath.audio;


/**
 *
 * @author pjl
 */
public class FFTMagnitude {



    public void getData(float data[][],double fftOut[]) {

       float magnArray[]=data[0];

       for (int i = 0; i < data[0].length; i++) {
            double real = fftOut[2 * i];
            double imag = fftOut[2 * i + 1];

            magnArray[i] = (float) Math.sqrt(real * real + imag * imag);
			
        }
    } 
}

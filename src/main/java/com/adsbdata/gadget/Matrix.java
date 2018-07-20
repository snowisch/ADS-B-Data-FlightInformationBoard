package com.adsbdata.gadget;

public class Matrix {
	public static double [][] transPosition(double originalMatrix[][]) {
		double newMatrix[][] = new double[originalMatrix[0].length][originalMatrix.length];
	    for (int i = 0; i < originalMatrix[i].length; i++) {
	    	for (int j = 0; j < originalMatrix.length; j++) {
	    		newMatrix[i][j] = originalMatrix[j][i];
	    		//System.out.println(originalMatrix[i][j]);
	        }
	    }
		return newMatrix;
	}
}
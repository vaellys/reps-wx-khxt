package com.reps.khxt.util;

import java.text.DecimalFormat;

public class DigitalFormat {
	
	public static String format(Double number) {
		DecimalFormat df = new DecimalFormat("###.####");
		return df.format(number);
	}
	
}

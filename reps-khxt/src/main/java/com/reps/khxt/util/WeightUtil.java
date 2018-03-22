package com.reps.khxt.util;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeightUtil {
	
	/**
	 * 存储级别权重适用年度MAP
	 */
	public static final Map<String, String> APPLY_YEAR_RANGE = new LinkedHashMap<>();
	
	static {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		APPLY_YEAR_RANGE.put(String.valueOf(currentYear - 1),  String.valueOf(currentYear - 1));
		APPLY_YEAR_RANGE.put(String.valueOf(currentYear),  String.valueOf(currentYear));
		APPLY_YEAR_RANGE.put(String.valueOf(currentYear + 1),  String.valueOf(currentYear + 1));
	}

}

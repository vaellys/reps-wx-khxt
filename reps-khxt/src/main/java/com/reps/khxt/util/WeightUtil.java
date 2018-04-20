package com.reps.khxt.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.reps.core.exception.RepsException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	
	@SuppressWarnings("unchecked")
	public static String findWeightByLevelId(String levelId, String weightJson) throws RepsException {
		JSONArray weightArray = JSONArray.fromObject(weightJson);
		if (null != weightArray && !weightArray.isEmpty()) {
			for (Iterator<JSONObject> iterator = weightArray.iterator(); iterator.hasNext();) {
				JSONObject weightObj = iterator.next();
				String lId = weightObj.getString("levelId");
				if(levelId.equals(lId)) {
					return weightObj.getString("weight");
				}
			}
		}
		return "0";
	}

}

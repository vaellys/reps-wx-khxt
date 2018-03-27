package com.reps.khxt.service;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtPerformancePoint;

public interface IKhxtPerformancePointService {

	/**
	 * @param khxtPerformancePoint
	 * @throws RepsException
	 */
	public void save(KhxtPerformancePoint khxtPerformancePoint) throws RepsException;
	

}

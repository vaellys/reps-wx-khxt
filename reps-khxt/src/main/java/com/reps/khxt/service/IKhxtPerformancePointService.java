package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtPerformancePoint;

public interface IKhxtPerformancePointService {

	/**
	 * @param khxtPerformancePoint
	 * @throws RepsException
	 */
	public void save(KhxtPerformancePoint khxtPerformancePoint) throws RepsException;
	
	/**
	 * 修改个人考核评分
	 * @param khxtPerformancePoint
	 * @throws RepsException
	 */
	public void update(KhxtPerformancePoint khxtPerformancePoint) throws RepsException;
	
	/**
	 * @param id
	 * @return KhxtPerformancePoint
	 * @throws RepsException
	 */
	public KhxtPerformancePoint get(String id) throws RepsException;

	public List<KhxtPerformancePoint> find(KhxtPerformancePoint khxtPerformancePoint);
	
}

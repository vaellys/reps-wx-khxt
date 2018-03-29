package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtPerformanceWork;

public interface IKhxtPerformanceWorkService {
	
	public List<KhxtPerformanceWork> find(KhxtPerformanceWork khxtPerformanceWork) throws RepsException;
}

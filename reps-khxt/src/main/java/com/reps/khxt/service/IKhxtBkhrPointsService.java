package com.reps.khxt.service;

import com.reps.khxt.entity.KhxtPerformanceMembers;

/**
 * 统计被考核人合计总分
 * 
 * @author ：Alex
 * @date 2018年4月23日
 */
public interface IKhxtBkhrPointsService {


	void countScore(KhxtPerformanceMembers khxtPerformanceMembers) throws Exception;

}

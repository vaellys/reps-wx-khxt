package com.reps.khxt.service;

import com.reps.khxt.entity.KhxtBkhrPoints;

/**
 * 统计被考核人合计总分
 * 
 * @author ：Alex
 * @date 2018年4月23日
 */
public interface IKhxtBkhrPointsService {

	/**
	 * 保存合计总分
	 * 
	 * @author Alex
	 * @date 2018年4月23日
	 * @param bkhrPoints
	 * @return void
	 */
	void save(KhxtBkhrPoints bkhrPoints);

}

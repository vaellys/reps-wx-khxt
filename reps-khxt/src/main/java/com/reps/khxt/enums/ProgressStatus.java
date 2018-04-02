package com.reps.khxt.enums;

/**
 * 
 * @ClassName: ProgressStatus
 * @Description: 进度情况
 * @author qianguobing
 * @date 2018年3月29日 下午12:14:08
 */
public enum ProgressStatus {
	
	UN_FINISHED((short)0, "未完成"), FINISHED((short) 1, "已完成"), HAS_OVER((short)2, "已结束");
	
	private Short id;
	
	private String status;
	
	ProgressStatus(Short id, String status) {
		this.id = id;
		this.status = status;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

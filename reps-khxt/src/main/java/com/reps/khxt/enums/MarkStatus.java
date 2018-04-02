package com.reps.khxt.enums;

/**
 * @ClassName: MarkStatus
 * @Description: 打分情况
 * @author qianguobing
 * @date 2018年3月29日 下午12:13:46
 */
public enum MarkStatus {
	
	NOT_FINISHED_MARKING((short)0, "未完成打分"), FINISHED_MARKING((short) 1, "已完成打分");
	
	private Short id;
	
	private String status;
	
	MarkStatus(Short id, String status) {
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

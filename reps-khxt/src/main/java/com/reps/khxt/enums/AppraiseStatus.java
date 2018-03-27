package com.reps.khxt.enums;

public enum AppraiseStatus {
	
	UN_REPORTED((short)0, "未上报"), REPORTED((short) 1, "已上报"), APPRAISED((short)2, "已打分");
	
	private Short id;
	
	private String status;
	
	AppraiseStatus(Short id, String status) {
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

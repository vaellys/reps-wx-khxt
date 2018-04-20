package com.reps.khxt.vo;

public class ItemWeight {
	
	private Double point;
	
	private String id;
	
	public ItemWeight(Double point, String id) {
		super();
		this.point = point;
		this.id = id;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

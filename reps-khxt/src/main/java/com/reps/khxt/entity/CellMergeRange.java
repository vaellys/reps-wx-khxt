package com.reps.khxt.entity;

public class CellMergeRange {
	
	private int firstRow;
	
	private int lastRow;
	
	private int firstCol;
	
	private int lastCol;
	
	public CellMergeRange(int firstRow, int lastRow) {
		super();
		this.firstRow = firstRow;
		this.lastRow = lastRow;
	}

	public CellMergeRange(int firstRow, int lastRow, int firstCol, int lastCol) {
		super();
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.firstCol = firstCol;
		this.lastCol = lastCol;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public int getFirstCol() {
		return firstCol;
	}

	public void setFirstCol(int firstCol) {
		this.firstCol = firstCol;
	}

	public int getLastCol() {
		return lastCol;
	}

	public void setLastCol(int lastCol) {
		this.lastCol = lastCol;
	}
	
}

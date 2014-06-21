package com.qconnector.schematicparser;

public class Wire {
	//private int firstPartIndex;
	//private int secondPartIndex;
	private int sideStart;
	private int sideEnd;
	private int rowStart;
	private int rowEnd;
	public Wire(int sideStart, int sideEnd, int rowStart, int rowEnd)
	{
		this.sideStart = sideStart;
		this.sideEnd = sideEnd;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
	}
	
	public int sideS()
	{
		return sideStart;
	}

	public int sideE()
	{
		return sideEnd;
	}
	
	public int rowS()
	{
		return rowStart;
	}
	
	public int rowE()
	{
		return rowEnd;
	}
/*
	public int getSecondPartIndex() {
		return secondPartIndex;
	}

	public void setSecondPartIndex(int secondPartIndex) {
		this.secondPartIndex = secondPartIndex;
	}

	public int getFirstPartIndex() {
		return firstPartIndex;
	}

	public void setFirstPartIndex(int firstPartIndex) {
		this.firstPartIndex = firstPartIndex;
	}
	*/
	
}

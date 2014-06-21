package com.qconnector.schematicparser;

public class Wire {
	private int sideChange;
	private int rowStart;
	private int rowEnd;
	public Wire(int sideChange, int rowStart, int rowEnd)
	{
		this.sideChange = sideChange;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
	}
	
	public int side()
	{
		return sideChange;
	}

	public int rowS()
	{
		return rowStart;
	}
	
	public int rowE()
	{
		return rowEnd;
	}
}

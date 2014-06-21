package com.qconnector.schematicparser;

import java.util.ArrayList;

public class Net {
	private ArrayList<Integer> partsAttached = new ArrayList<Integer>();
	private ArrayList<Integer> correspondingPin = new ArrayList<Integer>();
	
	private ArrayList<Integer> row = new ArrayList<Integer>();
	private ArrayList<Integer> side = new ArrayList<Integer>();
	
	String netName;
	public Net(String name)
	{
		netName = name;
	}
	
	public ArrayList<Integer> getParts()
	{
		return partsAttached;
	}
	
	public ArrayList<Integer> getPins()
	{
		return correspondingPin;
	}
	
	public ArrayList<Integer> getRows()
	{
		return row;
	}
	
	public ArrayList<Integer> getSides()
	{
		return side;
	}
	
	public String getName()
	{
		return netName;
	}
	
	
}

package com.qconnector.schematicparser;

public class Part {
	private String name;
	private String deviceSet;
	private String device;
	private String packageType;
	
	public Part() {
		name = "";
		setDeviceSet("");
		setDevice("");
		packageType = "";
	}
	
	public Part(String name, String deviceSet, String device)
	{
		this.name = name;
		this.setDeviceSet(deviceSet);
		this.setDevice(device);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getPackageType()
	{
		return this.packageType;
	}
	
	public void setPackageType(String type)
	{
		this.packageType = type;
	}

	public String getDeviceSet() {
		return this.deviceSet;
	}

	public void setDeviceSet(String deviceSet) {
		this.deviceSet = deviceSet;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

}

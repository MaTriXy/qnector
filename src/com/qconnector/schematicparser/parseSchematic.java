package com.qconnector.schematicparser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class parseSchematic {
	public static void main(String argv[])
	{
		try {
			File fXmlFile = new File("C:/Users/Jake/Documents/2013-2014/ECE362/Eagle/simple/2Rinplane.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			ArrayList<Part> parts = getPartsList(doc);
			ArrayList<Part> tempParts = (ArrayList<Part>) parts.clone();
			
			NodeList nl = doc.getElementsByTagName("device");
			for (int i = 0; i < nl.getLength(); i++)
			{
				Node nDevice = nl.item(i);
				if (nDevice.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eDevice = (Element) nDevice;
					Iterator<Part> it = tempParts.iterator();
					while(it.hasNext())
					{
						Part temp = it.next();
						if (eDevice.getAttribute("name").equals(temp.getDevice()))
						{
							temp.setPackageType(eDevice.getAttribute("package"));
							it.remove();
						}
					}
				}
			}
			for (int i = 0; i < parts.size(); i++)
				System.out.println(parts.get(i).getName() + " is from " + parts.get(i).getDeviceSet() + " and is " + parts.get(i).getDevice() + " which is device type " + parts.get(i).getPackageType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Part> getPartsList(Document doc)
	{
		NodeList nl = doc.getElementsByTagName("parts");
		Node node = nl.item(0);
		NodeList partsList = doc.getElementsByTagName("part");
		ArrayList<Part> parts = new ArrayList<Part>();
		
		for (int i = 0; i < partsList.getLength(); i++) 
		{
			String name = ((Element) partsList.item(i)).getAttribute("name");
			String deviceSet = ((Element) partsList.item(i)).getAttribute("deviceset");
			String device = ((Element) partsList.item(i)).getAttribute("device");
			Part newpart = new Part(name, deviceSet, device);
			parts.add(newpart);
		}		
		return parts;
	}
	
	
}

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

public class ParseSchematic {
    private static ArrayList<Part> parts = new ArrayList<Part>();

    public static void go(File fXmlFile) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			parts = getPartsList(doc);
			ArrayList<Part> tempParts = (ArrayList<Part>) parts.clone();
			getDeviceInfo(doc, tempParts);
						
			ArrayList<Net> netList = new ArrayList<Net>();
			populateNetAndPins(doc, parts, netList);
			
			/*
			int[] setRow = parts.get(0).getPinRow();
			int[] setSide = parts.get(0).getPinSide();
			setRow[0] = 1;
			setSide[0] = 0;
			setRow[1] = 0;
			setSide[1] = 0;
			setRow = parts.get(1).getPinRow();
			setSide = parts.get(1).getPinSide();
			setRow[0] = 1;
			setSide[0]= 1;
			setRow[1] = 2;
			setSide[1] = 1;
			setRow = parts.get(2).getPinRow();
			setSide = parts.get(2).getPinSide();
			setRow[0] = 2;
			setSide[0]= 0;
			setRow[1] = 3;
			setSide[1] = 0;
			*/
			
			int[] setRow = parts.get(0).getPinRow();
			int[] setSide = parts.get(0).getPinSide();
			setRow[0] = 0;
			setSide[0] = 1;
			setRow[1] = 1;
			setSide[1] = 1;
			setRow = parts.get(1).getPinRow();
			setSide = parts.get(1).getPinSide();
			setRow[0] = 1;
			setSide[0]= 0;
			setRow[1] = 0;
			setSide[1] = 0;
			setRow = parts.get(2).getPinRow();
			setSide = parts.get(2).getPinSide();
			setRow[0] = 2;
			setSide[0]= 1;
			setRow[1] = 3;
			setSide[1] = 1;
			
			ArrayList<Wire> wires = generateWires(netList, parts);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public static ArrayList<Part> getPartsList() {
        return parts;
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
	
	private static void populateNetAndPins(Document doc, ArrayList<Part> parts, ArrayList<Net> netList)
	{
		NodeList nl2 = doc.getElementsByTagName("net");
		for (int netIndex = 0; netIndex < nl2.getLength(); netIndex++)
		{
			String name = ((Element) nl2.item(netIndex)).getAttribute("name");
			Net newNet = new Net(name);
			netList.add(newNet);
			NodeList pinrefs = ((Element) nl2.item(netIndex)).getElementsByTagName("pinref");
			for (int j = 0; j < pinrefs.getLength(); j++)
			{
				String targetName = ((Element) pinrefs.item(j)).getAttribute("part");
				for (int partIndex = 0; partIndex < parts.size(); partIndex++)
				{
					if (targetName.equals(parts.get(partIndex).getName()))
					{
						netList.get(netIndex).getParts().add(partIndex);
						int pinNumber;
						try
						{
							pinNumber = Integer.parseInt(((Element) pinrefs.item(j)).getAttribute("pin"));
						} catch (NumberFormatException e)
						{
							if (((Element) pinrefs.item(j)).getAttribute("pin").equals("+"))
								pinNumber = 1;
							else
								pinNumber = 2;
						}
						netList.get(netIndex).getPins().add(pinNumber);
						parts.get(partIndex).getPinNets()[pinNumber-1] =  netIndex;
						break;
					}
				}
				
			}
		}
	}
	
	private static void getDeviceInfo(Document doc, ArrayList<Part> clone)
	{
		NodeList nl = doc.getElementsByTagName("device");
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node nDevice = nl.item(i);
			if (nDevice.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eDevice = (Element) nDevice;
				Iterator<Part> it = clone.iterator();
				while(it.hasNext())
				{
					Part temp = it.next();
					if (eDevice.getAttribute("name").equals(temp.getDevice()))
					{
						NodeList connects = eDevice.getElementsByTagName("connects");
						int numOfPins = ((Element) connects.item(0)).getElementsByTagName("connect").getLength();
						temp.setPackageType(eDevice.getAttribute("package"));
						if (temp.getDeviceSet().equals("BATTERY"))
							numOfPins--;
						temp.setNumPins(numOfPins);
						it.remove();
					}
				}
			}
		}
	}
	
	private static ArrayList<Wire> generateWires(ArrayList<Net> netList, ArrayList<Part> partsList)
	{
		ArrayList<Wire> wires = new ArrayList<Wire>();
		for (int netIndex = 0; netIndex < netList.size(); netIndex++) //for every net
		{
			for (int i = 0; i < netList.get(netIndex).getParts().size()-1; i++) //loop through every pair of pins 
			{
				int firstPartIndex = netList.get(netIndex).getParts().get(i);
				int secondPartIndex = netList.get(netIndex).getParts().get(i+1);
				Part firstPart = partsList.get(firstPartIndex);
				Part secondPart = partsList.get(secondPartIndex);
				int firstSide = firstPart.getPinSide()[netList.get(netIndex).getPins().get(i)-1];
				int firstRow = firstPart.getPinRow()[netList.get(netIndex).getPins().get(i)-1];
				int secondSide = secondPart.getPinSide()[netList.get(netIndex).getPins().get(i+1)-1];
				int secondRow = secondPart.getPinRow()[netList.get(netIndex).getPins().get(i+1)-1];
				
				Wire wire = new Wire(firstSide, secondSide, firstRow, secondRow);
				wires.add(wire);	
			}
		}
		return wires;
	}

}

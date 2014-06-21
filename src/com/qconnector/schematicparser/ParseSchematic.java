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
			getDeviceInfo(doc, tempParts);
			
			for (int i = 0; i < parts.size(); i++)
				System.out.println(parts.get(i).getName() + " is from " + parts.get(i).getDeviceSet() + " and is " + parts.get(i).getDevice() + " which is device type " + parts.get(i).getPackageType());
			
			ArrayList<Net> netList = new ArrayList<Net>();
			populateNetAndPins(doc, parts, netList);
			
			/*
			for (int i = 0; i < netList.size(); i++)
			{
				System.out.println("Net name is " + netList.get(i).getName());
				for (int j = 0; j < netList.get(i).getParts().size(); j++)
				{
					System.out.print(" " + netList.get(i).getParts().get(j));
				}
				
				System.out.println();
				
				for (int j = 0; j < netList.get(i).getParts().size(); j++)
				{
					System.out.print(" " + netList.get(i).getPins().get(j));
				}
				
				System.out.println();
			}
			*/
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
			
			
			ArrayList<Wire> wires = generateWires(netList, parts);
			
			for(int i = 0; i < wires.size(); i++)
			{
				System.out.println(wires.get(i).side() + "=" + wires.get(i).rowS() + "=" + wires.get(i).rowE());
			}
		
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
	
	/*
	private static ArrayList<Wire> generateWires(ArrayList<Net> netList, ArrayList<Part> partsList)
	{
		ArrayList<Wire> wires = new ArrayList<Wire>();

		
		for (int i = 0; i < netList.size(); i++)
		{
			Net tempNet = netList.get(i);
			Part tempPart = partsList.get(tempNet.getParts().get(0)); //get the first part listed in the first net
			int[] rowArr = tempPart.getPinRow();
			int[] sideArr = tempPart.getPinSide();
			tempNet.getRows().add(rowArr[tempNet.getPins().get(0)-1]); //add the row info for the first pin in the net list
			tempNet.getSides().add(sideArr[tempNet.getPins().get(0)-1]);		
			for (int j = 1; j < tempNet.getParts().size(); j++)
			{
				tempPart = partsList.get(tempNet.getParts().get(j)); 
				rowArr = tempPart.getPinRow();
				int row = rowArr[tempNet.getPins().get(j)-1];
				sideArr = tempPart.getPinSide();
				int side = rowArr[tempNet.getPins().get(j)-1];
				
				int ret = rowInNetWrongSide(tempNet, row, side);
				if (ret > -1)
				{
					int sideChange = 1;
					int row1 = tempNet.getRows().get(ret);
					int row2 = row1;
					Wire tempWire = new Wire(sideChange, row1, row2);
					wires.add(tempWire);
				}
				
				ret = sideInNetWrongRow(tempNet, row, side);
				if (ret > -1)
				{
					int row1 = tempNet.getPins().get(ret);
					int row2 = row;
					Wire tempWire = new Wire(0, row1, row2);
					wires.add(tempWire);
				}
				
				if (!alreadyInNet(tempNet, row, side))
				{
					int sideChange = 1;
					int row1 = row;
					int row2 = tempNet.getRows().get(tempNet.getRows().size()-1);
					Wire tempWire = new Wire(1, row1, row2);
					wires.add(tempWire);
				}
				
				tempNet.getRows().add(row);
				tempNet.getSides().add(side);
				
			}
		}
		return wires;
	}
	*/
	private static ArrayList<Wire> generateWires(ArrayList<Net> netList, ArrayList<Part> partsList)
	{
		ArrayList<Wire> wires = new ArrayList<Wire>();
		for (int netIndex = 0; netIndex < netList.size(); netIndex++) //for every net
		{
			for (int i = 0; i < netList.get(netIndex).getParts().size()-1; i++) //loop through every pair of pins 
			{
				
			}
		}
		return wires;
	}
	
	
	private static int rowInNetWrongSide(Net net, int row, int side)
	{
		ArrayList<Integer> rows = net.getRows();
		ArrayList<Integer> sides = net.getSides();
		for (int i = 0; i < rows.size(); i++)
		{
			if ((rows.get(i) == row) && (sides.get(i) != side))
				return i;
		}
		return -1;
	}
	
	private static int sideInNetWrongRow(Net net, int row, int side)
	{
		ArrayList<Integer> sides = net.getSides();
		ArrayList<Integer> rows = net.getRows();
		for (int i = 0; i < sides.size(); i++)
		{
			if ((sides.get(i) == side) && (rows.get(i) != row))
				return i;
		}
		return -1;		
	}
	
	
	private static Boolean alreadyInNet(Net net, int row, int side)
	{
		ArrayList<Integer> rows = net.getRows();
		ArrayList<Integer> sides = net.getSides();
		
		for (int i = 0; i < rows.size(); i++)
		{
			if ((rows.get(i) == row) && (sides.get(i) == side))
				return true;
		}
		return false;
	}
	
}

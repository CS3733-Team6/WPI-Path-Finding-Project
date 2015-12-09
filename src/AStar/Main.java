package AStar;

import AStar.Settings;
import AStar.TestMain;
import AStar.Node;
import AStar.Edge;
import AStar.AStar;
import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.Arrays;
import AStar.NodeList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class Main {

	private static Settings defaultSettings = new Settings(false, false, false);
	private static NodeList nlist = new NodeList();
	public static String excmessage;
	
//	public static void main(String[] args){
//		Result result = JUnitCore.runClasses(TestJunit.class);
//	      for (Failure failure : result.getFailures()) {
//	         System.out.println(failure.toString());
//	      }
//	      System.out.println(result.wasSuccessful());
//	}
	static List<Node> getNodesFromFile(String filePath)
	{
		List<Node> nodeList = new ArrayList<Node>();
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int nodeNameIndex = 0;
		int nodeXIndex = 1;
		int nodeYIndex = 2;
		int nodeDescIndex = 3;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] nodeData = line.split(delimiter);
				String name = nodeData[nodeNameIndex];
				int x = Integer.parseInt(nodeData[nodeXIndex]);
				int y = Integer.parseInt(nodeData[nodeYIndex]);
				String description = nodeData[nodeDescIndex];
				Node newNode = new Node(name,0,0,0,false, x, y, description);
				nodeList.add(newNode);
			}

		} 
		catch (FileNotFoundException e) {e.printStackTrace();
		excmessage="FileNotFound";} 
		catch (IOException e) {e.printStackTrace();
		excmessage="IOException";} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		return nodeList;
	}
	private static void connectEdgesFromFile(List<Node> nodeList, String filePath)
	{
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int edgeX1Index = 0;
		int edgeY1Index = 1;
		int edgeX2Index = 2;
		int edgeY2Index = 3;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] edgeData = line.split(delimiter);
				int x1 = Integer.parseInt(edgeData[edgeX1Index]);
				int y1 = Integer.parseInt(edgeData[edgeY1Index]);
				int x2 = Integer.parseInt(edgeData[edgeX2Index]);
				int y2 = Integer.parseInt(edgeData[edgeY2Index]);
				Node n1 = findNodeByXY(nodeList, x1, y1);
				Node n2 = findNodeByXY(nodeList, x2, y2);
				if (n1.neighbors == null)
				{
					n1.neighbors =  new ArrayList<>(Arrays.asList(new Edge(n2, AStar.getDistance(n1, n2))));
				}
				else
				{
					n1.neighbors.add(new Edge(n2, AStar.getDistance(n1, n2)));
				}
				if (n2.neighbors == null)
				{
					n2.neighbors =  new ArrayList<>(Arrays.asList(new Edge(n1, AStar.getDistance(n1, n2))));
				}
				else
				{
					n2.neighbors.add(new Edge(n1, AStar.getDistance(n1, n2)));
				}
			}

		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	public static List<Integer> getScaleFromFile(String filePath)
	{
		List<Integer> scaleList = new ArrayList<Integer>();
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] scaleData = line.split(delimiter);
				System.out.println("ScaleSize");
				System.out.println(scaleData);
				String xScale_char = scaleData[0];
				String yScale_char = scaleData[1];
				int xScale = Integer.parseInt(xScale_char);
				int yScale = Integer.parseInt(yScale_char);
				scaleList.add(xScale);
				scaleList.add(yScale);
			}
			System.out.println("================");
			System.out.println("Scale");
			System.out.println(scaleList);
			System.out.println("================");

		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		return scaleList;
	}
	public static Node findNodeByXY(List<Node> nodeList, int x, int y)//Want to change this to throwing an exception when the node is not found
	{
		for(Node n : nodeList){
			if(n.xPos == x && n.yPos == y)
			{
				return n;
			}
		}
		return null;
	}
	public static List<Node> readMap(String nodeFilePath, String edgeFilePath)
	{
		List<Node> nodeList = getNodesFromFile(nodeFilePath);
		connectEdgesFromFile(nodeList, edgeFilePath);
		return nodeList;
	}
	//Method to find the path given a start node and an end node.
	public static List<Node> getPathFromNode(Node startNode, Node endNode, List<Node> map)
	{
		AStar astar = new AStar(map, defaultSettings);
		System.out.println(map);
		return astar.findPath(startNode, endNode);
	}
	//Method to find path when given a string 
	public static List<Node> getPathFromString(String startName, String destName, List<Node> map)
	{
		Node startNode = nlist.findNode(startName);
		Node destNode = nlist.findNode(destName);
		return getPathFromNode(startNode, destNode, map);
		//drawPath(path);
	}
	
	//Method to draw a path on a map, given said path.
	public static void drawPath(List<Node> path)
	{
		
	}
	
	//Method to provide a list of directions from a list of nodes.
	public static List<String> getDirectionsList(List<Node> path)
	{
		List<String> directions = new ArrayList<String>();
		if(path.size() == 0 || path.size() == 1)
		{
			directions.add("Can't Generate Directions as no path was found");
			return directions;
		}
		else if(path.size() == 2)
		{
			directions.add("Proceed straight on path");
			return directions;
		}
		int prevAngle = 0;
		String prevDirection = "";
		for(int i = 0; i < path.size(); i++)
		{
			if (i == path.size() - 1)
			{
				directions.add("Continue straight until you've have reached your destination");
				break;
			}
			Node n1 = path.get(i);
			Node n2 = path.get(i+1);
			int newAngle = getAngle(path.get(i), path.get(i+1));
			int delta_angle = 0;
			if (i != 0)
			{
				delta_angle = newAngle - prevAngle;
				double delta_angle_rad = (Math.PI / 180) * (double) delta_angle;
				delta_angle = (int) ((180 / Math.PI) * Math.atan2(Math.sin(delta_angle_rad), Math.cos(delta_angle_rad)));//Bind angle to range [-180,180]
			}
			
			// System.out.println(delta_angle);
			String direction = getDirectionFromAngle(delta_angle);
			double distance_x = ((double)(n2.xPos - n1.xPos) * .18) * 12;
			double distance_y = ((double)(n2.yPos - n1.yPos) * .13) * 12;
			double distance = Math.sqrt((distance_x * distance_x) + (distance_y * distance_y));
			String dist = String.format("%.2f", distance);
					
			direction = direction + " walk " + dist + " ft";
//			if (direction.equals("Go Straight") && direction.equals(prevDirection))
//			{
//				//Don't repeat straight directions
//			}
//			else
//			{
				directions.add(direction);
		//}
			prevAngle = newAngle;
			prevDirection = direction;
		}
		return directions;
	}
	public static String getDirectionFromAngle(int angle)
	{
		if (-30 < angle && angle < 30)//Going Straight
		{
			return "Go Straight";
		}
		else if (30 <= angle && angle < 60)
		{
			return "Slight right turn";
		}
		else if (60 <= angle && angle < 120)
		{
			return "Right turn";
		}
		else if (120 < angle && angle <= 180)
		{
			return "Sharp right turn";
		}
		else if (-60 < angle && angle <= -30)
		{
			return "Slight left turn";
		}
		else if (-120 < angle && angle <= -60)
		{
			return "Left turn";
		}
		else if (-180 < angle && angle <= -120)
		{
			return "Sharp left turn";
		}
		else
		{
			return "Go Straight";
		}
	}
	//Returns the angle between two nodes in degrees
	public static int getAngle(Node n1, Node n2)
	{
		int dx = n2.xPos - n1.xPos;
		int dy = n2.yPos - n1.yPos;
		return (int) ((180 / Math.PI) * Math.atan2(dy,dx));
	}
	
	
}
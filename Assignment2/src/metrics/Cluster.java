package metrics;

import java.util.ArrayList;

public class Cluster {

	public ArrayList<Point> points = new ArrayList<Point>();
	public Point center = null;
	
	public Cluster(Point center) {
		this.center = center;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	public String printCluster() {
		String cluster = "";
		for (Point point : points) {
			cluster = cluster + point.getKey() + " ";
		}
		return cluster;
	}
}

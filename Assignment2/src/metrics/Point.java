package metrics;

import java.util.Random;

public class Point {

	private static int MIN = 0;
	private static int MAX = 10000;
	Random r = new Random();

	EucldianDistance ed = new EucldianDistance();
	
	double x;
	double y;
	String key;
	
	public Point () {
		
	}
	
	public Point(double x, double y, String key) {
		this.x = x;
		this.y = y;
		this.key = key;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getX() {
		return this.x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public double getXDistance(double x1, double x2) {
		return ed.calculateDistance(x1, x2);
	}
	
	public double getYDistance(double y1, double y2) {
		return ed.calculateDistance(y1, y2);
	}
	
	public Point createPoint(String key) {
		double x = MIN + (MAX - MIN) * r.nextDouble();
		double y = MIN + (MAX - MIN) * r.nextDouble();
		System.out.println("Created a new point for " + key + " at (" + x + ", " + y + ")");
		return new Point(x, y, key);
	}
}

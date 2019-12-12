package algorithm;

import java.io.Serializable;

public class Edge implements Serializable {

	private double weight;
	private Vertex src;
	private Vertex dest;
	
	public Edge(double weight, Vertex src, Vertex dest) {
		this.weight = weight;
		this.src = src;
		this.dest = dest;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public void setSrc(Vertex src) {
		this.src = src;
	}
	
	public Vertex getSrc() {
		return this.src;
	}
	
	public void setDest(Vertex dest) {
		this.dest = dest;
	}
	
	public Vertex getDest() {
		return this.dest;
	}
}

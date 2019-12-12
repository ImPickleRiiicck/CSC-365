package algorithm;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	private List<Vertex> vertexList;
	private List<Edge> edgeList;
	
	public Graph(List<Vertex> vertexList, List<Edge> edgeList) {
		this.vertexList = vertexList;
		this.edgeList = edgeList;
	}
	
	public List<Vertex> getVertexList() {
		return this.vertexList;
	}
	
	public void setVertexList(List<Vertex> vertexList) {
		this.vertexList = vertexList;
	}
	
	public List<Edge> getEdgeList() {
		return this.edgeList;
	}
	
	public void setEdgeList(List<Edge> edgeList) {
		this.edgeList = edgeList;
	}
	
	public boolean edgeSearch(Edge edge) {
		for (Edge e: edgeList) {
			if(edge.getDest() != null && e.getSrc().getName().equals(edge.getDest().getName())) return true;
		}
		return false;
	}
	
	public List<String> disjoint() {
		//System.out.println("Doing disjoint stuff");
		List<String> path = new ArrayList<String>();
		for (Edge e : edgeList) {
			//System.out.println("Looking at edge from " + e.getSrc().getName());
			if (edgeSearch(e)) {
				if (!path.contains(e.getSrc().getName())) {
					path.add(e.getSrc().getName());
				}
			}
		}
		return path;
	}
}

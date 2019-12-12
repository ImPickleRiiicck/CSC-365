package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstras {

	public Dijkstras() {
	}
	
	public void calculateShortestPath(Vertex source) {
		
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		
		source.setDistance(0);
		source.setBeenVisited(true);
		queue.add(source);
		
		while (!queue.isEmpty()) {
			
			// Get the top vertex from the queue
			Vertex topVertex = queue.poll();
			List<Edge> vertexEdgeList = topVertex.getListOfEdges();
			
			for (Edge edge : vertexEdgeList) {
				Vertex edgeVertex = edge.getDest();
				if (edge.getDest() != null && !edgeVertex.getBeenVisited()) {
					double distance = topVertex.getDistance() + edge.getWeight();
					
					if (distance < edgeVertex.getDistance()) {
						queue.remove(edgeVertex);
						edgeVertex.setDistance(distance);
						edgeVertex.setSource(topVertex);
						queue.add(edgeVertex);
					}
				}
			}
			topVertex.setBeenVisited(true);
		}
	}
	
	public List<Vertex> getShortestPath (Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		
		for (Vertex vertex = target; vertex != null; vertex = vertex.getSource()) {
			path.add(vertex);
			System.out.println(vertex.getName());
		}
		Collections.reverse(path);
		return path;
	}
}

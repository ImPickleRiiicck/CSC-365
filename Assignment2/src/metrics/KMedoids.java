package metrics;

public class KMedoids {
	
	public KMedoids() {
		
	}
	
	public double calculate(Cluster cluster) {
		double sum = 0;
		double x = cluster.center.getX();
		double y = cluster.center.getY();
		
		for (Point p : cluster.getPoints()) {
			sum += p.getXDistance(x, p.getX());
			sum += p.getYDistance(y, p.getY());
		}
		return sum;
	}
}

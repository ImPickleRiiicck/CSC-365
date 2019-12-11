package metrics;

public class EucldianDistance {

	public double calculateDistance(Double num1, Double num2) {
		double sum = 0;
		
		if (num1 != null && num2 != null) {
			sum += Math.pow(num1, num2);
		}
		
		return Math.sqrt(sum);
	}
}

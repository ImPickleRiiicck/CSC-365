package tree;

import java.util.Arrays;

public class Node {
	
	// Node variables
	int numberOfKeys;
	int minimumDegree;
	int median;
	int childrenCount;
	boolean isLeaf;
	double distance;
	Node[] children;
	String[] keys;
	int tfidf;
	
	// Constructor for the Node
	public Node(boolean isLeaf, int min) {
		
		numberOfKeys = 0;
		minimumDegree = min;
		median = min/2;
		this.isLeaf = isLeaf;
		keys = new String[(2*min) - 1];
		childrenCount = 0;
		children = new Node[2*min];
		distance = 0.0;
	}
	
	// Returns true if the node is full, and false if the node is not full
	public boolean checkIfFull() {
		// There can only be 2*m-1 keys stored in a node as per the rule of btrees
		return numberOfKeys == (2*minimumDegree) - 1;
	}
	
	// Method used for testing the checkIfFull method
	public void addKeyTest() {
		numberOfKeys = minimumDegree;
	}
	
	// Returns the children of the node in location i
	public Node getChildNode(int i) {
		return children[i];
	}
	
	// Sets a new child node!
	public void setAsChildNode(Node n) {
		System.out.println("Setting child node ...");
		children[childrenCount] = n;
		childrenCount ++;
	}
	
	public void removeChildNode(int i) {
		children[i] = null;
		childrenCount--;
	}
	
	public Node[] getAllChildren() {
		return children;
	}
	
	
	// Returns a key at index i
	public String getKey(int i) {
		return keys[i];
	}
	
	public void addKey(String key) {
		keys[numberOfKeys] = key;
		numberOfKeys++;
	}
	
	public void removeKey(int toRemove) {
		keys[toRemove] = null;
		numberOfKeys--;
	}
	
	public String[] getAllKeys() {
		return keys;
	}
	
	public void sortKeys() {
		Arrays.sort(keys, 0, numberOfKeys);
	}
}

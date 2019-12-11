package tree;

public class BTree {
	
	// Class variables
	private Node root;
	int minimumDegree;
	
	/**
	 * BTree constructor which takes in a minumum degree and creates an initial root node.
	 * @param min
	 */
	public BTree(int min) {
		minimumDegree = min;
		root = new Node(true, min);
	}
	
	/**
	 * Simple test method to check if the root is full. Did not want 
	 * to write a junit framework to test this.
	 * @return
	 */
	public boolean testFull() {
		return root.checkIfFull();
	}
	
	/**
	 * Simple test method to add keys. Did not want to write a junit 
	 * framework to test this.
	 */
	public void addKeysTest() {
		root.addKeyTest();
	}
	
	/**
	 * Simple initial insert function. This works by first checking if the root is null,
	 * if it is, it creates a new node to insert at and splits.
	 * 
	 * @param key
	 */
	public void insertKey (String key) {
		// If the root is not full then we can add at the root, if it is full then we
		// must make a new node
		if (root.checkIfFull()) {
			System.out.println("The node is full");
			System.out.println("Creating a new node ... ");
			Node newNode = new Node(false, minimumDegree);
			System.out.println("Setting root as child to new node ... ");
			newNode.setAsChildNode(root);
			System.out.println("Setting root to the new node ... ");
			root = newNode;
			System.out.println("Splitting ... ");
			splitNode(root, 0);
		}
		System.out.println("Insert Key passed. Inserting into node");
		insertKeyIntoNode(root, key);
	}

	/**
	 * 
	 * @param n
	 * @param key
	 */
	public void insertKeyIntoNode(Node n, String key) {
		
		System.out.println("Inserting a key");
		
		int numberOfKeys = n.numberOfKeys;
		
		System.out.println("This is the current number of keys before inserting: " + numberOfKeys);
		
		// If the current node is a leaf, then we can insert here
		if (n.isLeaf) {
			System.out.println("It is a leaf");
			System.out.println("Adding key");
			n.addKey(key);
			System.out.println("Key is added. Sorting...");
			n.sortKeys();
			System.out.println("Keys sorted.");
			System.out.println("Printing sorted keys ...");
			String[] printList = n.getAllKeys();
			int index = 0;
			for (String str : printList) {
				System.out.println("Key " + index + " is " + str);
				index++;
			}
			return;
		} else {
			System.out.println("The node is not a leaf. Now checking where the node belongs ... ");
			// Find the child-node where the inserted-key belongs
			while (numberOfKeys > 0 && key.compareTo(n.getKey(numberOfKeys - 1)) < 0) {
				numberOfKeys --;
			}
			System.out.println("The node has " + n.childrenCount + " children");
			// If the child node is full, then split the child node
			System.out.println("The number of keys: " + numberOfKeys);
			if (n.getAllChildren()[numberOfKeys].checkIfFull()) {
				System.out.println("Splitting child node");
				splitNode(n, numberOfKeys);
				System.out.println("Child has been split");
			}
			n = n.getAllChildren()[numberOfKeys];
		}
		insertKeyIntoNode(n, key);
	}
	
	/**
	 * Split method. The demon spawn of the BTree.
	 * 
	 * @param parent
	 * @param childIndex
	 */
	public void splitNode(Node parent, int childIndex) {
		// Create new node with the leaf being the same as the split node
		Node newLeftNode = parent.getAllChildren()[childIndex];
		Node newRightNode = new Node(newLeftNode.isLeaf, minimumDegree);
		int median = parent.median;
		System.out.println("This is the median: " + parent.median);
		parent.addKey(newLeftNode.getAllKeys()[median]);
		
		for (int i = 1; i <= median; i++) {
			newRightNode.addKey(newLeftNode.getAllKeys()[i + median]);
		}
		
		for (int i = newLeftNode.numberOfKeys - 1; i >= median; i--) {
			newLeftNode.removeKey(i);
		}
		
		if (!newLeftNode.isLeaf) {
			for (int i = 1; i <= median + 1; i++) {
				newRightNode.setAsChildNode(newLeftNode.getAllChildren()[i + median]);
			}
			
			for (int i = newLeftNode.childrenCount - 1; i >= median + 1 ; i--) {
				newLeftNode.removeChildNode(i);
			}
		}
		
		parent.setAsChildNode(newRightNode);
	}
	
	/**
	 * Search the b tree from a given node. This traverses the tree until it either finds
	 * the key it is looking for, or returns null
	 * 
	 * @param node
	 * @param key
	 * @return
	 */
	public String keySearch (Node node, String key, int childIndex) {
		
		int currentIndex = 0;
		
		while (currentIndex < node.numberOfKeys && key.compareTo(node.getKey(currentIndex)) > 0) {
			System.out.println("I am comparing " + key + " to " + node.getKey(currentIndex));
			currentIndex ++;
		}

		if(currentIndex < node.numberOfKeys && key.compareTo(node.getKey(currentIndex)) == 0) {
			System.out.println("Current index of " + key + " is " + currentIndex);
			return node.getKey(currentIndex);
		}
		
		if (node.isLeaf) return null;
		
		if (childIndex < node.childrenCount) {
			childIndex++;
			keySearch(node.children[currentIndex], key, childIndex);
		}
		
		return keySearch(node.children[currentIndex], key, childIndex);
	}
	
	/** 
	 * Return the root of the tree. Had to add to search for a key 
	 * and keep the root private.
	 * 
	 * @return
	 */
	public Node getRoot() {
		return this.root;
	}
}

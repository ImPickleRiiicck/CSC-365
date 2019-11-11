package hasher;

public class HashTable {

	static double RESIZE_THRESHOLD = .75;
	
	static class Node {
		String key;
		int value;
		Node next;
		
		Node (String k, int v, Node n) {
			key = k;
			value = v;
			next = n;
		}
	}
	
	Node[] table = new Node[16];
	int count = 0;
	
	int get (String k) {
		int h = k.hashCode();
		int i = h & (table.length - 1);
		for (Node p = table[i]; p != null; p = p.next) {
			if (k.equals(p.key)) return p.value;
		}
		return 0;
	}
	
	void addOne (String k) {
		int h = k.hashCode();
		int i = h & (table.length -1);
		for (Node p = table[i]; p != null; p = p.next) {
			if (k.contentEquals(p.key)) {
				++p.value;
				return;
			}
		}
		
		table[i] = new Node(k, 1, table[i]);
		
		if (++count > RESIZE_THRESHOLD * table.length) {
			resize();
		}
	}
	
	void resize() {
        int newSize = 2 * table.length;
        int oldSize = table.length;
        Node[] oldHash = new Node[oldSize];
        oldHash = table;
        table = new Node[newSize];

        for (int i = 0; i < oldSize; i++) {
            while (oldHash[i] != null) {
                rehash(oldHash[i].key, oldHash[i].value);
                oldHash[i] = oldHash[i].next;
            }
        }
    }

    void rehash(String key, int value) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        table[i] = new Node(key, value, table[i]);
    }

    void printAll() {
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                System.out.println(e.key);
            }
        }
    }
}

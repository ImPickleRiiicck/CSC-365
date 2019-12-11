package Main;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cache.Cache;
import WebUtilities.WebScraper;
import tree.BTree;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		/*
		// Initial testing
		BTree testTree = new BTree(2);
		//System.out.println(testTree.testFull());
		testTree.insertKey("hello");
		//testTree.addKeysTest();
		//System.out.println(testTree.testFull());
		testTree.insertKey("againstill");
		testTree.insertKey("bagain");
		//System.out.println(testTree.testFull());
		testTree.insertKey("againstills");
		testTree.insertKey("garble");
		testTree.insertKey("damn");
		testTree.insertKey("whatup");
		testTree.insertKey("hi");
		testTree.insertKey("noway");
		testTree.insertKey("hihihihi");
		testTree.insertKey("omg");
		testTree.insertKey("becky");
		testTree.insertKey("lookup");
		testTree.insertKey("dangson");
		System.out.println(testTree.testFull());
		System.out.println(testTree.keySearch(testTree.getRoot(), "hello"));
		System.out.println(testTree.keySearch(testTree.getRoot(), "bagain"));
		System.out.println(testTree.keySearch(testTree.getRoot(), "againstill"));

		System.out.println(testTree.keySearch(testTree.getRoot(), "againstills"));
		System.out.println(testTree.keySearch(testTree.getRoot(), "zzz"));
		
		String bagain = "bagain";
		String garble = "garble";
		*/
		
		Cache cache = new Cache();
		cache.addToCache("testFile.txt", 0);
		cache.addToCache("dontdeleteme.txt", 0);
		WebScraper ws = new WebScraper();
		String testUrl[] = ws.getLinks("https://en.wikipedia.org/wiki/K-medoids");
		for (String url : testUrl) {
			System.out.println(url);
		}
	}

}

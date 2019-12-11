package cache;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Cache {

	private static final int MAX_CACHE_SIZE = 50;
	private static final String CACHE_PATH = "C:/Users/rober/csc365-new-workspace/Assignment1/FileCache/";
	int num = 0;
	LinkedHashMap<String, Long> cache;
	
	public Cache() throws IOException {
		cache = new LinkedHashMap<String,Long>(MAX_CACHE_SIZE);
	}
	
	public double getTfidfFromCache(String url) throws IOException {
		for (String key : cache.keySet()) {
			if (key.equals(url)) {
				File cacheFile = new File(CACHE_PATH + url.replaceAll(" ", "").replaceAll(":", "").replaceAll("/", "") + ".txt");
				String contents = new String(Files.readAllBytes(Paths.get(cacheFile.toString())));
				return Double.parseDouble(contents);
			}
		}
		return -1;
	}
	
	public void addToCache(String url, double tfidf) throws IOException {
		File newCacheFile = new File(CACHE_PATH + url.replaceAll(" ", "").replaceAll(":", "").replaceAll("/", "") + ".txt");
		System.out.println(newCacheFile.getPath());
		newCacheFile.createNewFile();
		if (newCacheFile.isFile()) {
			System.out.println("Exists");
			FileWriter writer;
			writer = new FileWriter(newCacheFile);
			writer.write(String.valueOf(tfidf));
			writer.close();
		}
		Long lastModified = newCacheFile.lastModified();
		cache.put(url, lastModified);
		System.out.println("The current cache size is " + cache.size());
		removeOldestEntry();
	}
	
	public void removeOldestEntry() {
		System.out.println("Checking if I should delete ... ");
		if (cache.size() == MAX_CACHE_SIZE) {
			System.out.println("Cache is too big. Trying to delete ...");
			Long oldest = 0L;
			String toRemove = "";
			for (Map.Entry<String, Long> entry : cache.entrySet()) {
				if (oldest == 0) {
					oldest = entry.getValue();
					toRemove = entry.getKey();
				} else {
					if (oldest > entry.getValue()) {
						oldest = entry.getValue();
					}
				}
			}
			System.out.println("Deleting " + toRemove);
			cache.remove(toRemove);
			deleteFileFromCache(toRemove);
		}
	}
	
	public void deleteFileFromCache(String fileName) {
		File cacheFile = new File(CACHE_PATH + fileName);
		if (cacheFile.exists()) {
			cacheFile.delete();
		}
	}
	
}
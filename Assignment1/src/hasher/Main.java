package hasher;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import WebUtilities.WebScraper;
import cache.Cache;
import metrics.Cluster;
import metrics.KMedoids;
import metrics.Point;
import tree.BTree;

public class Main {

	private JFrame frame;
	private JTextField textField;
	// Keeps track of the amount of urls
	int urlCount = 105;
	int urlIndex = 0;
	double fc = 0;
	String foundUrl = "";

	// WebScraper to get links from pages
	WebScraper scraper = new WebScraper();
	
	// Cache to get info faster
	Cache cache = new Cache();

	// This hash table will keep track of the amount of documents that contain a
	// certain word.
	final HashTable frequencyHashie = new HashTable();
	
	BTree[] trees = new BTree[5];
	
	String[] urlsList = new String[105];
	
	Cluster[] clusters = new Cluster[5];

	private static final String URL_FILE = "C:/Users/rober/csc365-new-workspace/Assignment2/Urls.txt";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public Main() throws IOException {
		initialize();
	}

	/**
	 * Method that calculates Tf-Idf
	 * 
	 * @param wordFrequency
	 * @param totalDocuments
	 * @param totalWords
	 * @param documentFrequency
	 * @return
	 */
	public double tfidfCount(int wordFrequency, int totalDocuments, int totalWords, int documentFrequency) {
		double tf = (double) documentFrequency / (double) totalWords;
		double idf = Math.log((double) totalDocuments / (double) wordFrequency);
		double tfidf = tf * idf;
		return Math.abs(tfidf);
	}

	public void setHashies(String link) throws IOException {
		System.out.println(link);
		
		HashTable urlHashie = new HashTable();
		urlsList[urlIndex] = link;
		Document urlDoc = Jsoup.connect(link).get();
		String[] urlWordCount = urlDoc.body().text().split(" ");

		// Adds all elements to a hash table
		for (int j = 0; j < urlWordCount.length; j++) {
			urlHashie.addOne(urlWordCount[j].toLowerCase());
		}
		// Adds all keys to the frequency table
		for (int j = 0; j < urlHashie.table.length; j++) {
			for (HashTable.Node p = urlHashie.table[j]; p != null; p = p.next) {
				frequencyHashie.addOne(p.key.toLowerCase());
			}
		}
	}
	
	public double getUrlFC(String url) {
		Document doc;
		HashTable hashie = new HashTable();
		double fc = 0;
		String text = "";

		System.out.println("The url is " + url);
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception EH) {
			JOptionPane.showMessageDialog(frame, "Please enter a valid URL.", "Oops",
			        JOptionPane.WARNING_MESSAGE);
			textField.requestFocusInWindow();
			textField.selectAll();
			return -1;
		}

		text = doc.body().text();
		String[] wordCount = text.split(" ");

		for (int i = 0; i < wordCount.length; i++) {
			hashie.addOne(wordCount[i].toLowerCase());
		}
		for (int i = 0; i < hashie.table.length; i++) {
			for (HashTable.Node p = hashie.table[i]; p != null; p = p.next) {

				fc = tfidfCount(urlCount, frequencyHashie.get(p.key.toLowerCase()), hashie.table.length,
				        hashie.get(p.key.toLowerCase())) + fc;
			}
		}
		return fc;
	}
	
	public void addUrlToCache(String url) {
		double urlFc = getUrlFC(url);
		
		try {
			cache.addToCache(url, urlFc);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 929, 686);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField = new JTextField();
		textField.setColumns(10);

		final JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBackground(new Color(240, 240, 240));
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		

		final File urlFile = new File(URL_FILE);
		Scanner sc = new Scanner(urlFile);
		String[] urls = sc.next().split(",");
		//System.out.println(urls[0]);

		// Create a table that keeps track of the amount of times a document has a word
		// at least once in it.
		int treeCounter = 0;
		for (String url : urls) {
			BTree tempTree = new BTree(4);
			tempTree.insertKey(url.toLowerCase());
			Point p = new Point();
			clusters[treeCounter] = new Cluster(p.createPoint(url));
			urlsList[urlIndex] = url;
			String[] links = scraper.getLinks(url);
			setHashies(url);
			urlIndex++;
			int i = 0;
			for (String link : links) {
				tempTree.insertKey(link.toLowerCase());
				System.out.println(link);
				setHashies(link);
				urlIndex++;
				i++;
				if (i == 20) {
					break;
				}
			}
			trees[treeCounter] = tempTree;
			treeCounter ++;
		}


		// Iterate through the list of urls
		for (String url : urlsList) {

			if (url == null) {
				break;
			}
			Random r = new Random();
			Point temp = new Point();
			Point p = temp.createPoint(url);
			System.out.println("Adding to random cluster " + p.getKey());
			clusters[r.nextInt(5)].addPoint(p);
			addUrlToCache(url);
		}
		
		sc.close();

		final JButton btnSubmit = new JButton("Submit");

		// Action that occurs when the submit button is clicked
		Action action = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			/**
			 * Perform the calculations and insertions into the hash tables.
			 */
			public void actionPerformed(ActionEvent e) {

				String matchingUrl = "";
				double comparer = 0;
				System.out.println("You entered : " + textField.getText());
				fc = getUrlFC(textField.getText());
				
				if (fc == -1) {
					JOptionPane.showMessageDialog(frame, "Please enter a valid URL.", "Oops",
					        JOptionPane.WARNING_MESSAGE);
					textField.requestFocusInWindow();
					textField.selectAll();
					return;
				}
				System.out.println("The tfidf of the input url is ... " + fc);
				
				// Iterate through the list of urls
				for (String url : urlsList) {

					if (url == null) {
						break;
					}

					Document urlDoc = null;
					try {
						System.out.println("The url is ... " + url);
						urlDoc = Jsoup.connect(url).get();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					double urlFc = -1;
					try {
						System.out.println("The url is in the cache");
						urlFc = cache.getTfidfFromCache(url);
						if (urlFc == -1 ) {
							urlFc = getUrlFC(url);
							cache.addToCache(url, urlFc);
						}
						System.out.println("The frequency count for " + url + " is " + urlFc);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if (urlFc != -1) {
						System.out.println("This is what I am comparing with " + fc);
						System.out.println("This is what I am comparing against " + urlFc);
						double diff = Math.abs(fc - urlFc);
						
						if (comparer == 0) {
							matchingUrl = urlDoc.title();
							comparer = diff;
							foundUrl = url;
						} else if (diff == 0) {
							matchingUrl = urlDoc.title();
							foundUrl = url;
							break;
						} else if (diff < comparer) {
							matchingUrl = urlDoc.title();
							comparer = diff;
							foundUrl = url;
						} else {
						}
					}
				}
				
				int treeCount = 0;
				for (BTree tree : trees) {
					if (tree.keySearch(tree.getRoot(), foundUrl.toLowerCase(), 0) != null) {
						System.out.println("Matching tree is "  + treeCount);
						break;
					} else {
						System.out.println("Tree " + treeCount + " didn't work. Trying again.");
					}

					treeCount++;
				}
				
				Cluster closestCluster = null;
				double tempCalc = 0;
				KMedoids kmedoids = new KMedoids();
				for (Cluster cluster : clusters) {
					System.out.println("The center is " + cluster.getCenter().getKey());
					if (closestCluster == null) {
						closestCluster = cluster;
						tempCalc = kmedoids.calculate(cluster);
					} else {
						double calc = kmedoids.calculate(cluster);
						if (calc < tempCalc) {
							tempCalc = calc;
							closestCluster = cluster;
						}
					}
				}
				System.out.println(closestCluster.printCluster());
				textArea.setText("The closest match is: " + matchingUrl + "\n" + " The cluster for this url is " + closestCluster.printCluster());
			}
		};

		// This is a bunch of auto generate gui stuff
		btnSubmit.addActionListener(action);
		textField.addActionListener(action);

		JLabel lblNewLabel = new JLabel("Web Categorization Program");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        .addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
		                .addComponent(textField, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
		                .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSubmit).addContainerGap())
		        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
		                .addComponent(textArea, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE).addContainerGap()));
		groupLayout
		        .setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		                .addGroup(groupLayout.createSequentialGroup().addContainerGap()
		                        .addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
		                        .addGap(18)
		                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
		                                .addComponent(textField, GroupLayout.PREFERRED_SIZE, 22,
		                                        GroupLayout.PREFERRED_SIZE)
		                                .addComponent(btnSubmit))
		                        .addPreferredGap(ComponentPlacement.RELATED)
		                        .addComponent(textArea, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
		                        .addContainerGap()));
		frame.getContentPane().setLayout(groupLayout);
	}
}

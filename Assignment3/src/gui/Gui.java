package gui;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import WebUtilities.WebScraper;
import algorithm.Dijkstras;
import algorithm.Edge;
import algorithm.Graph;
import algorithm.Vertex;
import hasher.HashTable;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui {

	private JFrame frame;
	private JTextField urlOneTextField;
	private JTextField urlTwoTextField;
	private JScrollPane urlListScrollPane;
	private JScrollPane pathScrollPane;
	private static final String PATH = "C:/Users/rober/csc365-new-workspace/Assignment3/";
	private static final String URL_FILE = "C:/Users/rober/csc365-new-workspace/Assignment3/Urls.txt";
	int urlIndex = 0;
	int urlCount = 0;
	int disjointCount = 0;

	List<Vertex> vertexList = new ArrayList<Vertex>();
	List<Edge> edgeList = new ArrayList<Edge>();

	List<String> urlsList = new CopyOnWriteArrayList<String>();
	
	List<String> medoids = new ArrayList<String>();

	// This hash table will keep track of the amount of documents that contain a
	// certain word.
	final HashTable frequencyHashie = new HashTable();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
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
	public Gui() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 527, 662);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTextArea textArea = new JTextArea(5, 30);
		JTextArea pathTextArea = new JTextArea(5, 30);
		final JTextArea shortestPathTextArea = new JTextArea(5, 30);

		urlOneTextField = new JTextField();
		urlOneTextField.setColumns(10);

		urlTwoTextField = new JTextField();
		urlTwoTextField.setColumns(10);

		JButton findPathButton = new JButton("Find");
		// Action to do after button click
		findPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String urlOne = urlOneTextField.getText();
				String urlTwo = urlTwoTextField.getText();
				if (!urlsList.contains(urlOne) || !urlsList.contains(urlTwo)) {
					JOptionPane.showMessageDialog(frame, "Please enter a valid URL.", "Oops",
					        JOptionPane.WARNING_MESSAGE);
				} else {
					Dijkstras dijkstras = new Dijkstras();
					Vertex one = new Vertex("");
					Vertex two = new Vertex("");
					for (Vertex v : vertexList) {
						if(v.getName().equals(urlOne)) {
							one = v;
							System.out.println("Found one");
						}
						
						if(v.getName().equals(urlTwo)) {
							two = v;
							System.out.println("Found two");
						}
					}
					
					dijkstras.calculateShortestPath(one);
					List<Vertex> path = new ArrayList<Vertex>();
					path = dijkstras.getShortestPath(two);
					
					String pathText = "";
					for (Vertex v : path) {
						pathText = pathText + v.getName() + "\n";
					}

					for (String medoid : medoids) {
						if (pathText.contains(medoid)) {
							pathText = pathText + "You go through this medoid " + medoid + "\n";
						}
					}
					shortestPathTextArea.setText(pathText);
				}
			}
		});

		urlListScrollPane = new JScrollPane(textArea);

		pathScrollPane = new JScrollPane(pathTextArea);
		
		scrollPane = new JScrollPane(shortestPathTextArea);

		JList list = new JList();
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(findPathButton, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
						.addComponent(urlOneTextField, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
						.addComponent(urlTwoTextField, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
						.addComponent(urlListScrollPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(pathScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addContainerGap(207, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(61)
					.addComponent(urlOneTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(urlTwoTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(findPathButton)
						.addComponent(list, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(urlListScrollPane, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(pathScrollPane, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);

		final File urlFile = new File(URL_FILE);
		Scanner sc = new Scanner(urlFile);
		String[] urls = sc.next().split(",");
		sc.close();

		System.out.println("Setting url list ...");
		setUrlList(urls);

		System.out.println("Setting hash tables ...");

		for (String url : urlsList) {
			//System.out.println("Adding hash table for " + url);
			setHashies(url);
			count++;
			//System.out.println("Added hash table number " + count);
		}
		System.out.println("Got my hash tables ...");

		System.out.println("Setting urls to list ...");
		textArea.setText(getUrlText());
		System.out.println("I set my unique url list ...");
		
		

		System.out.println("Creating vertex and edges ... ");
		setVertexAndEdges(urls);
		System.out.println("Set the vertex and edges ... ");

		System.out.println("Creating the graph ...");
		Graph graph = new Graph(vertexList, edgeList);
		System.out.println("Graph created ...");
		System.out.println("Getting the disjoint list ...");
		List<String> disjointList = graph.disjoint();
		System.out.println("Generated disjoint list ...");
		System.out.println(getDisjoint(disjointList));
		pathTextArea.setText("The disjoint list: \n" + getDisjoint(disjointList));
		System.out.println("Creating serialized file ...");
		addToSerializedFile();
		System.out.println("Created serialized file ...");
		System.out.println("Starting ...");
	}
	
	public String getDisjoint(List<String> list) {
		String text = "";
		for(String item : list) {
			//System.out.println(item);
			text = text + item + "\n";
		}
		return text;
	}

	public void setUrlList(String[] urls) {

		WebScraper scraper = new WebScraper();

		for (String url : urls) {
			medoids.add(url);
			urlsList.add(url);
			urlCount++;
			String[] links = scraper.getLinks(url);
			for (String link : links) {
				if (link.contains("en.wikipedia.org") && !Character.isDigit(link.charAt(link.length() - 1))
						&& !urlsList.contains(link) && !link.contains("Main_Page")) {
					urlsList.add(link);
					urlCount++;
				} 
				//else if (urlsList.contains(link)) {
				//	disjointCount++;
				//}
			}
		}
		System.out.println("I found this many UNIQUE urls: " + urlCount);
		//System.out.println("There are this many disjoint sets: " + disjointCount);
	}

	public void setVertexAndEdges(String[] urls) {
		WebScraper scraper = new WebScraper();

		for (String url : urls) {
			Vertex v = new Vertex(url);
			double fc = getUrlFC(url);
			String[] links = scraper.getLinks(url);
			for (String link : links) {
				if (link.contains("en.wikipedia.org") && !Character.isDigit(link.charAt(link.length() - 1)) && !link.contains("Main_Page")) {
					double fcLink = getUrlFC(link);
					Vertex vLink = new Vertex(link);
					Edge linkToNull = new Edge(0, vLink, null);
					Edge linkToSource = new Edge(fc, vLink, v);
					Edge sourceToLink = new Edge(fcLink, v, vLink);
					vLink.addEdge(linkToNull);
					vLink.addEdge(linkToSource);
					v.addEdge(sourceToLink);
					edgeList.add(linkToNull);
					edgeList.add(linkToSource);
					edgeList.add(sourceToLink);
					vertexList.add(vLink);
				}
			}
			vertexList.add(v);
		}
	}

	public String getUrlText() {
		String text = "";
		for (String url : urlsList) {
			if (url != null) {
				text = text + url + "\n";
			}
		}
		return text;
	}

	public int count = 0;
	private JScrollPane scrollPane;
	
	public double getUrlFC(String url) {
		Document doc;
		HashTable hashie = new HashTable();
		double fc = 0;
		String text = "";

		count ++;
		//System.out.println("The url is " + url + " " + count);
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception EH) {
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

	public void setHashies(String link) throws IOException {
		// System.out.println(link);
		Document urlDoc = new Document("");
		HashTable urlHashie = new HashTable();
		try {
			urlDoc = Jsoup.connect(link).get();
		} catch (Exception e) {
			urlsList.remove(link);
			urlCount--;
			return;
		}
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

	public void addToSerializedFile() throws IOException {
		FileOutputStream fout = new FileOutputStream(PATH + "Urls.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(edgeList);
		fout.close();
		oos.close();
	}
}

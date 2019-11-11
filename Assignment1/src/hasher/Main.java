package hasher;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class Main {

	private JFrame frame;
	private JTextField textField;
	// Keeps track of the amount of urls
	int urlCount = 0;

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

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 552, 237);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField = new JTextField();
		textField.setColumns(10);

		final JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBackground(new Color(240, 240, 240));
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());

		// This hash table will keep track of the amount of documents that contain a
		// certain word.
		final HashTable frequencyHashie = new HashTable();

		final File urlFile = new File("C:\\Users\\rober\\csc365-new-workspace\\Assignment1\\src\\hasher\\URL_Text_File.txt");
		Scanner sc = new Scanner(urlFile);
		String[] urls = sc.next().split(",");

		// Create a table that keeps track of the amount of times a document has a word
		// at least once in it.
		for (String url : urls) {
			HashTable urlHashie = new HashTable();
			urlCount++;
			Document urlDoc = Jsoup.connect(url).get();
			String[] urlWordCount = urlDoc.body().text().split(" ");

			// Adds all elements to a hash table
			for (int i = 0; i < urlWordCount.length; i++) {
				urlHashie.addOne(urlWordCount[i].toLowerCase());
			}
			// Adds all keys to the frequency table
			for (int i = 0; i < urlHashie.table.length; i++) {
				for (HashTable.Node p = urlHashie.table[i]; p != null; p = p.next) {
					frequencyHashie.addOne(p.key.toLowerCase());
				}
			}
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

				Document doc;
				HashTable hashie = new HashTable();
				Scanner sc = null;

				String matchingUrl = "";
				String text = "";
				double comparer = 0;
				double fc = 0;

				try {
					doc = Jsoup.connect(textField.getText()).get();
				} catch (Exception EH) {
					JOptionPane.showMessageDialog(frame, "Please enter a valid URL.", "Oops",
					        JOptionPane.WARNING_MESSAGE);
					textField.requestFocusInWindow();
					textField.selectAll();
					return;
				}

				text = doc.body().text();
				String[] wordCount = text.split(" ");

				for (int i = 0; i < wordCount.length; i++) {
					hashie.addOne(wordCount[i].toLowerCase());
				}
				urlCount++;
				for (int i = 0; i < hashie.table.length; i++) {
					for (HashTable.Node p = hashie.table[i]; p != null; p = p.next) {
						frequencyHashie.addOne(p.key.toLowerCase());

						fc = tfidfCount(urlCount, frequencyHashie.get(p.key.toLowerCase()), hashie.table.length,
						        hashie.get(p.key.toLowerCase())) + fc;
					}
				}

				try {
					sc = new Scanner(urlFile);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				String[] urls = sc.next().split(",");

				for (String url : urls) {

					double urlFc = 0;

					HashTable urlHashie = new HashTable();
					Document urlDoc = null;

					try {
						urlDoc = Jsoup.connect(url).get();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					long webMills = 0;
					long localMills = 0;
					
					File cacheFile = new File("C:\\Users\\rober\\csc365-new-workspace\\Assignment1\\FileCache\\" + urlDoc.title().replaceAll(" ", "").replaceAll(":", "") + ".txt");
					
					try {
						String lastModified = Jsoup.connect(url).execute().header("Last-Modified");
						SimpleDateFormat sdf = new SimpleDateFormat("EEE',' dd MMM yyyy HH':'mm':'ss zzz");
						Date date = sdf.parse(lastModified);
						webMills = date.getTime();
						localMills = cacheFile.lastModified();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if(cacheFile.exists() && localMills > webMills) {
						try {
							/*
							FileReader reader = new FileReader(cacheFile);
							String data = reader.read();
							urlFc = reader.read();
							System.out.println(urlFc);
							*/
							System.out.println("Trying for " + cacheFile);
							String contents = new String(Files.readAllBytes(Paths.get(cacheFile.toString())));
							System.out.println(contents + " for " + cacheFile);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						if (cacheFile.exists()) {
							cacheFile.delete();
						}
						String[] urlWordCount = urlDoc.body().text().split(" ");
	
						for (int i = 0; i < urlWordCount.length; i++) {
							urlHashie.addOne(urlWordCount[i].toLowerCase());
						}
	
						for (int i = 0; i < urlHashie.table.length; i++) {
							for (HashTable.Node p = urlHashie.table[i]; p != null; p = p.next) {
									urlFc = tfidfCount(urlCount, frequencyHashie.get(p.key.toLowerCase()),
									        urlHashie.table.length, urlHashie.get(p.key.toLowerCase())) + urlFc;
							}
						}

						try {
							FileWriter writer;
							cacheFile.createNewFile();
							writer = new FileWriter(cacheFile);
							writer.write(String.valueOf(urlFc));
							writer.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					double diff = Math.abs(fc - urlFc);
					
					if (comparer == 0) {
						matchingUrl = urlDoc.title();
						comparer = diff;
					} else if (urlFc == 0) {
						matchingUrl = urlDoc.title();
					} else if (diff < comparer) {
						matchingUrl = urlDoc.title();
						comparer = diff;
					} else {
					}

				}

				textArea.setText("The closest match is: " + matchingUrl);
				sc.close();
				//btnSubmit.setEnabled(false);
			}
		};

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

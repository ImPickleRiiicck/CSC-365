package WebUtilities;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
	
	public static String[] urlLinks;

	public WebScraper() {}
	
	public String[] getLinks(String url) {
		Document doc = null;
		int i = 0;
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			urlLinks = new String[links.size()];
			for (Element link: links) {
				urlLinks[i] = link.attr("abs:href").toString();
				i++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return urlLinks;
	}
}

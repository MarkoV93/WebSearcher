package com.markoproject.runnebleImpl;

import com.markoproject.service.ThreadManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for scanning web pages and finding in them links and words
 * implementation Runnable
 */
public class Scanner implements Runnable {


    private static final Logger logger = LogManager.getLogger(Scanner.class);
    private String word;
    private String url;
    private Map<String, String> results;
    BlockingQueue<String> linksForScanning;
    int count;//count for count of wordns on page

    //@params : word(searching word) ,url( url for scanning), results( for mapping pages :url-key , error link or count of words on page-value),
// linksForScanning(BlockingQueue for puting links for scanning by next Scanners)
    public Scanner(String word, String url, Map<String, String> results, BlockingQueue<String> linksForScanning) {
        this.url = url;
        this.word = word;
        this.results = results;
        this.linksForScanning = linksForScanning;
    }

    public void run() {
        try {
            count = 0;//count for
            results.put(url.toLowerCase(), "_____downloading");//put this url in result with status dounloading"
            Document doc = Jsoup.connect(url).timeout(0).get();//Get page
            Elements links = doc.body().select("a[href]");//get oll links on this page
            String text = doc.body().text();//Get only text from body
            Pattern p1 = Pattern.compile(word, Pattern.CASE_INSENSITIVE);//create pattern for word.If you want to find individual words uncomment next line
            // Pattern p1 = Pattern.compile(word+"[\\.|\\s|\\!|\\?|\\:\\-|s|es|'s]", Pattern.CASE_INSENSITIVE);
            Matcher m1 = p1.matcher(text);
            Pattern p2 = Pattern.compile("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");//create pattern for links like http and https(under the condition)
            results.put(url.toLowerCase(), "________scanning");//put this url in result with status "scanning"
            while (m1.find()) {//counting words
                count++;
            }
            results.put(url.toLowerCase(), "_______________" + count);//put this url in result with status "count of words on this page"

            for (Element e : links) {
                if (linksForScanning.size() == 10) {//For end of finding words by this scanner of BlockingQueue<String> has max size
                    break;
                }
                Matcher m2 = p2.matcher(e.attributes().toString());//scan of link with pattern "http"
                if (m2.find()) {
                    String newUrl = m2.group();
                    try {
                        doc = Jsoup.connect(newUrl).get();//try to connect
                        if (!results.containsKey(newUrl) && !linksForScanning.contains(newUrl)) {//if this link unique
                            linksForScanning.add(newUrl);//add to queue for scanning by next scanner
                        }

                    } catch (HttpStatusException ex) {
                        results.put(newUrl.toLowerCase(), ":http error");//if connection is failed, then put this url in result with status "count of words on this page"
                        logger.warn("bed url http error"+ newUrl );
                    } catch (UnsupportedMimeTypeException ex) {
                        System.out.println(newUrl + " BedURL ");
                        results.put(newUrl, "bed url UnsupportedMimeTypeException");//if type if file with this url not suported, then put this url in result with status "count of words on this page"
                        logger.warn("bed url UnsupportedMimeTypeException"+ newUrl );
                    }
                }
            }
        } catch (IOException e) {//oonly if first url whrows exception
            results.put(url, "error, bad link");
            logger.warn("Bad first url"+url );

        }
    }
}

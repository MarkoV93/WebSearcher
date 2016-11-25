package markoproject.runnebleImpl;

import com.markoproject.runnebleImpl.Scanner;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marko on 25.11.2016.
 */
public class ScannerTest {

    @Test
    public void scanPage() {

        int count = 0;
        Map<String, String> results = new LinkedHashMap<String, String>();
        Queue<String> linksForScunning = new ArrayBlockingQueue<String>(10);
        Runnable scanner = new Scanner("java", "https://help.github.com/articles/searching-repositories/", results, (BlockingQueue<String>) linksForScunning);
        Thread thread = new Thread(scanner);
        thread.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(results);
        Pattern p = Pattern.compile("[___]*(\\d)");
        Matcher m = p.matcher(results.toString());
        if (m.find()) {
            count = Integer.parseInt(m.group(1));


        }
        Assert.assertEquals(count, 2);
    }
}

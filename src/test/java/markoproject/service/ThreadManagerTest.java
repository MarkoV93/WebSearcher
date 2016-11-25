package markoproject.service;

import com.markoproject.objects.SearchPropertiesWrapper;
import com.markoproject.service.ThreadManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marko on 25.11.2016.
 */
public class ThreadManagerTest {
    @Test
    public void scanByThreads() throws InterruptedException {

        int count = 0;
        ThreadManager threadManager = new ThreadManager();
        SearchPropertiesWrapper pw = new SearchPropertiesWrapper();
        pw.setCountOfLinks(10);
        pw.setCountOfThreads(5);
        pw.setUrl("http://cursor.education/course/java-basic");
        pw.setWord("java");
        threadManager.startScanning(pw);
        while (!threadManager.getLoaded().equals("100%")) {

        }
        Pattern p = Pattern.compile("[_]*\\d");
        int complite = 0;
        List<String> list = new ArrayList<String>(threadManager.getResults().values());
        for (String str : list) {

            Matcher m = p.matcher(str);
            if (m.find()) {
                count++;
            }


        }
        Assert.assertEquals(count, 10);
    }
}

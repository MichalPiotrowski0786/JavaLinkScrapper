package mp.ug;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        final URL otodomURL = new URL("https://www.otodom.pl/pl/oferty/wynajem/mieszkanie/sopot");
        runThreadsExecutorsService(otodomURL);
    }

    private static void runThreadsExecutorsService(URL url) {
        ExecutorService es = Executors.newFixedThreadPool(4);
        es.submit(() -> {
            try {
                runLinkScrapper(url);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        });

        es.shutdown();
    }

    private static void runLinkScrapper(URL url) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine = inputReader.readLine();
        List<String> scrappedUrls = extractUrls(inputLine);
        Object[] scrappedUrlsArray = scrappedUrls.toArray();

        int index = 0;
        for(Object t : scrappedUrlsArray) {
            if(t.getClass() == String.class) {
                System.out.println("["+index +"]: "+ t);
                index++;
            }
        }
        inputReader.close();
    }

    public static List<String> extractUrls(String text)
    {
        // https://stackoverflow.com/a/28269120
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}

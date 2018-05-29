package web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static String getHtmlText(String url){
        String response=null;

        try {
            URL htmlUrl=new URL(url);
            Scanner scanner=new Scanner(htmlUrl.openStream());
            scanner.useDelimiter("\\Z");
            response=scanner.next();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    public static List<String> parseLinksFromHtml(String html, String prefix) {

        List<String> links = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("a");

        for (Element element : elements) {
            String link = element.attr("href");
            if (link != null && link.endsWith(".mp3")) {
                if (prefix != null) {
                    link = prefix.concat(link);
                }
                links.add(link);
            }
        }

        return links;
    }
}

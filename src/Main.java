import web.Downloader;
import web.Parser;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Random;

public class Main {

    public static final String BASE_URL = "http://holychords.com";
    public static final String BASE_SAVE_PATH = "E:\\Download\\";


    public static void main(String[] args) {

        Downloader downloader = new Downloader();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));


        while (true) {
            String url = null;
            try {
                System.out.print("\nUrl to web page:");
                url = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (url != null) {

                System.out.println("Getting HTML...");
                String htmlText = Parser.getHtmlText(url);

                //Fetching song's links from Html response
                if (htmlText != null) {
                    List<String> links = Parser.parseLinksFromHtml(htmlText, BASE_URL);

                    if (!links.isEmpty()) {
                        System.out.println("Found songs:" + links.size());
                        System.out.println("Downloading...");

                        for (String link : links) {

                            System.out.println();
                            System.out.println("#"+(links.indexOf(link)+1));
                            System.out.println(link);

                            if (downloader.downloadAndSave(link, BASE_SAVE_PATH)) {
                                System.out.println("DOWNLOAD SUCCESSFUL");

                            } else {
                                System.out.println("DOWNLOAD UNSUCCESSFUL!!!");
                            }
                        }
                        System.out.println("-------- FINISH ---------");
                        try {
                            Desktop.getDesktop().open(new File(BASE_SAVE_PATH));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("\nError getting Html text!!!");
                }
            } else {
                System.out.println("Wrong url!!!");
            }
        }
    }
}

package web;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class Downloader {

    private static Random rand;

    public boolean downloadAndSave(String urlPath, String saveRootFolderPath) {
        rand = new Random();
        boolean result = false;

        InputStream inputStream = null;
        OutputStream outputStream = null;
        String fullPath=null;
        try {
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
            fullPath=saveRootFolderPath.concat(String.valueOf(rand.nextLong()).concat(".mp3"));
            outputStream = new FileOutputStream(new File(fullPath));

            byte[] bytes = new byte[4096];
            int length;
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }

            inputStream.close();
            outputStream.close();
            result = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(result==true && fullPath!=null){
            String renamePath= renameSongTitle(fullPath);
            System.out.println(renamePath);
        }
        return result;
    }

    private String renameSongTitle(String fullPath) {
        File file=new File(fullPath);
        String result="";

        if(file.exists()){

            String title=getSongName(fullPath);
            if(title!=null&&!title.isEmpty()){
                String renamePath=fullPath
                        .substring(0,fullPath.lastIndexOf("\\") + 1)
                        .concat(getSongAuthor(fullPath))
                        .concat(" - ")
                        .concat(title)
                        .concat(".mp3");
                File renameFile=new File(renamePath);
                if(file.renameTo(renameFile)){
                    result=renamePath;
                }
            }
        }
        return result;
    }

    public static String getSongName(String songPath) {

        String name = "";

        try {
            AudioFile audioFile = AudioFileIO.read(new File(songPath));
            Tag tag = audioFile.getTag();
            name = tag.getFirst(FieldKey.TITLE);
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        return name;
    }
    public static String getSongAuthor(String songPath) {

        String name = "";

        try {
            AudioFile audioFile = AudioFileIO.read(new File(songPath));
            Tag tag = audioFile.getTag();
            name = tag.getFirst(FieldKey.ARTIST);
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        return name;
    }
}


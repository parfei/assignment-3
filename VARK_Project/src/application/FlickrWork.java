package application;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FlickrWork extends Task<String> {
    private String _term;
    private int _num;
    private ArrayList<String> _html = new ArrayList<String>();

    public FlickrWork(String term, String num){
        _term = term;
        _num = Integer.parseInt(num);
    }

    @Override
    protected String call() throws Exception {
        // https://www.flickr.com/search/?text=_term;
        /*getHTML();
        downloadImgs();*/
        try {
            getPhotos();
        } catch (RuntimeException e){
            return "0";
        }
        System.out.println("flickr done");
        return Integer.toString(_num);
    }

    private String getAPIKey(String key) throws IOException {
        String config = PathCD.getPathInstance().getPath().substring(0, PathCD.getPathInstance().getPath().indexOf("/out"))
                + "/src/application/config/flickr-api-key.txt";
        File file = new File(config);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;
            while ( (line = br.readLine()) != null ) {
                if (line.trim().startsWith(key)) {
                    br.close();
                    return line.substring(line.indexOf("=")+1).trim();
                }
            }
            br.close();
            throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getPhotos(){
        try {
            String apiKey = getAPIKey("apikey");
            String sharedSecret = getAPIKey("secret");

            Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

            String query = _term;
            int resultsPerPage = _num;
            int page = 0;

            PhotosInterface photos = flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            params.setSort(SearchParameters.RELEVANCE);
            params.setMedia("photos");
            params.setText(query);

            PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
            System.out.println("Retrieving " + results.size()+ " results");

            int count = 1;
            for (Photo photo: results) {
                try {
                    BufferedImage image = photos.getImage(photo, Size.LARGE);
                    String filename = "img" + Integer.toString(count) + ".jpg";
                    File outputfile = new File(PathCD.getPathInstance().getPath() + "/mydir/extra",filename);
                    ImageIO.write(image, "jpg", outputfile);
                    System.out.println("Downloaded "+filename);

                    count++;
                } catch (FlickrException fe) {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); //TODO throw exception if cannot find images for the specific term or not enough images?
            throw new RuntimeException("No Flickr results for " + _term + ". Creating a blue video instead...");
        }

        System.out.println("\nDone");
    }

}

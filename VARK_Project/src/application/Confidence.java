package application;

import application.bashwork.ManageFolder;
import javafx.concurrent.Task;

import java.io.File;

public class Confidence extends Task<Boolean> {
    private int _rating;
    private String _rateFilePath;

    public Confidence(String creation, int rating) throws Exception {
        _rating = rating;
        _rateFilePath = ManageFolder.findPath(creation, false);
        /*File file = new File(ManageFolder.findPath(creation, false));
        _rateFilePath = file.getAbsoluteFile().getParent();
        _rateFilePath = _rateFilePath.substring(_rateFilePath.lastIndexOf("/"));*/ //TODO finding term code.
    }

    @Override
    protected Boolean call() throws Exception {
        ManageFolder.writeToFile(_rateFilePath + "/confidence.txt", Integer.toString(_rating));
        return true;
    }
}

package application.listeners;

import application.bashwork.ManageFolder;
import javafx.scene.control.ListCell;

public class CreationListCell extends ListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
            return;
        }

        String confidence = null;
        try {
            setText(item);
            confidence = ManageFolder.readFile(ManageFolder.findPath(item, false) + "/confidence.txt");
            String plays = ManageFolder.readFile(ManageFolder.findPath(item, false) + "/plays.txt");

            String bgStyle = "";
            String textStyle = "";

            if (Integer.parseInt(plays) == 0) { //If video has never been played. //TODO after play video once, confidence has to be set
                bgStyle += "#93D4EE;";
                textStyle += "white;";
            } else if (Integer.parseInt(confidence) < 3) { //If confidence is below 3
                bgStyle += "orange;";
                textStyle += "white;";
            } else {
                bgStyle += "white;";
                textStyle += "black;";
            }

            setStyle("-fx-background-color:" + bgStyle + "text-fill:" + textStyle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

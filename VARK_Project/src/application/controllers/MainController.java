package application.controllers;

import application.ChangeScene;
import application.Main;
import application.values.PicPath;
import application.values.SceneFXML;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {

    private ChangeScene _changeSceneObject=new ChangeScene();
    @FXML private AnchorPane TOPVIEW;
    @FXML private ImageView creatingImg;
    @FXML private Button starBtn;

    private static final Image LOADING = new Image(PicPath.MENU + "/download.png");
    private static final Image PLACEHOLDER = new Image(PicPath.MENU + "/placeholder.png");



    @FXML
    public void initialize() throws IOException {
        this.setTOPVIEW(SceneFXML.MENU.toString());
        creationInProgress(false);
    }

    /**
     * When this method is called, it will change the Scene to Create view
     * @param event
     * @throws IOException
     */

    @FXML
    public void create(ActionEvent event) throws IOException {
        popupHelper("Enter a word to search up!");
        this.setTOPVIEW(SceneFXML.SEARCH.toString());
    }

    /**
     * When this method is called, it will change the Scene to View creations
     * @param event
     * @throws IOException
     */

    @FXML
    public void view(ActionEvent event)throws IOException{
        popupHelper("Click on a creation to get started!");
        this.setTOPVIEW(SceneFXML.VIEW.toString());
    }

    /**
     * show instructions when user click star button
     * @param event
     * @throws IOException
     */
    @FXML
    public void showInstructions(ActionEvent event) throws IOException {
        starBtn.setDisable(true);
        Popup instructions = popupHelper("I show tips from time to time!");
        Executors.newSingleThreadExecutor().submit(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    instructions.hide();
                    starBtn.setDisable(false);
                });
                return null;
            }
        });
    }

    /**
     *
     * @param layout
     * @return
     * @throws IOException
     */

    public Object setTOPVIEW(String layout) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(layout));
        Parent node = loader.load();
        TOPVIEW.getChildren().setAll(node);

        return loader.getController();
    }


    /**
     * show user a downloading image when creation is still being made
     * @param inProgress
     */
    public void creationInProgress(Boolean inProgress){
        if (inProgress){
            creatingImg.setImage(LOADING);
        } else {
            creatingImg.setImage(PLACEHOLDER);
        }
    }

    /**
     * popupHelper helps to create a new pop up
     * @param text
     * @return
     * @throws IOException
     */

    public Popup popupHelper(String text) throws IOException {
        Popup popup = new Popup();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(SceneFXML.TIP.toString()));
        popup.getContent().add((Parent)loader.load());
        ((TipController) loader.getController()).setTipText(text);
        popup.setAnchorX(creatingImg.getX() - 100);
        popup.setAnchorY(creatingImg.getY() + 450);

        popup.show((Stage)TOPVIEW.getScene().getWindow());

        return popup;
    }

}


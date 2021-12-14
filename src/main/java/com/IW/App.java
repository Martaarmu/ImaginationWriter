package com.IW;

import com.IW.utils.Dialog;
import com.IW.utils.PersistenceUnit;
import com.IW.utils.Tools;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App extends Application {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage primaryStage) {
        PersistenceUnit.init();
        loadScene(primaryStage, "login", " Imagination Writer - Login", false, false);
    }

    private static Parent loadFXML(String fxml) {
        Parent result;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        try {
            result = fxmlLoader.load();
        } catch (IOException e) {
            logger.error("Hubo un error al cargar la vista " + fxml + " con el mensaje:\n" + e.getMessage());
            Dialog.showError("ERROR", "Hubo un error al cargar la vista", "La vista " + fxml + " no pudo cargarse debido a:\n " + e.getMessage());
            result = null;
        }
        return result;
    }

    /**
     * This method is used to load new Scenes
     * @param stage instance always a new Stage
     * @param fxml string of the fxml without the extension
     * @param title title of the window
     * @param SaW if the window must wait to the previous one
     * @param isResizable if thw window can be or not resizable
     */

    public static void loadScene(Stage stage, String fxml, String title, boolean SaW, boolean isResizable) {
        stage.setScene(new Scene(loadFXML(fxml)));
        Tools.addCssAndIcon(stage);
        new JMetro(Style.DARK).setScene(stage.getScene());
        stage.setTitle(title);
        stage.setResizable(isResizable);
        if (SaW)
            stage.showAndWait();
        else
            stage.show();
    }

    /**
     * This method is used to close a window
     *
     * @param stage stage that we want to close
     */
    public static void closeScene(Stage stage) {
        stage.close();
    }

    public static void main(String[] args) {
        launch();
        PersistenceUnit.closeAllConnections();
    }
}

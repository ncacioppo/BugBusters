package bugbusters.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Navigation {

    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    @FXML
    public static void toCompareSchedules(BorderPane pane) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        URL url = new File("src/main/java/bugbusters/UI/CompareSchedules.fxml").toURI().toURL();
        root = FXMLLoader.load(url);;
        stage = (Stage)(pane.getScene().getWindow());
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toSearchCalendar(BorderPane pane) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        URL url = new File("src/main/java/bugbusters/UI/SearchCalendar.fxml").toURI().toURL();
        root = FXMLLoader.load(url);;
        stage = (Stage)(pane.getScene().getWindow());
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toLogin(BorderPane pane) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        URL url = new File("src/main/java/bugbusters/UI/Login.fxml").toURI().toURL();
        root = FXMLLoader.load(url);;
        stage = (Stage)(pane.getScene().getWindow());
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toSearchList(BorderPane pane) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        URL url = new File("src/main/java/bugbusters/UI/SearchList.fxml").toURI().toURL();
        root = FXMLLoader.load(url);;
        stage = (Stage)(pane.getScene().getWindow());
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toUserSchedules(BorderPane pane) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        URL url = new File("src/main/java/bugbusters/UI/UserSchedules.fxml").toURI().toURL();
        root = FXMLLoader.load(url);;
        stage = (Stage)(pane.getScene().getWindow());
        scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }



}

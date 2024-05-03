package bugbusters.UI;

import bugbusters.Course;
import bugbusters.Schedule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Navigation {

    private static Stage stage;
    private static Scene scene;
    private static Parent root;
    private static Schedule currentSchedule;
    private static Course currentCourse; // todo: make these work

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

    @FXML
    public static void handleConflict(BorderPane pane) throws IOException {
        boolean courseAdded = currentSchedule.addCourse(currentCourse);

        if (courseAdded) return;

        Course currentCourse = currentSchedule.currentConflict.getKey();
        Course conflictingCourse = currentSchedule.currentConflict.getValue();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conflict Alert");
        alert.setHeaderText("There is a conflict with an existing course on the schedule.");
        alert.setContentText("Choose which course you would like to keep.");

        ButtonType buttonTypeOne = new ButtonType("Keep " + currentCourse.getName());
        ButtonType buttonTypeTwo = new ButtonType("Replace with " + conflictingCourse.getName());
        ButtonType buttonTypeThree = new ButtonType("Find Other Options");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            currentSchedule.resolveConflict(currentCourse, conflictingCourse);
        } else if (result.get() == buttonTypeTwo) {
            currentSchedule.resolveConflict(conflictingCourse, currentCourse);
        } else if (result.get() == buttonTypeThree) {
            // search for other classes/sections // todo: figure out how to refer back to search
        } else if (result.get() == buttonTypeCancel){
            // ... user chose CANCEL or closed the dialog
        }
        alert.close();
    }

    @FXML
    public TableView createCalendarView(){
        TableView calendar = new TableView();
        return calendar;
    }
}

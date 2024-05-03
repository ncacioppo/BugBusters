package bugbusters.UI;

import bugbusters.Course;
import bugbusters.Day;
import bugbusters.MeetingTime;
import bugbusters.Schedule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import static bugbusters.UI.Globals.currentCourse;
import static bugbusters.UI.Globals.currentSchedule;

public class Navigation {

    private static Stage stage;
    private static Scene scene;
    private static Parent root;
//    private static Schedule currentSchedule;
//    private static Course currentCourse; // todo: make these work

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

        System.out.println(courseAdded);
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
    public static GridPane createCalendarView(Schedule schedule){
        // Create the GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add labels for days of the week
        int count = 0;
        for (Day day : Day.values()){
            Label dayLabel = new Label(day.toString());
            gridPane.add(dayLabel, count + 1, 0);
            GridPane.setHalignment(dayLabel, HPos.CENTER);
            count += 1;
        }

        for (int i = 7; i < 21; i++) {
            gridPane.add(new Label(String.format("%02d:00", i)), 0, i + 1);
        }

        // Add labels for class meetings
        for (Course course : schedule.getCourses()) {
            for (MeetingTime meet : course.getMeetingTimes()){
                int colIndex = meet.getDay().ordinal() + 1; // Offset by 1 to skip day labels column
                int rowIndexStart = meet.getStartTime().getHour() +1 ;
                int rowIndexEnd = meet.getEndTime().getHour() + 1;
                Label label = new Label(course.getName());
                label.setAlignment(Pos.CENTER);
                gridPane.add(label, colIndex, rowIndexStart);
                GridPane.setRowSpan(new Label(course.getName()), rowIndexEnd - rowIndexStart + 1);
            }
        }
        return gridPane;
    }
}
